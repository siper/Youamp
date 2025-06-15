package ru.stersh.youamp.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.max

// Code from: https://engineering.dena.com/blog/2023/10/custom-layout-made-easy-with-jetpack-compose/ with some fixes
@Composable
fun VerticalGrid(
    modifier: Modifier = Modifier,
    column: Int = 2,
    horizontalSpacing: Dp = 0.dp,
    verticalSpacing: Dp = 0.dp,
    content: @Composable () -> Unit,
) {
    Layout(
        modifier = modifier,
        content = content,
    ) { measurables, constraints ->
        val horizontalSpacingPx = horizontalSpacing.roundToPx()
        val totalHorizontalSpace = (column - 1) * horizontalSpacingPx
        val itemWidth = (constraints.maxWidth - totalHorizontalSpace) / column
        val itemConstraints =
            constraints.copy(
                minWidth = itemWidth,
                maxWidth = itemWidth,
            )
        val placeables = measurables.map { it.measure(itemConstraints) }
        val width = constraints.maxWidth
        val gridHeights = mutableMapOf<Int, Int>()

        placeables.forEachIndexed { index, placeable ->
            val currentGrid = index / column
            val currentGridHeight = gridHeights[currentGrid] ?: 0
            if (placeable.height >= currentGridHeight) gridHeights[currentGrid] = placeable.height
        }

        val verticalSpacingPx = verticalSpacing.roundToPx()
        val totalVerticalSpace = (gridHeights.size - 1) * verticalSpacingPx
        val height =
            max(
                0,
                gridHeights.values.sum() + totalVerticalSpace,
            )

        layout(
            width,
            height,
        ) {
            var x = 0
            var y = 0
            placeables.forEachIndexed { index, placeable ->
                placeable.placeRelative(
                    x = x,
                    y = y,
                )
                val lastCellInRow = index % column == (column - 1)
                if (!lastCellInRow) {
                    x += itemWidth + horizontalSpacingPx
                } else {
                    x = 0
                    y += (gridHeights[index / column] ?: 0) + verticalSpacingPx
                }
            }
        }
    }
}

@Composable
@Preview
private fun VerticalGridPreview() {
    Column {
        VerticalGrid {
            repeat(10) {
                Box(
                    modifier =
                        Modifier.background(
                            color =
                                if (it % 2 == 0) {
                                    Color.Red
                                } else {
                                    Color.Blue
                                },
                        ),
                ) {
                    Text(text = "$it")
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        VerticalGrid {
            repeat(3) {
                Box(
                    modifier =
                        Modifier.background(
                            color =
                                if (it % 2 == 0) {
                                    Color.Red
                                } else {
                                    Color.Blue
                                },
                        ),
                ) {
                    Text(text = "$it")
                }
            }
        }
    }
}
