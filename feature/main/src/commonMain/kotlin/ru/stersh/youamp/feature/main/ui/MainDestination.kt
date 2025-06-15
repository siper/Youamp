package ru.stersh.youamp.feature.main.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.StringResource
import youamp.feature.main.generated.resources.Res
import youamp.feature.main.generated.resources.explore_title
import youamp.feature.main.generated.resources.library_title
import youamp.feature.main.generated.resources.personal_title

internal enum class MainDestination(
    val titleResId: StringResource,
    val icon: ImageVector,
) {
    PERSONAL(
        Res.string.personal_title,
        Icons.Rounded.Person,
    ),
    EXPLORE(
        Res.string.explore_title,
        Icons.Rounded.Search,
    ),
    LIBRARY(
        Res.string.library_title,
        Icons.Rounded.MusicNote,
    ),
}
