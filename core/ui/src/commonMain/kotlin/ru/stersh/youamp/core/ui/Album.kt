package ru.stersh.youamp.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AlbumItem(
    title: String,
    onClick: () -> Unit,
    artist: String? = null,
    artworkUrl: String? = null,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.requiredWidth(AlbumItemDefaults.Width),
    ) {
        Card(
            onClick = onClick,
        ) {
            Artwork(
                artworkUrl = artworkUrl,
                shape = MaterialTheme.shapes.medium,
                placeholder = Icons.Rounded.Album,
                modifier = Modifier.aspectRatio(1f),
            )
        }

        Column(
            modifier = Modifier.padding(top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Left,
                minLines = 1,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge,
            )

            if (artist != null) {
                Text(
                    text = artist,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Left,
                    minLines = 1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Stable
object AlbumItemDefaults {
    @Stable
    val Width = 160.dp
}

@Composable
@Preview
private fun AlbumItemPreview() {
    YouampPlayerTheme {
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
        ) {
            AlbumItem(
                title = "Album",
                artist = "artist",
                onClick = {},
            )
            SkeletonLayout {
                AlbumSkeleton()
            }
        }
    }
}
