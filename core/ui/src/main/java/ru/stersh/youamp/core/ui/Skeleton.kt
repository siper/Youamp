package ru.stersh.youamp.core.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SkeletonLayout(
    modifier: Modifier = Modifier,
    content: @Composable SkeletonScope.() -> Unit
) {

    val infiniteTransition = rememberInfiniteTransition(label = "skeleton_animation")
    val alphaAnimation by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "skeleton_alpha_animation"
    )
    Column(modifier = modifier.graphicsLayer(alpha = alphaAnimation)) {
        content(SkeletonScope)
    }
}

object SkeletonScope {

    @Composable
    fun SkeletonItem(
        modifier: Modifier = Modifier,
        shape: Shape = remember {
            RoundedCornerShape(16.dp)
        },
        color: Color = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        Box(
            modifier = modifier.background(color = color, shape = shape)
        )
    }
}


@Composable
@Preview
private fun SkeletonItemPreview() {
    YouAmpPlayerTheme {
        Surface {
            SkeletonLayout {
                SkeletonItem(
                    modifier = Modifier
                        .size(width = 60.dp, height = 48.dp)
                )
                SkeletonItem(
                    modifier = Modifier
                        .size(width = 130.dp, height = 20.dp)
                )
            }
        }
    }
}