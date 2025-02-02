package ru.stersh.youamp.feature.personal.ui

import androidx.compose.runtime.Immutable

@Immutable
internal data class PersonalSongUi(
    val id: String,
    val title: String,
    val artist: String?,
    val artworkUrl: String?,
    val isPlaying: Boolean
)