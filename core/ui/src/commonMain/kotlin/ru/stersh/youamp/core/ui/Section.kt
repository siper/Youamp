package ru.stersh.youamp.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Section(
    title: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable () -> Unit,
) {
    Column {
        Row(
            modifier =
                modifier.padding(
                    vertical = 24.dp,
                    horizontal = 24.dp,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Title(
                title = title,
                onClick = onClick,
            )
            Spacer(modifier = Modifier.weight(1f))
            Row {
                actions()
            }
        }
        content()
    }
}

@Composable
private fun Title(
    title: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    val clickable =
        if (onClick != null) {
            modifier.clickable(
                enabled = true,
                onClick = onClick,
            )
        } else {
            modifier
        }
    Box(
        modifier =
            Modifier
                .clip(MaterialTheme.shapes.medium)
                .then(clickable)
                .padding(
                    horizontal = 8.dp,
                    vertical = 4.dp,
                ),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
            )
            if (onClick != null) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.requiredSize(18.dp),
                )
            }
        }
    }
}

private val LazyListState.lastVisibleItemIndex: Int
    get() = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

private val LazyListState.lastVisibleItemOffset: Int
    get() = layoutInfo.visibleItemsInfo.lastOrNull()?.offset ?: 0

private val LazyListState.lastVisibleItemSize: Int
    get() = layoutInfo.visibleItemsInfo.lastOrNull()?.size ?: 0

private val LazyListState.lastItemIndex: Int
    get() = layoutInfo.totalItemsCount - 1

private const val SCROLL_ITEMS_COUNT = 3

@Composable
fun SectionScrollActions(
    listState: LazyListState,
    modifier: Modifier = Modifier,
) {
    val allItemsVisible by remember(listState) {
        derivedStateOf {
            listState.layoutInfo.visibleItemsInfo.size == listState.layoutInfo.totalItemsCount
        }
    }
    val scrollBackButtonEnabled by remember(listState) {
        derivedStateOf { listState.firstVisibleItemIndex != 0 }
    }
    val scrollForwardButtonEnabled by remember(listState) {
        derivedStateOf {
            listState.lastVisibleItemIndex != listState.lastItemIndex ||
                listState.lastVisibleItemOffset + listState.lastVisibleItemSize >
                listState.layoutInfo.viewportEndOffset
        }
    }
    val scope = rememberCoroutineScope()
    if (allItemsVisible) {
        return
    }
    Row(modifier = modifier) {
        FilledTonalIconButton(
            enabled = scrollBackButtonEnabled,
            onClick = {
                scope.launch {
                    val scrollToIndex =
                        (listState.firstVisibleItemIndex - SCROLL_ITEMS_COUNT).coerceAtLeast(0)
                    listState.animateScrollToItem(scrollToIndex)
                }
            },
        ) {
            Icon(
                imageVector = Icons.Rounded.ArrowBackIosNew,
                contentDescription = null,
            )
        }
        FilledTonalIconButton(
            enabled = scrollForwardButtonEnabled,
            onClick = {
                scope.launch {
                    val scrollToIndex =
                        (listState.firstVisibleItemIndex + SCROLL_ITEMS_COUNT)
                            .coerceAtMost(listState.lastItemIndex)
                    listState.animateScrollToItem(scrollToIndex)
                }
            },
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                contentDescription = null,
            )
        }
    }
}

@Composable
@Preview
private fun SectionTitlePreview() {
    YouampPlayerTheme {
        Column(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Section(
                title = "Test with actions",
                actions = {
                    FilledTonalIconButton(
                        onClick = {},
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBackIosNew,
                            contentDescription = null,
                        )
                    }
                    FilledTonalIconButton(
                        onClick = {},
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                            contentDescription = null,
                        )
                    }
                },
            ) {
            }
        }
    }
}
