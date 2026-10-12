package com.jula1717.welly.presentation.habitform

sealed interface HabitFormEffect {
    data object NavigateBack : HabitFormEffect
}
