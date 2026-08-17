package com.jula1717.welly.presentation.adddrink

sealed interface AddDrinkEffect {
    data object NavigateBack : AddDrinkEffect
}
