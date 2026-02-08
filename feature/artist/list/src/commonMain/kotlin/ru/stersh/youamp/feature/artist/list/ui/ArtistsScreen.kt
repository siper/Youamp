package ru.stersh.youamp.feature.artist.list.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import ru.stersh.youamp.core.ui.ArtistItem
import ru.stersh.youamp.core.ui.ArtistItemDefaults
import ru.stersh.youamp.core.ui.ArtistSkeleton
import ru.stersh.youamp.core.ui.BackNavigationButton
import ru.stersh.youamp.core.ui.EmptyLayout
import ru.stersh.youamp.core.ui.ErrorLayout
import ru.stersh.youamp.core.ui.PullToRefresh
import ru.stersh.youamp.core.ui.SkeletonLayout
import ru.stersh.youamp.core.ui.YouampPlayerTheme
import ru.stersh.youamp.core.ui.isCompactWidth
import youamp.feature.artist.list.generated.resources.Res
import youamp.feature.artist.list.generated.resources.artists_title

@Composable
fun ArtistsScreen(
    onArtistClick: (id: String) -> Unit,
    onBackClick: () -> Unit,
) {
    val viewModel: ArtistsViewModel = koinViewModel()

    val state by viewModel.state.collectAsStateWithLifecycle()

    ArtistsScreen(
        state = state,
        onRetry = viewModel::retry,
        onRefresh = viewModel::refresh,
        onArtistClick = onArtistClick,
        onBackClick = onBackClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ArtistsScreen(
    state: ArtistsStateUi,
    onRetry: () -> Unit,
    onRefresh: () -> Unit,
    onArtistClick: (id: String) -> Unit,
    onBackClick: () -> Unit,
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
                scrollBehavior = scrollBehavior,
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) {
        PullToRefresh(
            isRefreshing = state.isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier.padding(it),
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
                        modifier =
                            Modifier.verticalScroll(
                                state = rememberScrollState(),
                            ),
                    )
                }

                else -> {
                    Content(
                        items = state.items,
                        onArtistClick = onArtistClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun Content(
    items: ImmutableList<ArtistUi>,
    onArtistClick: (id: String) -> Unit,
) {
    LazyVerticalGrid(
        columns =
            if (isCompactWidth) {
                GridCells.Fixed(2)
            } else {
                GridCells.Adaptive(ArtistItemDefaults.Width)
            },
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        items(
            items = items,
        ) { artist ->
            ArtistItem(
                name = artist.name,
                artworkUrl = artist.artworkUrl,
                onClick = {
                    onArtistClick.invoke(artist.id)
                },
            )
        }
    }
}

@Composable
private fun Progress() {
    SkeletonLayout {
        LazyVerticalGrid(
            columns =
                if (isCompactWidth) {
                    GridCells.Fixed(2)
                } else {
                    GridCells.Adaptive(ArtistItemDefaults.Width)
                },
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            userScrollEnabled = false,
        ) {
            items(
                items = (0..12).toList(),
            ) {
                ArtistSkeleton()
            }
        }
    }
}

@Composable
@Preview
private fun ArtistsScreenProgressPreview() {
    YouampPlayerTheme {
        ArtistsScreen(
            state = ArtistsStateUi(),
            onRetry = {},
            onRefresh = {},
            onArtistClick = {},
            onBackClick = {},
        )
    }
}

@Composable
@Preview
private fun ArtistsScreenPreview() {
    YouampPlayerTheme {
        val items =
            persistentListOf(
                ArtistUi(
                    id = "1",
                    name = "Test",
                    artworkUrl = null,
                ),
                ArtistUi(
                    id = "2",
                    name = "Test 2",
                    artworkUrl = null,
                ),
                ArtistUi(
                    id = "3",
                    name = "Test 3",
                    artworkUrl = null,
                ),
                ArtistUi(
                    id = "4",
                    name = "Test 4",
                    artworkUrl = null,
                ),
                ArtistUi(
                    id = "5",
                    name = "Test 5",
                    artworkUrl = null,
                ),
            )
        val state =
            ArtistsStateUi(
                progress = false,
                isRefreshing = true,
                error = false,
                items = items,
            )
        ArtistsScreen(
            state = state,
            onRetry = {},
            onRefresh = {},
            onArtistClick = {},
            onBackClick = {},
        )
    }
}
