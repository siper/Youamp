package ru.stersh.youamp.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ArtistItem(
    name: String,
    onClick: () -> Unit,
    artworkUrl: String? = null,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .requiredWidth(ArtistItemDefaults.Width),
    ) {
        ArtistArtwork(
            artworkUrl,
            modifier =
                Modifier
                    .clip(CircleShape)
                    .clickable(onClick = onClick),
        )
        ArtistTitle(
            name = name,
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}

@Composable
private fun ArtistTitle(
    name: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = name,
        textAlign = TextAlign.Center,
        minLines = 1,
        maxLines = 2,
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.bodyLarge,
        modifier = modifier.fillMaxWidth(),
    )
}

@Composable
private fun ArtistArtwork(
    artworkUrl: String?,
    modifier: Modifier = Modifier,
) {
    CircleArtwork(
        artworkUrl = artworkUrl,
        placeholder = Icons.Rounded.Person,
        modifier = modifier,
    )
}

@Stable
object ArtistItemDefaults {
    @Stable
    val Width = 160.dp
}

@Composable
@Preview
private fun ArtistItemPreview() {
    MaterialTheme {
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
        ) {
            ArtistItem(
                name = "Name",
                onClick = {},
            )
            SkeletonLayout {
                ArtistSkeleton()
            }
        }
    }
}
