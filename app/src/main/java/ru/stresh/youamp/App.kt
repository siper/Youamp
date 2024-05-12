package ru.stresh.youamp

import android.app.Application
import coil.Coil
import coil.ImageLoader
import okhttp3.OkHttpClient

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        setupDi(this)
        setupCoil()
    }

    private fun setupCoil() {
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
                        .build()
                }
                .crossfade(true)
                .build()
        )
    }
}