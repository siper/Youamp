package ru.stersh.youamp.core.ui

import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SongPlayAnimation(
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
    barColor: Color = MaterialTheme.colorScheme.primary,
) {
    if (isPlaying) {
        AnimatingBars(
            barColor = barColor,
            modifier = modifier,
        )
    } else {
        StaticBars(
            barColor = barColor,
            modifier = modifier,
        )
    }
}

@Composable
private fun StaticBars(
    barColor: Color,
    modifier: Modifier,
) {
    Row(
        modifier = modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Bar(
            fraction = 0.2f,
            color = barColor,
        )
        Bar(
            fraction = 0.2f,
            color = barColor,
        )
        Bar(
            fraction = 0.2f,
            color = barColor,
        )
    }
}

@Composable
private fun AnimatingBars(
    barColor: Color,
    modifier: Modifier = Modifier,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "infinite_play_animation")
    val firstBar by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec =
            infiniteRepeatable(
                animation =
                    tween(
                        durationMillis = 500,
                        easing = EaseOut,
                    ),
                repeatMode = RepeatMode.Reverse,
            ),
        label = "first_bar",
    )
    val secondBar by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec =
            infiniteRepeatable(
                animation =
                    tween(
                        durationMillis = 700,
                        easing = EaseOut,
                    ),
                repeatMode = RepeatMode.Reverse,
            ),
        label = "second_bar",
    )
    val thirdBar by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.7f,
        animationSpec =
            infiniteRepeatable(
                animation =
                    tween(
                        durationMillis = 600,
                        easing = EaseOut,
                    ),
                repeatMode = RepeatMode.Reverse,
            ),
        label = "third_bar",
    )
    Row(
        modifier = modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Bar(
            fraction = firstBar,
            color = barColor,
        )
        Bar(
            fraction = secondBar,
            color = barColor,
        )
        Bar(
            fraction = thirdBar,
            color = barColor,
        )
    }
}

@Composable
private fun RowScope.Bar(
    fraction: Float,
    color: Color,
) {
    Box(
        modifier =
            Modifier
                .fillMaxHeight(fraction = fraction)
                .weight(1f)
                .background(
                    color = color,
                    shape = CircleShape,
                ),
    )
}
