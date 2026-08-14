package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.Drink
import com.jula1717.welly.domain.repository.DrinkRepository
import javax.inject.Inject

class AddDrinkUseCase
    @Inject
    constructor(
        private val repository: DrinkRepository,
    ) {
        suspend operator fun invoke(drink: Drink) = repository.addDrink(drink)
    }
