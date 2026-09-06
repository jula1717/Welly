package com.jula1717.welly.presentation.profile

sealed interface ProfileEffect {
    data object NavigateBack : ProfileEffect
}
