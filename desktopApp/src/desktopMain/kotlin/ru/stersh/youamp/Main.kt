package ru.stersh.youamp

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.crossfade
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.jetbrains.compose.resources.painterResource
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatform
import ru.stersh.youamp.core.api.ApiProvider
import ru.stersh.youamp.core.properties.app.AppProperties
import ru.stersh.youamp.app.ui.YouampApp
import youamp.desktopapp.generated.resources.Res
import youamp.desktopapp.generated.resources.ic_launcher_round

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
fun main() = application {
    startKoin {
        modules(diModules + desktopModule)
    }
    val apiProvider = KoinPlatform
        .getKoin()
        .get<ApiProvider>()
    val appProperties = KoinPlatform
        .getKoin()
        .get<AppProperties>()
    SingletonImageLoader.setSafe { context ->
        ImageLoader
            .Builder(context)
            .crossfade(true)
            .components {
                add(
                    OkHttpNetworkFetcherFactory(
                        callFactory = {
                            OkHttpClient
                                .Builder()
                                .addInterceptor { chain ->
                                    chain
                                        .proceed(chain.request())
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

                                    api
                                        .getClientParams()
                                        .forEach {
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
    val windowState = rememberWindowState(size = DpSize(1280.dp, 800.dp))
    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = appProperties.name,
        icon = painterResource(Res.drawable.ic_launcher_round)
    ) {
        val windowSizeClass: WindowSizeClass = calculateWindowSizeClass()
        YouampApp(windowSizeClass)
    }
}