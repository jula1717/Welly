package com.jula1717.welly.data.local.mapper

import com.jula1717.welly.data.local.entity.ActivityEntity
import com.jula1717.welly.domain.model.Activity

fun ActivityEntity.toDomain(): Activity =
    Activity(
        id = id,
        name = name,
        type = type,
    )

fun Activity.toEntity(): ActivityEntity =
    ActivityEntity(
        id = id,
        name = name,
        type = type,
    )
