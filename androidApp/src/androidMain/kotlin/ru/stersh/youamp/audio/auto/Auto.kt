package ru.stersh.youamp.audio.auto

object Auto {

    data class Song(
        val id: String,
        val title: String,
        val artist: String?,
        val coverUrl: String?,
        val streamUrl: String
    )

    data class Album(
        val id: String,
        val title: String,
        val coverUrl: String?
    )

    data class Playlist(
        val id: String,
        val title: String,
        val coverUrl: String?
    )

    data class Artist(
        val id: String,
        val name: String,
        val coverUrl: String?
    )
}