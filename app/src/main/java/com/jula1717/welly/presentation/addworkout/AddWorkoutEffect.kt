package com.jula1717.welly.presentation.addworkout

sealed interface AddWorkoutEffect {
    data object NavigateBack : AddWorkoutEffect
}
