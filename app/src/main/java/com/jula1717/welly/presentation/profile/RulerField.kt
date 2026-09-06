package com.jula1717.welly.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.jula1717.welly.R
import com.jula1717.welly.ui.theme.WellyTheme
import kotlin.math.roundToInt

private val FIELD_SHAPE = RoundedCornerShape(18.dp)

private val DRAG_PX_PER_UNIT = 5.dp

private const val RULER_TICK_SPAN = 14
private const val RULER_TALL_TICK_EVERY = 5
private val RULER_TICK_AREA_HEIGHT = 22.dp
private val RULER_SHORT_TICK_HEIGHT = 8.dp
private val RULER_TALL_TICK_HEIGHT = 16.dp
private val RULER_POINTER_HEIGHT = 26.dp

/**
 * A single integer inside [range], picked by swiping a horizontal ruler left/right. Shows the
 * current [value] with its [unit] above the ticks; [label] names the control for accessibility
 * services. Knows nothing about what it measures — height, reps, anything integer works.
 */
@Composable
internal fun RulerField(
    label: String,
    value: Int,
    unit: String,
    range: IntRange,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(FIELD_SHAPE)
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .border(1.5.dp, MaterialTheme.colorScheme.outlineVariant, FIELD_SHAPE)
            .padding(horizontal = 16.dp, vertical = 10.dp),
    ) {
        Row(
            // Purely decorative: the interactive ruler below carries the accessible value.
            modifier = Modifier
                .fillMaxWidth()
                .clearAndSetSemantics {},
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(
                text = value.toString(),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = " $unit",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 2.dp),
            )
        }
        RulerTicks(
            label = label,
            value = value,
            unit = unit,
            range = range,
            onValueChange = onValueChange,
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}

/** Value a horizontal ruler drag lands on: dragging right (positive travel) counts down. */
internal fun rulerStepTarget(
    startValue: Int,
    accumulatedPx: Float,
    stepPx: Float,
    range: IntRange,
): Int {
    val steps = (accumulatedPx / stepPx).toInt()
    return (startValue - steps).coerceIn(range.first, range.last)
}

@Composable
private fun RulerTicks(
    label: String,
    value: Int,
    unit: String,
    range: IntRange,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val minLabel = stringResource(R.string.cd_wheel_min)
    val maxLabel = stringResource(R.string.cd_wheel_max)
    val stateText =
        buildString {
            append(value).append(' ').append(unit)
            if (value <= range.first) append(", ").append(minLabel)
            if (value >= range.last) append(", ").append(maxLabel)
        }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(RULER_TICK_AREA_HEIGHT)
            .semantics(mergeDescendants = true) {
                contentDescription = label
                stateDescription = stateText
                progressBarRangeInfo =
                    ProgressBarRangeInfo(
                        current = value.toFloat(),
                        range = range.first.toFloat()..range.last.toFloat(),
                        steps = (range.last - range.first - 1).coerceAtLeast(0),
                    )
                setProgress { target ->
                    val next = target.roundToInt().coerceIn(range.first, range.last)
                    if (next != value) {
                        onValueChange(next)
                        true
                    } else {
                        false
                    }
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        TickRow(
            value = value,
            range = range,
            modifier = Modifier
                .fillMaxSize()
                .rulerDrag(value, range, onValueChange)
                .clearAndSetSemantics {},
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .width(3.dp)
                .height(RULER_POINTER_HEIGHT)
                .clip(RoundedCornerShape(2.dp))
                .background(MaterialTheme.colorScheme.secondary),
        )
    }
}

@Composable
private fun TickRow(
    value: Int,
    range: IntRange,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween) {
        for (offset in -RULER_TICK_SPAN..RULER_TICK_SPAN) {
            val tickValue = value + offset
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.BottomCenter) {
                if (tickValue in range) {
                    val tall = tickValue % RULER_TALL_TICK_EVERY == 0
                    Box(
                        modifier = Modifier
                            .width(2.dp)
                            .height(if (tall) RULER_TALL_TICK_HEIGHT else RULER_SHORT_TICK_HEIGHT)
                            .background(MaterialTheme.colorScheme.outlineVariant),
                    )
                }
            }
        }
    }
}

private class RulerDragState {
    var startValue = 0
    var accumulatedPx = 0f
}

/**
 * Horizontal drag stepping [onValueChange] by one unit for every [DRAG_PX_PER_UNIT] of travel.
 * Horizontal, so it never fights the page's vertical scroll. The gesture snapshots its start
 * value once and derives every target from that snapshot plus total travel, so it cannot drift.
 */
@Composable
private fun Modifier.rulerDrag(
    value: Int,
    range: IntRange,
    onValueChange: (Int) -> Unit,
): Modifier {
    val stepPx = with(LocalDensity.current) { DRAG_PX_PER_UNIT.toPx() }
    val latestValue = rememberUpdatedState(value)
    val latestRange = rememberUpdatedState(range)
    val latestOnValueChange = rememberUpdatedState(onValueChange)
    val drag = remember { RulerDragState() }

    return pointerInput(Unit) {
        detectHorizontalDragGestures(
            onDragStart = {
                drag.startValue = latestValue.value
                drag.accumulatedPx = 0f
            },
            onHorizontalDrag = { change, dragAmount ->
                change.consume()
                drag.accumulatedPx += dragAmount
                val target = rulerStepTarget(drag.startValue, drag.accumulatedPx, stepPx, latestRange.value)
                if (target != latestValue.value) latestOnValueChange.value(target)
            },
        )
    }
}

@PreviewLightDark
@Composable
private fun RulerFieldPreview() {
    WellyTheme {
        RulerField(
            label = "Height",
            value = 178,
            unit = "cm",
            range = 130..220,
            onValueChange = {},
            modifier = Modifier.padding(20.dp),
        )
    }
}
