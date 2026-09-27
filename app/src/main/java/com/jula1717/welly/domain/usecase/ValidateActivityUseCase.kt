package com.jula1717.welly.domain.usecase

import javax.inject.Inject

class ValidateActivityUseCase
    @Inject
    constructor() {
        operator fun invoke(name: String): Boolean = name.isNotBlank() && !name.first().isWhitespace()
    }
