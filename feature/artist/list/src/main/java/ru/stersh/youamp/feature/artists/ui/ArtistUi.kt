package ru.stersh.youamp.feature.artists.ui

import androidx.compose.runtime.Immutable

@Immutable
internal data class ArtistUi(
    val id: String,
    val name: String,
    val artworkUrl: String?
)