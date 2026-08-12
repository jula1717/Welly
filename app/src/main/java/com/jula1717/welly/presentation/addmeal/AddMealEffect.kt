package com.jula1717.welly.presentation.addmeal

sealed interface AddMealEffect {
    data object NavigateBack : AddMealEffect
}
