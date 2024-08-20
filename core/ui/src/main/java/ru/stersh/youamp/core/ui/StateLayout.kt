package ru.stersh.youamp.core.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier

@Stable
enum class LayoutStateUi { Content, Progress, Error, Empty }

@Composable
fun StateLayout(
    state: LayoutStateUi,
    content: @Composable () -> Unit,
    progress: @Composable () -> Unit,
    error: @Composable () -> Unit,
    empty: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        targetState = state,
        modifier = modifier,
        label = "animated_state"
    ) {
        when(it) {
            LayoutStateUi.Content -> content()
            LayoutStateUi.Progress -> progress()
            LayoutStateUi.Error -> error()
            LayoutStateUi.Empty -> empty()
        }
    }
}