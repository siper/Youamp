package ru.stersh.youamp.feature.personal.domain

internal data class Album(
    val id: String,
    val title: String,
    val artist: String?,
    val artworkUrl: String?,
)
