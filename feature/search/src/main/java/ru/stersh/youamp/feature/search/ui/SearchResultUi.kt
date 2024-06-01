package ru.stersh.youamp.feature.search.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.stersh.youamp.core.ui.Artwork
import ru.stersh.youamp.core.ui.YouAmpPlayerTheme
import ru.stersh.youamp.feature.search.R

@Composable
internal fun SongItem(
    item: SearchResultUi.Song,
    onPlay: (songId: String) -> Unit,
    onAddToQueue: (songId: String) -> Unit,
) {
    var menuExpanded by rememberSaveable { mutableStateOf(false) }
    ListItem(
        leadingContent = {
            Artwork(
                artworkUrl = item.artworkUrl,
                placeholder = Icons.Rounded.MusicNote,
                modifier = Modifier.size(56.dp)
            )
        },
        headlineContent = {
            Text(text = item.title)
        },
        supportingContent = {
            Text(text = item.artist)
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        trailingContent = {
            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(
                        imageVector = Icons.Rounded.MoreVert,
                        contentDescription = stringResource(R.string.more_action_description)
                    )
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = R.string.play_title)) },
                        onClick = {
                            onPlay(item.id)
                            menuExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = R.string.add_to_queue_title)) },
                        onClick = {
                            onAddToQueue(item.id)
                            menuExpanded = false
                        }
                    )
                }
            }
        },
        modifier = Modifier.clickable { menuExpanded = true }
    )
}

@Composable
internal fun AlbumItem(
    item: SearchResultUi.Album,
    onPlay: (albumId: String) -> Unit,
    onAddToQueue: (albumId: String) -> Unit,
    onOpenInfo: (albumId: String) -> Unit,
) {
    var menuExpanded by rememberSaveable { mutableStateOf(false) }
    ListItem(
        leadingContent = {
            Artwork(
                artworkUrl = item.artworkUrl,
                placeholder = Icons.Rounded.Album,
                modifier = Modifier.size(56.dp)
            )
        },
        headlineContent = {
            Text(text = item.title)
        },
        supportingContent = {
            Text(text = item.artist)
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        trailingContent = {
            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(
                        imageVector = Icons.Rounded.MoreVert,
                        contentDescription = stringResource(R.string.more_action_description)
                    )
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = R.string.play_title)) },
                        onClick = {
                            onPlay(item.id)
                            menuExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = R.string.add_to_queue_title)) },
                        onClick = {
                            onAddToQueue(item.id)
                            menuExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.album_info_title)) },
                        onClick = {
                            onOpenInfo(item.id)
                            menuExpanded = false
                        }
                    )
                }
            }
        },
        modifier = Modifier.clickable { menuExpanded = true }
    )
}

@Composable
internal fun ArtistItem(
    item: SearchResultUi.Artist,
    onPlay: (artistId: String) -> Unit,
    onAddToQueue: (artistId: String) -> Unit,
    onOpenInfo: (artistId: String) -> Unit,
) {
    var menuExpanded by rememberSaveable { mutableStateOf(false) }
    ListItem(
        leadingContent = {
            Artwork(
                artworkUrl = item.artworkUrl,
                placeholder = Icons.Rounded.Person,
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
            )
        },
        headlineContent = {
            Text(text = item.name)
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        trailingContent = {
            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(
                        imageVector = Icons.Rounded.MoreVert,
                        contentDescription = stringResource(R.string.more_action_description)
                    )
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.play_title)) },
                        onClick = {
                            onPlay(item.id)
                            menuExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.add_to_queue_title)) },
                        onClick = {
                            onAddToQueue(item.id)
                            menuExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.artist_info_title)) },
                        onClick = {
                            onOpenInfo(item.id)
                            menuExpanded = false
                        }
                    )
                }
            }
        },
        modifier = Modifier.clickable { menuExpanded = true }
    )
}

@Immutable
internal object SearchResultUi {

    @Immutable
    data class Song(
        val id: String,
        val title: String,
        val artist: String,
        val artworkUrl: String?
    )

    @Immutable
    data class Album(
        val id: String,
        val title: String,
        val artist: String,
        val artworkUrl: String?
    )

    @Immutable
    data class Artist(
        val id: String,
        val name: String,
        val artworkUrl: String?
    )
}

@Composable
@Preview
private fun Preview() {
    YouAmpPlayerTheme {
        Scaffold {
            Column(modifier = Modifier.padding(it)) {
                SongItem(
                    item = SearchResultUi.Song(
                        id = "1",
                        title = "Coolest song in the world",
                        artist = "Coolest artist in the world",
                        artworkUrl = null
                    ),
                    onPlay = {},
                    onAddToQueue = {},
                )
                AlbumItem(
                    item = SearchResultUi.Album(
                        id = "1",
                        title = "Coolest album in the world",
                        artist = "Coolest artist in the world",
                        artworkUrl = null
                    ),
                    onPlay = {},
                    onAddToQueue = {},
                    onOpenInfo = {}
                )
                ArtistItem(
                    item = SearchResultUi.Artist(
                        id = "1",
                        name = "Coolest artist in the world",
                        artworkUrl = null
                    ),
                    onPlay = {},
                    onAddToQueue = {},
                    onOpenInfo = {},
                )
            }
        }
    }
}