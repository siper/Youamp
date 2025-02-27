package ru.stersh.youamp.feature.song.info.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.stresh.youamp.core.api.ApiProvider
import ru.stresh.youamp.shared.favorites.SongFavoritesStorage
import ru.stresh.youamp.shared.queue.AudioSource
import ru.stresh.youamp.shared.queue.PlayerQueueAudioSourceManager
import timber.log.Timber

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

    fun playAfterCurrent(songId: String) = viewModelScope.launch {
        playerQueueAudioSourceManager.addAfterCurrent(AudioSource.Song(songId))
    }

    fun addToFavorites(songId: String) = viewModelScope.launch {
        runCatching { songFavoritesStorage.setSongFavorite(songId, true) }
            .onFailure { Timber.e(it) }
    }

    fun removeFromFavorites(songId: String) = viewModelScope.launch {
        runCatching { songFavoritesStorage.setSongFavorite(songId, false) }
            .onFailure { Timber.e(it) }
    }
}