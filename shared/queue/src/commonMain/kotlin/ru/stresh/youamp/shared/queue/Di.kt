package ru.stresh.youamp.shared.queue

import org.koin.dsl.module

val queueSharedModule = module {
    single<PlayerQueueAudioSourceManager> { PlayerQueueAudioSourceManagerImpl(get(), get()) }
}
