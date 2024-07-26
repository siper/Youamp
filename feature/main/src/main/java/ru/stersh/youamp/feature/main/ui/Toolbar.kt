package ru.stersh.youamp.feature.main.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.stersh.youamp.core.ui.AvatarImage


@Composable
internal fun Toolbar() {
    CenterAlignedTopAppBar(
        title = { Text(text = "Gonic") },
        navigationIcon = {
            AvatarImage(
                avatarUrl = "",
                onAvatarClick = { /*TODO*/ },
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(30.dp)
            )
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Rounded.Settings,
                    contentDescription = null
                )
            }
        }
    )
}