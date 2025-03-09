package ru.stersh.youamp

import android.app.Application
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.crossfade
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.stersh.youamp.audio.player.PlayQueueSyncActivityCallback
import ru.stersh.youamp.audio.player.ProgressSyncActivityCallback
import ru.stersh.youamp.core.api.ApiProvider
import ru.stersh.youamp.core.player.Player
import ru.stersh.youamp.player.ApiSonicPlayQueueSyncer

class App : Application() {
    private val apiProvider: ApiProvider by inject()
    private val player: Player by inject()
    private val playQueueSyncer: ApiSonicPlayQueueSyncer by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(diModules + androidModule)
        }
        setupCoil()
        setupActivityCallbacks()
    }

    private fun setupActivityCallbacks() {
        registerActivityLifecycleCallbacks(
            ProgressSyncActivityCallback(
                player = player,
                apiProvider = apiProvider
            )
        )
        registerActivityLifecycleCallbacks(
            PlayQueueSyncActivityCallback(playQueueSyncer)
        )
    }

    private fun setupCoil() {
        val apiProvider: ApiProvider = get()
        SingletonImageLoader.setSafe { context ->
            ImageLoader.Builder(context)
                .crossfade(true)
                .components {
                    add(
                        OkHttpNetworkFetcherFactory(
                            callFactory = {
                                OkHttpClient.Builder()
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
                                        val newUrlBuilder = request
                                            .url
                                            .newBuilder()

                                        api.getClientParams().forEach {
                                            newUrlBuilder.addQueryParameter(it.key, it.value)
                                        }

                                        chain.proceed(
                                            request
                                                .newBuilder()
                                                .url(newUrlBuilder.build())
                                                .build()
                                        )
                                    }
                                    .build()
                            },
                        )
                    )
                }
                .build()
        }
    }
}