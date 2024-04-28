package ru.stresh.youamp.feature.albums.domain

internal data class Album(
    val id: String,
    val title: String,
    val artist: String,
    val artworkUrl: String?
)