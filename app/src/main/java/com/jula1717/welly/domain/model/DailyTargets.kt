package com.jula1717.welly.domain.model

data class DailyTargets(
    val bmrKcal: Int,
    val tdeeKcal: Int,
    val calorieTargetKcalMin: Int,
    val calorieTargetKcalMax: Int,
    val proteinGramsMin: Int,
    val proteinGramsMax: Int,
    val carbsGramsMin: Int,
    val carbsGramsMax: Int,
    val fatGramsMin: Int,
    val fatGramsMax: Int,
    val fiberGramsMin: Int,
    val fiberGramsMax: Int,
    val waterMlMin: Int,
    val waterMlMax: Int,
)
