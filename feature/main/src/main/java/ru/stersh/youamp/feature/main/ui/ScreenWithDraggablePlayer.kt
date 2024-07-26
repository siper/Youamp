package ru.stersh.youamp.feature.main.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.stersh.youamp.core.ui.YouampPlayerTheme


@Composable
fun ScreenWithDraggablePlayer(
    modifier: Modifier = Modifier,
    collapsedHeight: Dp = 80.dp,
    content: @Composable () -> Unit = {},
    toolbar: @Composable () -> Unit = {},
    expandedContent: @Composable () -> Unit = {},
    collapsedContent: @Composable () -> Unit = {},
    bottomNavigation: @Composable () -> Unit = {},
) {
    val density = LocalDensity.current

    var bottomNavigationHeightMax by remember {
        mutableStateOf(0.dp)
    }
    var toolbarHeightMax by remember {
        mutableStateOf(0.dp)
    }
    var maxHeight by remember {
        mutableStateOf(0.dp)
    }

    val scope = rememberCoroutineScope()

    val toolbarHeight = remember(toolbarHeightMax) {
        Animatable(toolbarHeightMax.toPx(density))
    }
    val bottomNavigationHeight = remember(bottomNavigationHeightMax) {
        Animatable(bottomNavigationHeightMax.toPx(density))
    }

    val playerHeight = remember(collapsedHeight, bottomNavigationHeightMax) {
        Animatable(collapsedHeight.toPx(density) + bottomNavigationHeightMax.toPx(density))
    }

    val globalModifier = if (maxHeight == 0.dp) {
        Modifier.onGloballyPositioned {
            maxHeight = with(density) { it.size.height.toDp() }
        }
    } else {
        Modifier
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .then(globalModifier)
    ) {
        Column {
            val toolbarModifier = if (toolbarHeightMax == 0.dp) {
                Modifier.onGloballyPositioned {
                    if (toolbarHeightMax == 0.dp) {
                        toolbarHeightMax = with(density) { it.size.height.toDp() }
                    }
                }
            } else {
                Modifier.height(toolbarHeight.value.toDp(density))
            }
            Box(
                modifier = Modifier
                    .then(toolbarModifier)

            ) {
                toolbar()
            }
            content()
        }

        var offsetPosition by remember { mutableFloatStateOf(0f) }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(playerHeight.value.toDp(density))
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .align(Alignment.BottomEnd)
                .draggable(
                    onDragStopped = {
                        if (offsetPosition < maxHeight.toPx(density) * 0.5f) {
                            scope.launch {
                                playerHeight.animateTo(collapsedHeight.toPx(density) + bottomNavigationHeightMax.value)
                            }
                            scope.launch {
                                toolbarHeight.animateTo(toolbarHeightMax.toPx(density))
                            }
                            scope.launch {
                                bottomNavigationHeight.animateTo(bottomNavigationHeightMax.toPx(density))
                            }
                        } else {
                            scope.launch {
                                playerHeight.animateTo(maxHeight.toPx(density))
                            }
                            scope.launch {
                                toolbarHeight.animateTo(0f)
                            }
                            scope.launch {
                                bottomNavigationHeight.animateTo(0f)
                            }
                        }
                    },
                    orientation = Orientation.Vertical,
                    state = rememberDraggableState { delta ->
                        val newValue = offsetPosition - delta
                        offsetPosition = newValue.coerceIn(
                            bottomNavigationHeightMax.toPx(density) + collapsedHeight.toPx(density),
                            maxHeight.toPx(density)
                        )
                        val percent = offsetPosition / maxHeight.toPx(density)
                        scope.launch {
                            playerHeight.animateTo(
                                kotlin.math.max(
                                    maxHeight.toPx(density) * percent,
                                    collapsedHeight.toPx(density) + bottomNavigationHeightMax.toPx(density)
                                )
                            )
                        }
                        scope.launch {
                            toolbarHeight.animateTo(toolbarHeightMax.toPx(density) - (toolbarHeightMax.toPx(density) * percent))
                        }
                        scope.launch {
                            bottomNavigationHeight.animateTo(
                                bottomNavigationHeightMax.toPx(density) - (bottomNavigationHeightMax.toPx(
                                    density
                                ) * percent)
                            )
                        }
                    }
                )
                .clickable {
//                    currentState = if (currentState == State.Expanded) {
//                        State.Collapsed
//                    } else {
//                        State.Expanded
//                    }
                }
        ) {
            collapsedContent()
        }
        val bottomNavigationModifier = if (bottomNavigationHeightMax == 0.dp) {
            Modifier.onGloballyPositioned {
                bottomNavigationHeightMax = with(density) { it.size.height.toDp() }
            }
        } else {
            Modifier.height(bottomNavigationHeight.value.toDp(density))
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .then(bottomNavigationModifier)
        ) {
            bottomNavigation()
        }
    }
}

fun Float.toDp(density: Density): Dp {
    return with(density) { toDp() }
}

fun Int.toDp(density: Density): Dp {
    return with(density) { toDp() }
}

fun Dp.toPx(density: Density): Float {
    return with(density) { toPx() }
}

fun Dp.roundToPx(density: Density): Int {
    return with(density) { roundToPx() }
}

@Composable
@Preview
fun ScreenWithDraggablePlayerPreview() {
    YouampPlayerTheme {
        ScreenWithDraggablePlayer(
            content = {},
            toolbar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(Color.Red)
                )
            },
            expandedContent = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Blue)
                )
            },
            collapsedContent = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(Color.Green)
                )
            },
            bottomNavigation = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(Color.Red)
                )
            }
        )
    }
}