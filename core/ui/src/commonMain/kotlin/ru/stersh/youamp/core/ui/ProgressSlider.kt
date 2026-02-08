@file:Suppress("SameParameterValue")

package ru.stersh.youamp.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun ProgressSlider(
    value: Float,
    onValueChange: (value: Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onValueChangeFinished: (() -> Unit)? = null,
) {
    val density = LocalDensity.current

    val height = with(density) { 20.dp.roundToPx() }
    var width by remember { mutableIntStateOf(0) }

    var valueState by remember(
        value,
        valueRange,
    ) {
        mutableFloatStateOf(value)
    }

    val state =
        rememberDraggableState { delta ->
            val currentThumbX =
                valueToXPositionPx(
                    valueState,
                    valueRange,
                    width.toFloat(),
                )
            val newThumbX = currentThumbX + delta
            valueState =
                calculateNewValue(
                    valueState,
                    valueRange,
                    currentThumbX,
                    newThumbX,
                )
            onValueChange(valueState)
        }

    val draggable =
        Modifier.draggable(
            state = state,
            orientation = Orientation.Horizontal,
            enabled = enabled,
            interactionSource = interactionSource,
            onDragStopped = { onValueChangeFinished?.invoke() },
        )
    val clicks =
        Modifier.pointerInput(Unit) {
            detectTapGestures { offset ->
                val currentThumbX =
                    valueToXPositionPx(
                        valueState,
                        valueRange,
                        width.toFloat(),
                    )
                val newThumbX = offset.x
                valueState =
                    calculateNewValue(
                        valueState,
                        valueRange,
                        currentThumbX,
                        newThumbX,
                    )
                onValueChange(valueState)
                onValueChangeFinished?.invoke()
            }
        }
    Layout(
        content = {
            Track(
                value = valueState,
                valueRange = valueRange,
                modifier = clicks,
            )
            Thumb(
                modifier = draggable,
            )
        },
        modifier =
            modifier.onGloballyPositioned {
                width = it.size.width
            },
    ) { measurables, constraints ->

        val trackMeasurable = measurables[0]
        val thumbMeasurable = measurables[1]

        val newConstraints =
            constraints.copy(
                maxHeight = height,
                minHeight = height,
            )

        val thumbPlaceable = thumbMeasurable.measure(newConstraints)
        val trackPlaceable = trackMeasurable.measure(newConstraints)

        val thumbX =
            valueToXPositionPx(
                valueState,
                valueRange,
                newConstraints.maxWidth.toFloat(),
            ) - thumbPlaceable.width / 2

        layout(
            newConstraints.maxWidth,
            newConstraints.maxHeight,
        ) {
            trackPlaceable.placeRelative(
                x = 0,
                y = 0,
            )
            if (!thumbX.isNaN()) {
                thumbPlaceable.placeRelative(
                    x = thumbX.roundToInt(),
                    y = newConstraints.maxHeight / 2 - thumbPlaceable.height / 2,
                )
            }
        }
    }
}

private fun calculateNewValue(
    currentValue: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    currentThumbX: Float,
    newThumbX: Float,
): Float =
    if (newThumbX > 0) {
        val newValue = currentValue / currentThumbX * newThumbX
        valueRange.normalize(newValue)
    } else {
        currentValue
    }

private fun ClosedFloatingPointRange<Float>.normalize(value: Float): Float =
    when {
        value < this.start -> this.start
        value > this.endInclusive -> this.endInclusive
        else -> value
    }

@Composable
private fun Track(
    value: Float,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
) {
    val trackInactiveColor = MaterialTheme.colorScheme.secondaryContainer
    val trackActiveColor = MaterialTheme.colorScheme.primary
    val progressWidth = with(LocalDensity.current) { 4.dp.toPx() }

    Canvas(
        modifier =
            modifier
                .height(20.dp)
                .fillMaxWidth(),
    ) {
        val thumbX =
            valueToXPositionPx(
                value,
                valueRange,
                size.width,
            )
        drawLine(
            color = trackInactiveColor,
            strokeWidth = progressWidth,
            cap = StrokeCap.Round,
            start =
                Offset(
                    0f,
                    center.y,
                ),
            end =
                Offset(
                    size.width,
                    center.y,
                ),
        )
        drawLine(
            color = trackActiveColor,
            strokeWidth = progressWidth,
            cap = StrokeCap.Round,
            start =
                Offset(
                    0f,
                    center.y,
                ),
            end =
                Offset(
                    thumbX,
                    center.y,
                ),
        )
    }
}

private fun valueToXPositionPx(
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    trackWidth: Float,
): Float {
    val raw = (value - valueRange.start) / (valueRange.endInclusive - valueRange.start)
    return raw.coerceIn(
        0f,
        1f,
    ) * trackWidth
}

@Composable
private fun Thumb(modifier: Modifier = Modifier) {
    Box(
        modifier =
            modifier
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape,
                ).size(20.dp),
    )
}

@Composable
@Preview
private fun ProgressSliderPreview() {
    YouampPlayerTheme {
        Surface {
            ProgressSlider(
                value = 0.2f,
                onValueChange = {},
            )
        }
    }
}
