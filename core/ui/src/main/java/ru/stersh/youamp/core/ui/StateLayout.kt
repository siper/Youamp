package ru.stersh.youamp.core.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier

@Stable
enum class LayoutStateUi { Content, Progress, Error, Empty }

@Composable
fun StateLayout(
    state: LayoutStateUi,
    content: @Composable () -> Unit = {},
    progress: @Composable () -> Unit = {},
    error: @Composable () -> Unit = {},
    empty: @Composable () -> Unit = {},
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        targetState = state,
        modifier = modifier,
        transitionSpec = {
            fadeIn(animationSpec = tween(220))
                .togetherWith(fadeOut(animationSpec = tween(220)))
        },
        label = "animated_state"
    ) {
        when (it) {
            LayoutStateUi.Content -> content()
            LayoutStateUi.Progress -> progress()
            LayoutStateUi.Error -> error()
            LayoutStateUi.Empty -> empty()
        }
    }
}