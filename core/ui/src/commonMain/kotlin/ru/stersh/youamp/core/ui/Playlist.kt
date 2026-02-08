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
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PlaylistItem(
    title: String,
    onClick: () -> Unit,
    artworkUrl: String? = null,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.requiredWidth(PlaylistItemDefaults.Width),
    ) {
        ElevatedCard(
            onClick = onClick,
        ) {
            Artwork(
                artworkUrl = artworkUrl,
                shape = MaterialTheme.shapes.medium,
                placeholder = Icons.Rounded.MusicNote,
                modifier = Modifier.aspectRatio(1f),
            )
        }

        Text(
            text = title,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            textAlign = TextAlign.Left,
            minLines = 1,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Stable
object PlaylistItemDefaults {
    @Stable
    val Width = 160.dp
}

@Composable
@Preview
private fun PlaylistItemPreview() {
    MaterialTheme {
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
        ) {
            PlaylistItem(
                title = "Playlist",
                onClick = {},
            )
            SkeletonLayout {
                PlaylistSkeleton()
            }
        }
    }
}
