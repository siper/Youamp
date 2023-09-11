package ru.stresh.youamp.feature.main.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.QueueMusic
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material.icons.rounded.Person
import androidx.compose.ui.graphics.vector.ImageVector

internal sealed class BottomNavigation(
    val title: String,
    val icon: ImageVector,
    val destination: String
) {
    data object Albums : BottomNavigation("Albums", Icons.Rounded.Album, "albums")
    data object Artists : BottomNavigation("Artists", Icons.Rounded.Person, "artists")
    data object Playlists : BottomNavigation("Playlists", Icons.AutoMirrored.Rounded.QueueMusic, "playlists")
}