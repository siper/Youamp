package ru.stersh.youamp.shared.queue

sealed class AudioSource(open val id: String) {

    data class Song(override val id: String) : AudioSource(id)

    data class Album(
        override val id: String,
        val songId: String? = null
    ) : AudioSource(id)

    data class Artist(override val id: String) : AudioSource(id)

    data class Playlist(
        override val id: String,
        val songId: String? = null
    ) : AudioSource(id)

    data class RawSong(
        override val id: String,
        val title: String?,
        val artist: String?,
        val album: String?,
        val artworkUrl: String?
    ) : AudioSource(id)
}
