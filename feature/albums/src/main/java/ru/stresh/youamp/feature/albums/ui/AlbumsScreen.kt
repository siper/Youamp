package ru.stresh.youamp.feature.albums.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.SubcomposeAsyncImage


@Composable
fun AlbumsScreen(onAlbumClick: (id: String) -> Unit) {
    AlbumsScreen(viewModel(), onAlbumClick)
}

@Composable
private fun AlbumsScreen(
    viewModel: AlbumsViewModel,
    onAlbumClick: (id: String) -> Unit
) {
    val albums: List<AlbumUi> by viewModel.albums.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val pullRefreshState = rememberPullToRefreshState(enabled = { !isRefreshing })
    val listState = rememberLazyGridState()
    if (pullRefreshState.isRefreshing) {
        viewModel.refresh()
    }
    Box(
        modifier = Modifier.nestedScroll(pullRefreshState.nestedScrollConnection)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            state = listState,
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        )
        {
            items(albums) { album ->
                AlbumItem(
                    album = album,
                    onAlbumClick = onAlbumClick
                )
            }
        }
        PullToRefreshContainer(
            modifier = Modifier.align(Alignment.TopCenter),
            state = pullRefreshState,
        )
    }

    listState.OnBottomReached {
        viewModel.loadMore()
    }
}

@Composable
private fun LazyGridState.OnBottomReached(
    loadMore: () -> Unit
) {
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf true

            lastVisibleItem.index == layoutInfo.totalItemsCount - 1
        }
    }

    LaunchedEffect(shouldLoadMore) {
        snapshotFlow { shouldLoadMore.value }
            .collect { loadMore ->
                if (loadMore) {
                    loadMore()
                }
            }
    }
}

@Composable
private fun AlbumItem(
    album: AlbumUi,
    onAlbumClick: (id: String) -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .clickable {
                onAlbumClick(album.id)
            },
    ) {
        SubcomposeAsyncImage(
            model = album.artworkUrl,
            contentDescription = "Album image",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            loading = {
                Image(
                    imageVector = Icons.Rounded.Album,
                    contentDescription = "placeholder"
                )
            }
        )
        Text(
            text = album.title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 12.dp, end = 12.dp),
            textAlign = TextAlign.Left,
            minLines = 1,
            maxLines = 1,
            style = MaterialTheme.typography.labelLarge
        )
        Text(
            text = album.artist,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp, start = 12.dp, end = 12.dp, bottom = 8.dp),
            textAlign = TextAlign.Left,
            minLines = 1,
            maxLines = 1,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}