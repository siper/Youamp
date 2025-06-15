package ru.stersh.youamp.feature.song.info.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.stersh.youamp.core.api.ApiProvider
import ru.stersh.youamp.shared.favorites.SongFavoritesStorage
import ru.stersh.youamp.shared.queue.AudioSource
import ru.stersh.youamp.shared.queue.PlayerQueueAudioSourceManager

internal class SongInfoViewModel(
    private val id: String,
    private val showAlbum: Boolean,
    private val apiProvider: ApiProvider,
    private val playerQueueAudioSourceManager: PlayerQueueAudioSourceManager,
    private val songFavoritesStorage: SongFavoritesStorage,
) : ViewModel() {
    private val _state = MutableStateFlow(StateUi(showAlbum = showAlbum))
    val state: StateFlow<StateUi>
        get() = _state

    private val _dismiss = MutableSharedFlow<Unit>()
    val dismiss: Flow<Unit>
        get() = _dismiss

    init {
        loadSongInfo(id)
    }

    fun retry() {
        _state.update {
            it.copy(
                progress = true,
                error = false,
            )
        }
        loadSongInfo(id)
    }

    private fun loadSongInfo(songId: String) =
        viewModelScope.launch {
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
            } catch (exception: Exception) {
                Logger.w(exception) { "Filed to load song info" }
                _state.update {
                    it.copy(
                        progress = false,
                        error = true,
                    )
                }
            }
        }

    fun play(songId: String) =
        viewModelScope.launch {
            playerQueueAudioSourceManager.playSource(AudioSource.Song(songId))
            _dismiss.emit(Unit)
        }

    fun addToQueueNext(songId: String) =
        viewModelScope.launch {
            playerQueueAudioSourceManager.addNext(AudioSource.Song(songId))
            _dismiss.emit(Unit)
        }

    fun addToQueueLast(songId: String) =
        viewModelScope.launch {
            playerQueueAudioSourceManager.addLast(AudioSource.Song(songId))
            _dismiss.emit(Unit)
        }

    fun addToFavorites(songId: String) =
        viewModelScope.launch {
            runCatching {
                songFavoritesStorage.setSongFavorite(
                    songId,
                    true,
                )
            }.onFailure { Logger.w(it) { "Filed to like song" } }
            _dismiss.emit(Unit)
        }

    fun removeFromFavorites(songId: String) =
        viewModelScope.launch {
            runCatching {
                songFavoritesStorage.setSongFavorite(
                    songId,
                    false,
                )
            }.onFailure { Logger.e(it) { "Filed to dislike song" } }
            _dismiss.emit(Unit)
        }
}
