package ru.stresh.youamp.feature.album.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.stersh.youamp.shared.player.queue.AudioSource
import ru.stersh.youamp.shared.player.queue.PlayerQueueAudioSourceManager
import ru.stresh.youamp.core.api.provider.ApiProvider
import ru.stresh.youamp.core.utils.formatSongDuration

internal class AlbumInfoViewModel(
    private val id: String,
    private val apiProvider: ApiProvider,
    private val playerQueueAudioSourceManager: PlayerQueueAudioSourceManager
) : ViewModel() {

    private val _state = MutableStateFlow<AlbumInfoScreenState>(AlbumInfoScreenState.Progress)
    val state: StateFlow<AlbumInfoScreenState>
        get() = _state

    init {
        loadAlbum()
    }

    fun playShuffled() = viewModelScope.launch {
        playerQueueAudioSourceManager.playSource(AudioSource.Album(id), true)
    }

    fun playAll() = viewModelScope.launch {
        playerQueueAudioSourceManager.playSource(AudioSource.Album(id))
    }

    fun onPlaySong(id: String) = viewModelScope.launch {
        playerQueueAudioSourceManager.playSource(AudioSource.Song(id))
    }

    private fun loadAlbum() = viewModelScope.launch {
        val api = apiProvider.getApi()
        val album = api.getAlbum(id)

        _state.value = AlbumInfoScreenState.Content(
            coverArtUrl = api.getCoverArtUrl(album.coverArt),
            title = album.name ?: album.album ?: error("Wtf"),
            artist = album.artist,
            year = album.year.toString(),
            songs = album.song?.map {
                AlbumSongUi(
                    id = it.id,
                    title = it.title,
                    duration = formatSongDuration(it.duration * 1000)
                )
            } ?: emptyList()
        )
    }
}