package ru.stersh.youamp.feature.artist.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.stersh.youamp.core.ui.AlbumItem
import ru.stersh.youamp.core.ui.AlbumUi
import ru.stersh.youamp.core.ui.Artwork
import ru.stersh.youamp.core.ui.BackNavigationButton
import ru.stersh.youamp.core.ui.PlayAllButton
import ru.stersh.youamp.core.ui.PlayShuffledButton
import ru.stersh.youamp.core.ui.VerticalGrid


@Composable
fun ArtistInfoScreen(
    id: String,
    onAlbumClick: (albumId: String) -> Unit,
    onBackClick: () -> Unit
) {
    val viewModel = koinViewModel<ArtistInfoViewModel> {
        parametersOf(id)
    }
    val state by viewModel.state.collectAsStateWithLifecycle()
    ArtistInfoScreen(
        state = state,
        onPlayAll = viewModel::playAll,
        onPlayShuffled = viewModel::playShuffled,
        onAlbumClick = onAlbumClick,
        onBackClick = onBackClick
    )
}

@Composable
private fun ArtistInfoScreen(
    state: AlbumInfoStateUi,
    onPlayAll: () -> Unit,
    onPlayShuffled: () -> Unit,
    onAlbumClick: (albumId: String) -> Unit,
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    BackNavigationButton(onClick = onBackClick)
                }
            )
        }
    ) {
        val scrollState = rememberScrollState()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .verticalScroll(
                    state = scrollState
                )
                .fillMaxSize()
                .padding(it)
        ) {
            Artwork(
                artworkUrl = state.coverArtUrl,
                placeholder = Icons.Rounded.Person,
                shape = CircleShape,
                modifier = Modifier
                    .padding(horizontal = 48.dp)
                    .size(160.dp)
                    .aspectRatio(1f)
            )

            if (state.name != null) {
                Text(
                    text = state.name,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PlayAllButton(
                    onClick = onPlayAll,
                    modifier = Modifier.weight(0.5f)
                )
                PlayShuffledButton(
                    onClick = onPlayShuffled,
                    modifier = Modifier.weight(0.5f)
                )
            }

            VerticalGrid(
                horizontalSpacing = 8.dp,
                verticalSpacing = 8.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                state.albums.forEach { album ->
                    AlbumItem(
                        album = album,
                        onAlbumClick = onAlbumClick
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ArtistInfoScreenPreview() {
    val albums = listOf(
        AlbumUi(
            id = "1",
            title = "Test album",
            artist = null,
            artworkUrl = null
        ),
        AlbumUi(
            id = "2",
            title = "Test album 2",
            artist = null,
            artworkUrl = null
        ),
        AlbumUi(
            id = "3",
            title = "Test album 3",
            artist = null,
            artworkUrl = null
        )
    )
    MaterialTheme {
        ArtistInfoScreen(
            state = AlbumInfoStateUi(
                coverArtUrl = null,
                name = "Artist",
                progress = false,
                albums = albums
            ),
            onPlayAll = {},
            onAlbumClick = {},
            onPlayShuffled = {},
            onBackClick = {}
        )
    }
}