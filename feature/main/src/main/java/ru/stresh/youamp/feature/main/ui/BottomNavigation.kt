package ru.stresh.youamp.feature.main.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.QueueMusic
import androidx.compose.material.icons.automirrored.rounded.QueueMusic
import androidx.compose.material.icons.outlined.Album
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material.icons.rounded.Person
import androidx.compose.ui.graphics.vector.ImageVector

internal sealed class BottomNavigation(
    val title: String,
    val icon: BottomNavigationIcon,
    val destination: String
) {
    data object Albums : BottomNavigation(
        title = "Albums",
        icon = BottomNavigationIcon(
            active = Icons.Rounded.Album,
            idle = Icons.Outlined.Album
        ),
        destination = "albums"
    )

    data object Artists : BottomNavigation(
        title = "Artists",
        icon = BottomNavigationIcon(
            active = Icons.Rounded.Person,
            idle = Icons.Outlined.Person
        ),
        destination = "artists"
    )

    data object Playlists : BottomNavigation(
        title = "Playlists",
        icon = BottomNavigationIcon(
            active = Icons.AutoMirrored.Rounded.QueueMusic,
            idle = Icons.AutoMirrored.Outlined.QueueMusic
        ),
        destination = "playlists"
    )
}

internal data class BottomNavigationIcon(
    val active: ImageVector,
    val idle: ImageVector
)