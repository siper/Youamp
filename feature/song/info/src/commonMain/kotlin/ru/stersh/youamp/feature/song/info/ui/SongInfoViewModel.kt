package ru.stersh.youamp.feature.song.info.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.stersh.youamp.core.api.ApiProvider
import ru.stersh.youamp.shared.favorites.SongFavoritesStorage
import ru.stersh.youamp.shared.queue.AudioSource
import ru.stersh.youamp.shared.queue.PlayerQueueAudioSourceManager

internal class SongInfoViewModel(
    private val apiProvider: ApiProvider,
    private val playerQueueAudioSourceManager: PlayerQueueAudioSourceManager,
    private val songFavoritesStorage: SongFavoritesStorage
) : ViewModel() {

    val state: StateFlow<State>
        get() = _state

    private val _state = MutableStateFlow(State())
    private var getSongJob: Job? = null

    fun loadSongInfo(songId: String, showAlbum: Boolean) {
        _state.value = State(showAlbum = showAlbum)
        getSongJob?.cancel()
        getSongJob = viewModelScope.launch {
            try {
                val api = apiProvider.getApi()
                val song = api.getSong(songId).data.song
                _state.update {
                    it.copy(
                        artworkUrl = api.getCoverArtUrl(song.coverArt),
                        title = song.title,
                        artist = song.artist,
                        artistId = song.artistId,
                        albumId = song.albumId,
                        progress = false,
                        error = false,
                        favorite = song.starred != null,
                    )
                }
            } catch (error: Exception) {
                if (error is CancellationException) throw error
                _state.update {
                    it.copy(
                        progress = false,
                        error = true,
                    )
                }
            }
        }
    }

    fun play(songId: String) = viewModelScope.launch {
        playerQueueAudioSourceManager.playSource(AudioSource.Song(songId))
    }

    fun addToQueueNext(songId: String) = viewModelScope.launch {
        playerQueueAudioSourceManager.addNext(AudioSource.Song(songId))
    }

    fun addToQueueLast(songId: String) = viewModelScope.launch {
        playerQueueAudioSourceManager.addLast(AudioSource.Song(songId))
    }

    fun addToFavorites(songId: String) = viewModelScope.launch {
        runCatching { songFavoritesStorage.setSongFavorite(songId, true) }
            .onFailure { Logger.w(it) { "Filed to like song" } }
    }

    fun removeFromFavorites(songId: String) = viewModelScope.launch {
        runCatching { songFavoritesStorage.setSongFavorite(songId, false) }
            .onFailure { Logger.e(it) { "Filed to dislike song" } }
    }
}