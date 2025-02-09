package ru.stersh.youamp.feature.main.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.ui.graphics.vector.ImageVector
import ru.stersh.youamp.feature.main.R

internal enum class MainDestination(
    @StringRes val titleResId: Int,
    val icon: ImageVector
) {
    PERSONAL(R.string.personal_title, Icons.Rounded.Person),
    EXPLORE(R.string.explore_title, Icons.Rounded.Search),
    LIBRARY(R.string.library_title, Icons.Rounded.MusicNote),
}