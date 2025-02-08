package ru.stersh.youamp

import android.app.Application
import coil.Coil
import coil.ImageLoader
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import ru.stersh.youamp.core.api.provider.ApiProvider
import ru.stersh.youamp.player.ApiSonicPlayQueueSyncer
import ru.stersh.youamp.player.PlayQueueSyncActivityCallback
import ru.stersh.youamp.player.ProgressSyncActivityCallback
import ru.stersh.youamp.shared.player.progress.PlayerProgressStore
import ru.stersh.youamp.shared.player.provider.PlayerProvider
import timber.log.Timber

class App : Application() {
    private val apiProvider: ApiProvider by inject()
    private val playerProgressStore: PlayerProgressStore by inject()
    private val playerProvider: PlayerProvider by inject()
    private val playQueueSyncer: ApiSonicPlayQueueSyncer by inject()

    override fun onCreate() {
        super.onCreate()
        setupDi(this)
        setupCoil()
        setupTimber()
        setupActivityCallbacks()
    }

    private fun setupActivityCallbacks() {
        registerActivityLifecycleCallbacks(
            ProgressSyncActivityCallback(
                playerProgressStore = playerProgressStore,
                playerProvider = playerProvider,
                apiProvider = apiProvider
            )
        )
        registerActivityLifecycleCallbacks(
            PlayQueueSyncActivityCallback(playQueueSyncer)
        )
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun setupCoil() {
        val apiProvider: ApiProvider = get()
        Coil.setImageLoader(
            ImageLoader
                .Builder(this)
                .okHttpClient {
                    OkHttpClient
                        .Builder()
                        .addInterceptor { chain ->
                            chain.proceed(chain.request())
                                .newBuilder()
                                .removeHeader("cache-control")
                                .removeHeader("expires")
                                .addHeader("cache-control", "public, max-age=604800, no-transform")
                                .build()
                        }
                        .addInterceptor { chain ->
                            val api = runBlocking { apiProvider.getApi() }

                            val request = chain.request()
                            val newUrl = api.appendAuth(
                                request
                                    .url
                                    .toUri()
                                    .toString()
                            )

                            chain.proceed(
                                request
                                    .newBuilder()
                                    .url(newUrl)
                                    .build()
                            )
                        }
                        .build()
                }
                .crossfade(true)
                .build()
        )
    }
}