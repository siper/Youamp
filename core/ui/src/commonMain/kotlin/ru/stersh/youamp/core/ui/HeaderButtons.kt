package ru.stersh.youamp.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import youamp.core.ui.generated.resources.Res
import youamp.core.ui.generated.resources.dislike_title
import youamp.core.ui.generated.resources.like_title
import youamp.core.ui.generated.resources.play_all_title
import youamp.core.ui.generated.resources.shuffle_title

@Composable
fun FavoriteButton(
    isFavorite: Boolean,
    onChange: (isFavorite: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val text = if (isFavorite) {
        stringResource(Res.string.dislike_title)
    } else {
        stringResource(Res.string.like_title)
    }
    ButtonLayout(
        button = {
            FilledTonalIconToggleButton(
                checked = isFavorite,
                onCheckedChange = onChange,
                modifier = modifier.size(ButtonSize)
            ) {
                Icon(
                    imageVector = if (isFavorite) {
                        Icons.Rounded.Favorite
                    } else {
                        Icons.Rounded.FavoriteBorder
                    },
                    contentDescription = text
                )
            }
        },
        title = {
            Text(
                text = text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        },
        modifier = modifier
    )
}

@Composable
fun PlayAllButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ButtonLayout(
        button = {
            FilledTonalIconButton(
                onClick = onClick,
                modifier = modifier.size(ButtonSize)
            ) {
                Icon(
                    imageVector = Icons.Rounded.PlayArrow,
                    contentDescription = stringResource(Res.string.play_all_title)
                )
            }
        },
        title = {
            Text(
                text = stringResource(Res.string.play_all_title),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        },
        modifier = modifier
    )
}

@Composable
fun PlayShuffledButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ButtonLayout(
        button = {
            FilledTonalIconButton(
                onClick = onClick,
                modifier = modifier.size(ButtonSize)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Shuffle,
                    contentDescription = stringResource(Res.string.shuffle_title)
                )
            }
        },
        title = {
            Text(
                text = stringResource(Res.string.shuffle_title),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        },
        modifier = modifier
    )
}

@Stable
private val ButtonSize = 64.dp

@Composable
private fun ButtonLayout(
    button: @Composable () -> Unit,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.width(72.dp)
    ) {
        button()
        CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.labelSmall) {
            title()
        }
    }
}