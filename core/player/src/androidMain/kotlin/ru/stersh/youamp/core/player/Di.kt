package ru.stersh.youamp.core.player

import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.datasource.ResolvingDataSource
import androidx.media3.datasource.okhttp.OkHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import okhttp3.OkHttpClient
import org.koin.core.module.Module
import org.koin.dsl.module

actual val playerCoreModule: Module = module {
    single<androidx.media3.common.Player> {
        val okHttpClient = OkHttpClient
            .Builder()
            .build()

        val okHttpDataSource = OkHttpDataSource
            .Factory(okHttpClient)
            .setDefaultRequestProperties(emptyMap())

        val resolver = ResolvingDataSource.Resolver {
            it
                .buildUpon()
                .setHttpRequestHeaders(emptyMap())
                .build()
        }

        val resolvingDataSource = ResolvingDataSource.Factory(okHttpDataSource, resolver)

        ExoPlayer
            .Builder(get())
            .setLooper(PlayerThread.looper)
            .setMediaSourceFactory(DefaultMediaSourceFactory(resolvingDataSource))
            .setAudioAttributes(AudioAttributes.DEFAULT, true)
            .setWakeMode(C.WAKE_MODE_NETWORK)
            .build()
    }
    single<Player> { AndroidPlayer(get()) }
}