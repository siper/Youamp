package ru.stersh.youamp.feature.artist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.stersh.youamp.core.api.provider.ApiProvider
import ru.stersh.youamp.core.ui.AlbumUi
import ru.stersh.youamp.shared.player.queue.AudioSource
import ru.stersh.youamp.shared.player.queue.PlayerQueueAudioSourceManager

internal class ArtistInfoViewModel(
    private val id: String,
    private val apiProvider: ApiProvider,
    private val playerQueueAudioSourceManager: PlayerQueueAudioSourceManager
) : ViewModel() {

    private val _state = MutableStateFlow(AlbumInfoStateUi())
    val state: StateFlow<AlbumInfoStateUi>
        get() = _state

    init {
        loadArtist()
    }

    fun playShuffled() = viewModelScope.launch {
        playerQueueAudioSourceManager.playSource(AudioSource.Artist(id), true)
    }

    fun playAll() = viewModelScope.launch {
        playerQueueAudioSourceManager.playSource(AudioSource.Artist(id))
    }

    private fun loadArtist() = viewModelScope.launch {
        val api = apiProvider.getApi()
        val artist = api.getArtist(id)

        _state.update {
            AlbumInfoStateUi(
                coverArtUrl = api.getCoverArtUrl(artist.coverArt),
                name = artist.name,
                progress = false,
                albums = artist
                    .albums
                    .orEmpty()
                    .map {
                        AlbumUi(
                            id = it.id,
                            title = requireNotNull(it.name ?: it.album),
                            artist = null,
                            artworkUrl = api.getCoverArtUrl(it.coverArt)
                        )
                    }
            )
        }
    }
}