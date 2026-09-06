package com.jula1717.welly.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.jula1717.welly.data.datastore.putUserProfile
import com.jula1717.welly.data.datastore.toUserProfile
import com.jula1717.welly.domain.model.UserProfile
import com.jula1717.welly.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class UserProfileRepositoryImpl
    @Inject
    constructor(
        private val dataStore: DataStore<Preferences>,
    ) : UserProfileRepository {
        override fun observeProfile(): Flow<UserProfile?> =
            dataStore.data
                .catch { cause ->
                    if (cause is IOException) emit(emptyPreferences()) else throw cause
                }.map { it.toUserProfile() }

        override suspend fun updateProfile(profile: UserProfile) {
            dataStore.edit { it.putUserProfile(profile) }
        }
    }
