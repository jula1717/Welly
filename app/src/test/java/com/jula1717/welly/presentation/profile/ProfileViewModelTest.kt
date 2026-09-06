package com.jula1717.welly.presentation.profile

import com.jula1717.welly.MainDispatcherRule
import com.jula1717.welly.domain.model.ActivityLevel
import com.jula1717.welly.domain.model.BiologicalSex
import com.jula1717.welly.domain.model.NutritionGoal
import com.jula1717.welly.domain.model.UserProfile
import com.jula1717.welly.domain.repository.UserProfileRepository
import com.jula1717.welly.domain.usecase.ObserveUserProfileUseCase
import com.jula1717.welly.domain.usecase.UpdateUserProfileUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.time.Clock
import java.time.LocalDate
import java.time.ZoneOffset

class ProfileViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val today = LocalDate.of(2026, 9, 4)
    private val clock = Clock.fixed(today.atStartOfDay(ZoneOffset.UTC).toInstant(), ZoneOffset.UTC)

    private var collectJob: Job? = null
    private val effects = mutableListOf<ProfileEffect>()

    @After
    fun tearDown() {
        collectJob?.cancel()
    }

    private fun buildViewModel(
        storedProfile: UserProfile? = null,
        repository: FakeUserProfileRepository = FakeUserProfileRepository(storedProfile),
    ): Pair<ProfileViewModel, FakeUserProfileRepository> {
        val viewModel = ProfileViewModel(
            observeUserProfile = ObserveUserProfileUseCase(repository),
            updateUserProfile = UpdateUserProfileUseCase(repository),
            clock = clock,
        )
        collectJob = CoroutineScope(Dispatchers.Main).launch {
            viewModel.effects.collect { effects.add(it) }
        }
        return viewModel to repository
    }

    /** Fills every field a brand-new profile needs so the remaining assertion isolates one rule. */
    private fun ProfileViewModel.enterCompleteProfile() {
        onEvent(ProfileUiEvent.OnDateOfBirthChanged(LocalDate.of(1996, 6, 12)))
        onEvent(ProfileUiEvent.OnHeightChanged(178))
        onEvent(ProfileUiEvent.OnWeightChanged("72"))
        onEvent(ProfileUiEvent.OnSexChanged(BiologicalSex.Female))
        onEvent(ProfileUiEvent.OnActivityLevelChanged(ActivityLevel.Moderate))
        onEvent(ProfileUiEvent.OnGoalChanged(NutritionGoal.Maintain))
    }

    @Test
    fun `falls back to defaults when no profile is stored`() {
        val (viewModel, _) = buildViewModel(storedProfile = null)

        val state = viewModel.uiState.value

        assertTrue(state.isLoaded)
        assertFalse(state.loadFailed)
        // Nothing is pre-set: every profile field stays null until the user provides it.
        assertNull(state.dateOfBirth)
        assertNull(state.heightCm)
        assertEquals(null, state.weightKg)
        assertEquals(BiologicalSex.Unspecified, state.sex)
        assertEquals(null, state.activityLevel)
        assertEquals(null, state.goal)
        assertFalse(state.canSave)
    }

    @Test
    fun `prefills state from a stored profile`() {
        val stored = UserProfile(
            sex = BiologicalSex.Male,
            dateOfBirth = LocalDate.of(1990, 1, 1),
            heightCm = 182,
            weightKg = 78.0,
            activityLevel = ActivityLevel.Active,
            goal = NutritionGoal.Gain,
        )

        val (viewModel, _) = buildViewModel(storedProfile = stored)

        val state = viewModel.uiState.value
        assertTrue(state.isLoaded)
        assertEquals(stored.dateOfBirth, state.dateOfBirth)
        assertEquals(182, state.heightCm)
        assertEquals(78.0, state.weightKg!!, 0.0)
        assertEquals(BiologicalSex.Male, state.sex)
        assertEquals(ActivityLevel.Active, state.activityLevel)
        assertEquals(NutritionGoal.Gain, state.goal)
        // A stored profile is complete and can be re-saved without re-touching every field.
        assertTrue(state.canSave)
    }

    @Test
    fun `save persists the edited profile and navigates back`() {
        val (viewModel, repository) = buildViewModel()

        viewModel.onEvent(ProfileUiEvent.OnDateOfBirthChanged(LocalDate.of(1994, 3, 20)))
        viewModel.onEvent(ProfileUiEvent.OnHeightChanged(190))
        viewModel.onEvent(ProfileUiEvent.OnWeightChanged("85.5"))
        viewModel.onEvent(ProfileUiEvent.OnSexChanged(BiologicalSex.Male))
        viewModel.onEvent(ProfileUiEvent.OnActivityLevelChanged(ActivityLevel.Active))
        viewModel.onEvent(ProfileUiEvent.OnGoalChanged(NutritionGoal.Gain))
        viewModel.onEvent(ProfileUiEvent.OnSave)

        val saved = repository.savedProfile
        assertEquals(LocalDate.of(1994, 3, 20), saved?.dateOfBirth)
        assertEquals(190, saved?.heightCm)
        assertEquals(85.5, saved?.weightKg ?: 0.0, 0.0)
        assertEquals(BiologicalSex.Male, saved?.sex)
        assertEquals(ActivityLevel.Active, saved?.activityLevel)
        assertEquals(NutritionGoal.Gain, saved?.goal)
        assertEquals(listOf(ProfileEffect.NavigateBack), effects)
        assertFalse(viewModel.uiState.value.isSaving)
    }

    @Test
    fun `save stays disabled until every required field is provided`() {
        val (viewModel, repository) = buildViewModel()

        // Sex is intentionally left untouched: Unspecified is a valid choice, not a missing one.
        viewModel.onEvent(ProfileUiEvent.OnDateOfBirthChanged(LocalDate.of(1996, 6, 12)))
        assertFalse(viewModel.uiState.value.canSave)
        viewModel.onEvent(ProfileUiEvent.OnSave)
        assertEquals(null, repository.savedProfile)

        viewModel.onEvent(ProfileUiEvent.OnHeightChanged(178))
        assertFalse(viewModel.uiState.value.canSave)

        viewModel.onEvent(ProfileUiEvent.OnWeightChanged("72"))
        assertFalse(viewModel.uiState.value.canSave)

        viewModel.onEvent(ProfileUiEvent.OnActivityLevelChanged(ActivityLevel.Moderate))
        assertFalse(viewModel.uiState.value.canSave)

        viewModel.onEvent(ProfileUiEvent.OnGoalChanged(NutritionGoal.Maintain))
        assertTrue(viewModel.uiState.value.canSave)

        viewModel.onEvent(ProfileUiEvent.OnSave)
        assertEquals(NutritionGoal.Maintain, repository.savedProfile?.goal)
    }

    @Test
    fun `save is ignored while a numeric field is out of range or unparseable`() {
        val (viewModel, repository) = buildViewModel()
        viewModel.enterCompleteProfile()
        assertTrue(viewModel.uiState.value.canSave)

        viewModel.onEvent(ProfileUiEvent.OnHeightChanged(ProfileUiState.MAX_HEIGHT_CM + 1))
        assertFalse(viewModel.uiState.value.canSave)
        viewModel.onEvent(ProfileUiEvent.OnHeightChanged(178))

        viewModel.onEvent(ProfileUiEvent.OnWeightChanged("546"))
        assertFalse(viewModel.uiState.value.canSave)

        viewModel.onEvent(ProfileUiEvent.OnWeightChanged(""))
        assertFalse(viewModel.uiState.value.canSave)

        viewModel.onEvent(ProfileUiEvent.OnWeightChanged("abc"))
        assertNull(viewModel.uiState.value.weightKg)
        assertFalse(viewModel.uiState.value.canSave)

        viewModel.onEvent(ProfileUiEvent.OnSave)
        assertEquals(null, repository.savedProfile)
        assertTrue(effects.isEmpty())
    }

    @Test
    fun `finishes loading with a flag when the stored profile can't be read`() {
        val (viewModel, _) = buildViewModel(repository = FakeUserProfileRepository(null, failObserve = true))

        val state = viewModel.uiState.value
        assertTrue(state.isLoaded)
        assertTrue(state.loadFailed)
    }

    @Test
    fun `keeps the user on the screen with an error flag when saving fails`() {
        val (viewModel, _) = buildViewModel(repository = FakeUserProfileRepository(null, failUpdate = true))

        viewModel.enterCompleteProfile()
        viewModel.onEvent(ProfileUiEvent.OnSave)

        val state = viewModel.uiState.value
        assertTrue(state.saveFailed)
        assertFalse(state.isSaving)
        assertTrue(effects.isEmpty())
    }

    private class FakeUserProfileRepository(
        private var profile: UserProfile?,
        private val failObserve: Boolean = false,
        private val failUpdate: Boolean = false,
    ) : UserProfileRepository {
        var savedProfile: UserProfile? = null
            private set

        override fun observeProfile(): Flow<UserProfile?> {
            if (failObserve) return flow { error("boom") }
            return flowOf(profile)
        }

        override suspend fun updateProfile(profile: UserProfile) {
            if (failUpdate) error("boom")
            savedProfile = profile
            this.profile = profile
        }
    }
}
