package ru.stersh.youamp.feature.search.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.stersh.youamp.core.ui.Artwork
import ru.stersh.youamp.core.ui.CircleArtwork
import ru.stersh.youamp.core.ui.YouampPlayerTheme
import youamp.feature.search.generated.resources.Res
import youamp.feature.search.generated.resources.add_to_queue_title
import youamp.feature.search.generated.resources.album_info_title
import youamp.feature.search.generated.resources.artist_info_title
import youamp.feature.search.generated.resources.more_action_description
import youamp.feature.search.generated.resources.play_title

@Composable
internal fun SongItem(
    item: Song,
    onMoreClick: () -> Unit,
) {
    ListItem(
        leadingContent = {
            Artwork(
                artworkUrl = item.artworkUrl,
                placeholder = Icons.Rounded.MusicNote,
                modifier = Modifier.size(56.dp),
            )
        },
        headlineContent = {
            Text(
                text = item.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        supportingContent = {
            item.artist?.let {
                Text(
                    text = it,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        },
        colors =
            ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            ),
        trailingContent = {
            Box {
                IconButton(onClick = onMoreClick) {
                    Icon(
                        imageVector = Icons.Rounded.MoreVert,
                        contentDescription = stringResource(Res.string.more_action_description),
                    )
                }
            }
        },
        modifier = Modifier.clickable(onClick = onMoreClick),
    )
}

@Composable
internal fun AlbumItem(
    item: Album,
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
                modifier = Modifier.size(56.dp),
            )
        },
        headlineContent = {
            Text(
                text = item.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        supportingContent = {
            Text(
                text = item.artist,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        colors =
            ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            ),
        trailingContent = {
            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(
                        imageVector = Icons.Rounded.MoreVert,
                        contentDescription = stringResource(Res.string.more_action_description),
                    )
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    modifier = Modifier.align(Alignment.Center),
                ) {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(Res.string.play_title)) },
                        onClick = {
                            onPlay(item.id)
                            menuExpanded = false
                        },
                    )
                    DropdownMenuItem(
                        text = { Text(text = stringResource(Res.string.add_to_queue_title)) },
                        onClick = {
                            onAddToQueue(item.id)
                            menuExpanded = false
                        },
                    )
                    DropdownMenuItem(
                        text = { Text(text = stringResource(Res.string.album_info_title)) },
                        onClick = {
                            onOpenInfo(item.id)
                            menuExpanded = false
                        },
                    )
                }
            }
        },
        modifier = Modifier.clickable { menuExpanded = true },
    )
}

@Composable
internal fun ArtistItem(
    item: Artist,
    onPlay: (artistId: String) -> Unit,
    onAddToQueue: (artistId: String) -> Unit,
    onOpenInfo: (artistId: String) -> Unit,
) {
    var menuExpanded by rememberSaveable { mutableStateOf(false) }
    ListItem(
        leadingContent = {
            CircleArtwork(
                artworkUrl = item.artworkUrl,
                placeholder = Icons.Rounded.Person,
                modifier = Modifier.size(56.dp),
            )
        },
        headlineContent = {
            Text(
                text = item.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        colors =
            ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            ),
        trailingContent = {
            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(
                        imageVector = Icons.Rounded.MoreVert,
                        contentDescription = stringResource(Res.string.more_action_description),
                    )
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    modifier = Modifier.align(Alignment.Center),
                ) {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(Res.string.play_title)) },
                        onClick = {
                            onPlay(item.id)
                            menuExpanded = false
                        },
                    )
                    DropdownMenuItem(
                        text = { Text(text = stringResource(Res.string.add_to_queue_title)) },
                        onClick = {
                            onAddToQueue(item.id)
                            menuExpanded = false
                        },
                    )
                    DropdownMenuItem(
                        text = { Text(text = stringResource(Res.string.artist_info_title)) },
                        onClick = {
                            onOpenInfo(item.id)
                            menuExpanded = false
                        },
                    )
                }
            }
        },
        modifier = Modifier.clickable { menuExpanded = true },
    )
}

@Composable
@Preview
private fun Preview() {
    YouampPlayerTheme {
        Scaffold {
            Column(modifier = Modifier.padding(it)) {
                SongItem(
                    item =
                        Song(
                            id = "1",
                            title = "Coolest song in the world",
                            artist = "Coolest artist in the world",
                            artworkUrl = null,
                        ),
                    onMoreClick = {},
                )
                AlbumItem(
                    item =
                        Album(
                            id = "1",
                            title = "Coolest album in the world",
                            artist = "Coolest artist in the world",
                            artworkUrl = null,
                        ),
                    onPlay = {},
                    onAddToQueue = {},
                    onOpenInfo = {},
                )
                ArtistItem(
                    item =
                        Artist(
                            id = "1",
                            name = "Coolest artist in the world with very long title",
                            artworkUrl = null,
                        ),
                    onPlay = {},
                    onAddToQueue = {},
                    onOpenInfo = {},
                )
            }
        }
    }
}
