package ru.stersh.youamp.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Immutable
data class ArtistUi(
    val id: String,
    val name: String,
    val artworkUrl: String?
)

@Composable
fun ArtistItem(
    artist: ArtistUi,
    onAlbumClick: (id: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(ArtistShape)
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

@Stable
private val ArtistShape = RoundedCornerShape(36.dp)