package ru.stersh.youamp.feature.artist.list.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import ru.stersh.youamp.core.ui.BackNavigationButton
import ru.stersh.youamp.core.ui.CircleArtwork
import ru.stersh.youamp.core.ui.EmptyLayout
import ru.stersh.youamp.core.ui.ErrorLayout
import ru.stersh.youamp.core.ui.SkeletonLayout
import ru.stersh.youamp.core.ui.YouampPlayerTheme
import youamp.feature.artist.list.generated.resources.Res
import youamp.feature.artist.list.generated.resources.artists_title


@Composable
fun ArtistsScreen(
    onArtistClick: (id: String) -> Unit,
    onBackClick: () -> Unit
) {
    val viewModel: ArtistsViewModel = koinViewModel()

    val state by viewModel.state.collectAsStateWithLifecycle()

    ArtistsScreen(
        state = state,
        onRetry = viewModel::retry,
        onRefresh = viewModel::refresh,
        onArtistClick = onArtistClick,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ArtistsScreen(
    state: ArtistsStateUi,
    onRetry: () -> Unit,
    onRefresh: () -> Unit,
    onArtistClick: (id: String) -> Unit,
    onBackClick: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        topBar = {
            LargeTopAppBar(
                navigationIcon = {
                    BackNavigationButton(onClick = onBackClick)
                },
                title = {
                    Text(text = stringResource(Res.string.artists_title))
                },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier.padding(it)
        ) {
            when {
                state.progress -> {
                    Progress()
                }

                state.error -> {
                    ErrorLayout(onRetry = onRetry)
                }

                state.items.isEmpty() -> {
                    EmptyLayout(
                        modifier = Modifier.verticalScroll(
                            state = rememberScrollState()
                        )
                    )
                }

                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            items = state.items
                        ) { album ->
                            ArtistItem(
                                artist = album,
                                onAlbumClick = onArtistClick
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Progress() {
    SkeletonLayout {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = (0..12).toList()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SkeletonItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        shape = CircleShape
                    )
                    SkeletonItem(
                        modifier = Modifier.size(width = 80.dp, height = 24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ArtistItem(
    artist: ArtistUi,
    onAlbumClick: (id: String) -> Unit
) {
    val selectableShape = remember { RoundedCornerShape(36.dp) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(selectableShape)
            .clickable {
                onAlbumClick(artist.id)
            },
    ) {
        CircleArtwork(
            artworkUrl = artist.artworkUrl,
            placeholder = Icons.Rounded.Person,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = artist.name,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 12.dp, end = 12.dp),
            textAlign = TextAlign.Center,
            minLines = 2,
            maxLines = 2,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
@Preview
private fun ArtistsScreenPreview() {
    YouampPlayerTheme {
        val items = persistentListOf(
            ArtistUi(
                id = "1",
                name = "Test",
                artworkUrl = null
            ),
            ArtistUi(
                id = "2",
                name = "Test 2",
                artworkUrl = null
            ),
            ArtistUi(
                id = "3",
                name = "Test 3",
                artworkUrl = null
            ),
            ArtistUi(
                id = "4",
                name = "Test 4",
                artworkUrl = null
            ),
            ArtistUi(
                id = "5",
                name = "Test 5",
                artworkUrl = null
            )
        )
        val state = ArtistsStateUi(
            progress = false,
            isRefreshing = true,
            error = false,
            items = items
        )
        ArtistsScreen(
            state = state,
            onRetry = {},
            onRefresh = {},
            onArtistClick = {},
            onBackClick = {}
        )
    }
}