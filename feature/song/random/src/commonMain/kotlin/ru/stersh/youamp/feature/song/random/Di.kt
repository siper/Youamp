package ru.stersh.youamp.feature.song.random

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.stersh.youamp.core.player.Player
import ru.stersh.youamp.feature.song.random.ui.RandomSongsViewModel
import ru.stersh.youamp.shared.queue.PlayerQueueAudioSourceManager
import ru.stersh.youamp.shared.song.random.SongRandomStorage

val songRandomModule =
    module {
        viewModel {
            RandomSongsViewModel(
                get<SongRandomStorage>(),
                get<PlayerQueueAudioSourceManager>(),
                get<Player>(),
            )
        }
    }
