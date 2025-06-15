package ru.stersh.youamp.shared.queue

data class PlayingSource(
    val serverId: Long,
    val id: String,
    val type: Type,
) {
    enum class Type { Song, Album, Artist, Playlist }

    companion object {
        fun ofSong(
            serverId: Long,
            songId: String,
        ): PlayingSource =
            PlayingSource(
                serverId = serverId,
                id = songId,
                type = Type.Song,
            )

        fun ofAlbum(
            serverId: Long,
            albumId: String,
        ): PlayingSource =
            PlayingSource(
                serverId = serverId,
                id = albumId,
                type = Type.Album,
            )

        fun ofArtist(
            serverId: Long,
            artistId: String,
        ): PlayingSource =
            PlayingSource(
                serverId = serverId,
                id = artistId,
                type = Type.Artist,
            )

        fun ofPlaylist(
            serverId: Long,
            playlistId: String,
        ): PlayingSource =
            PlayingSource(
                serverId = serverId,
                id = playlistId,
                type = Type.Playlist,
            )
    }
}

fun PlayingSource.equals(
    serverId: Long,
    audioSource: AudioSource,
): Boolean {
    val type =
        when (audioSource) {
            is AudioSource.Album -> PlayingSource.Type.Album
            is AudioSource.Artist -> PlayingSource.Type.Artist
            is AudioSource.Playlist -> PlayingSource.Type.Playlist
            is AudioSource.RawSong,
            is AudioSource.Song,
            -> PlayingSource.Type.Song
        }
    return serverId == this.serverId && audioSource.id == this.id && type == this.type
}
