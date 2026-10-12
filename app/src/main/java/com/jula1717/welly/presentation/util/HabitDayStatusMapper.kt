package com.jula1717.welly.presentation.util

import androidx.annotation.StringRes
import com.jula1717.welly.R
import com.jula1717.welly.domain.model.HabitDayStatus

@get:StringRes
internal val HabitDayStatus.descriptionResId: Int
    get() = when (this) {
        HabitDayStatus.Done -> R.string.habit_cd_done
        HabitDayStatus.Covered -> R.string.habit_cd_covered
        HabitDayStatus.None -> R.string.habit_cd_not_done
    }
