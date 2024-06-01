package ru.stersh.youamp.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Immutable
data class AlbumUi(
    val id: String,
    val title: String,
    val artist: String?,
    val artworkUrl: String?
)

@Composable
fun AlbumItem(
    album: AlbumUi,
    onAlbumClick: (id: String) -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        shape = MaterialTheme.shapes.large,
        modifier = modifier,
        onClick = {
            onAlbumClick(album.id)
        }
    ) {
        Artwork(
            artworkUrl = album.artworkUrl,
            placeholder = Icons.Rounded.Album,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = album.title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Left,
                minLines = 1,
                maxLines = 1,
                style = MaterialTheme.typography.labelLarge
            )
            if (album.artist != null) {
                Text(
                    text = album.artist,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Left,
                    minLines = 1,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
@Preview
private fun AlbumItemPreview() {
    val album = AlbumUi(
        id = "1",
        title = "Album",
        artist = "Artist",
        artworkUrl = null
    )
    MaterialTheme {
        AlbumItem(
            album = album,
            onAlbumClick = {}
        )
    }
}