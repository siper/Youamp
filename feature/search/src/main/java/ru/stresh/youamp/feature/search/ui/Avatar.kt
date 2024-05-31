package ru.stresh.youamp.feature.search.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import ru.stersh.youamp.feature.search.R

@Composable
internal fun AvatarImage(
    avatarUrl: String?,
    onAvatarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SubcomposeAsyncImage(
        model = avatarUrl,
        contentDescription = stringResource(R.string.user_avatar_title),
        loading = {
            AvatarPlaceholder()
        },
        error = {
            AvatarPlaceholder()
        },
        modifier = modifier
            .size(AvatarSize)
            .clip(CircleShape)
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = CircleShape
            )
            .clickable {
                onAvatarClick()
            }
    )
}

@Composable
private fun AvatarPlaceholder() {
    Box(modifier = Modifier.size(AvatarSize)) {
        Icon(
            imageVector = Icons.Rounded.Person,
            contentDescription = stringResource(R.string.user_avatar_title),
            tint = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

private val AvatarSize = 40.dp