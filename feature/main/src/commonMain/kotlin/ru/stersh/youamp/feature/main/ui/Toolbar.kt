package ru.stersh.youamp.feature.main.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.stersh.youamp.core.ui.AvatarImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Toolbar(
    serverName: String?,
    avatarUrl: String?,
    onAvatarClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
            if (serverName != null) {
                Text(text = serverName)
            }
        },
        navigationIcon = {
            AvatarImage(
                avatarUrl = avatarUrl,
                onAvatarClick = onAvatarClick,
                modifier =
                    Modifier
                        .padding(start = 16.dp)
                        .size(30.dp),
            )
        },
        actions = {
            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Icons.Rounded.Settings,
                    contentDescription = null,
                )
            }
        },
    )
}
