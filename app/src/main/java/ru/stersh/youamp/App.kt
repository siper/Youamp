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
import ru.stersh.youamp.player.ApiSonicPlayQueueSyncer
import ru.stersh.youamp.player.PlayQueueSyncActivityCallback
import ru.stersh.youamp.player.ProgressSyncActivityCallback
import ru.stresh.youamp.core.api.ApiProvider
import ru.stresh.youamp.core.player.Player
import timber.log.Timber

class App : Application() {
    private val apiProvider: ApiProvider by inject()
    private val player: Player by inject()
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
                player = player,
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