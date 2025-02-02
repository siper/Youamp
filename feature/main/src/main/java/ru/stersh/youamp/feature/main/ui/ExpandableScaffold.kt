package ru.stersh.youamp.feature.main.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.stersh.youamp.core.ui.YouampPlayerTheme
import ru.stersh.youamp.core.ui.toDp
import kotlin.math.max

class ExpandableScaffoldState {
    val expandCollapseAnimator = Animatable(COLLAPSED)

    var bottomBarMaxHeight by mutableFloatStateOf(Float.NaN)
    var topBarMaxHeight by mutableFloatStateOf(Float.NaN)
    var fullMaxHeight by mutableFloatStateOf(Float.NaN)
    var collapsedHeight by mutableFloatStateOf(Float.NaN)

    val playerMinHeight by derivedStateOf { bottomBarMaxHeight + collapsedHeight }
    val playerHeight by derivedStateOf {
        max(fullMaxHeight * expandCollapseAnimator.value, playerMinHeight)
    }

    val topBarOffset by derivedStateOf {
        topBarMaxHeight * expandCollapseAnimator.value
    }
    val bottomBarOffset by derivedStateOf {
        bottomBarMaxHeight * expandCollapseAnimator.value
    }

    val expandedCrossfade by derivedStateOf { expandCollapseAnimator.value.coerceIn(0f, 1f) }
    val collapsedCrossfade by derivedStateOf { 1f - expandedCrossfade }

    suspend fun expand(animate: Boolean = true) {
        if (animate) {
            expandCollapseAnimator.animateTo(EXPANDED)
        } else {
            expandCollapseAnimator.snapTo(EXPANDED)
        }
    }

    suspend fun collapse(animate: Boolean = true) {
        if (animate) {
            expandCollapseAnimator.animateTo(COLLAPSED)
        } else {
            expandCollapseAnimator.snapTo(COLLAPSED)
        }
    }

    companion object {
        private const val COLLAPSED = 0f
        private const val EXPANDED = 1f
    }
}


@Composable
fun rememberExpandableScaffoldState() = remember {
    ExpandableScaffoldState()
}

@Composable
fun ExpandableScaffold(
    modifier: Modifier = Modifier,
    state: ExpandableScaffoldState = rememberExpandableScaffoldState(),
    topBar: @Composable () -> Unit = {},
    expandedContent: @Composable () -> Unit = {},
    collapsedContent: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {

    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    val globalModifier = if (state.fullMaxHeight.isNaN()) {
        Modifier.onGloballyPositioned {
            state.fullMaxHeight = it.size.height.toFloat()
        }
    } else {
        Modifier
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .then(globalModifier)
    ) {
        Column(
            modifier = Modifier.padding(bottom = (state.collapsedHeight + state.bottomBarMaxHeight).toDp(density))
        ) {
            val topBarModifier = if (state.topBarMaxHeight.isNaN()) {
                Modifier.onGloballyPositioned {
                    state.topBarMaxHeight = it.size.height.toFloat()
                }
            } else {
                Modifier.offset(y = -state.topBarOffset.toDp(density))
            }
            Box(
                modifier = topBarModifier
            ) {
                topBar()
            }
            content()
            Spacer(modifier = Modifier.height(state.collapsedHeight.toDp(density)))
        }

        var offsetPosition by remember { mutableFloatStateOf(0f) }

        val heightModifier = if (state.collapsedHeight.isNaN()) {
            Modifier
        } else {
            Modifier.height(state.playerHeight.toDp(density))
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .then(heightModifier)
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .align(Alignment.BottomEnd)
                .draggable(
                    onDragStopped = {
                        if (offsetPosition < state.fullMaxHeight * 0.5f) {
                            offsetPosition = 0f
                            scope.launch {
                                state.collapse()
                            }
                        } else {
                            offsetPosition = state.fullMaxHeight
                            scope.launch {
                                state.expand()
                            }
                        }
                    },
                    orientation = Orientation.Vertical,
                    state = rememberDraggableState { delta ->
                        val newValue = offsetPosition - delta
                        offsetPosition = newValue.coerceIn(state.playerMinHeight, state.fullMaxHeight)
                        scope.launch {
                            state.expandCollapseAnimator.animateTo(offsetPosition / state.fullMaxHeight)
                        }
                    }
                )
        ) {
            Box(
                modifier = Modifier
                    .alpha(state.expandedCrossfade)
            ) {
                if (state.expandedCrossfade > 0) {
                    expandedContent()
                }
            }
            val collapsedMeasureModifier = Modifier.onGloballyPositioned {
                val measuredHeight = it.size.height.toFloat()
                if (state.collapsedHeight != measuredHeight) {
                    state.collapsedHeight = measuredHeight
                }
            }
            Box(
                modifier = Modifier
                    .alpha(state.collapsedCrossfade)
                    .then(collapsedMeasureModifier)
            ) {
                if (state.collapsedCrossfade > 0) {
                    collapsedContent()
                }
            }
        }
        val bottomBarModifier = if (state.bottomBarMaxHeight.isNaN()) {
            Modifier.onGloballyPositioned {
                state.bottomBarMaxHeight = it.size.height.toFloat()
            }
        } else {
            Modifier.offset(y = state.bottomBarOffset.toDp(density))
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .then(bottomBarModifier)
        ) {
            bottomBar()
        }
    }
}

@Composable
@Preview
fun ScreenWithDraggablePlayerPreview() {
    YouampPlayerTheme {
        ExpandableScaffold(
            content = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.DarkGray)
                )
            },
            topBar = {
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
                ) {
                    Text(
                        text = "Expanded content",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            },
            collapsedContent = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(Color.Green),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Collapsed content",
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            },
            bottomBar = {
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