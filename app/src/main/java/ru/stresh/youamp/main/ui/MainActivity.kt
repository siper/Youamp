package ru.stresh.youamp.main.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.stresh.youamp.core.ui.YouAmpPlayerTheme
import ru.stresh.youamp.feature.album.ui.AlbumInfoScreen
import ru.stresh.youamp.feature.albums.ui.AlbumsScreen
import ru.stresh.youamp.feature.main.ui.MainScreen
import ru.stresh.youamp.feature.player.big.ui.PlayerScreen
import ru.stresh.youamp.feature.player.mini.ui.MiniPlayer
import ru.stresh.youamp.feature.server.create.ui.ServerScreen

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        setContent {
            YouAmpPlayerTheme {
                val rootNavController = rememberNavController()

                LaunchedEffect(key1 = "navigation") {
                    viewModel
                        .navigation
                        .onEach {
                            rootNavController.navigate(it.destination)
                        }
                        .launchIn(this)
                }

                NavHost(
                    navController = rootNavController,
                    startDestination = "load"
                ) {
                    composable("main") {
                        MainScreen(
                            miniPlayer = {
                                MiniPlayer(
                                    onClick = {
                                        rootNavController.navigate("player")
                                    }
                                )
                            },
                            albumsScreen = {
                                AlbumsScreen(
                                    onAlbumClick = {
                                        rootNavController.navigate("album/$it")
                                    }
                                )
                            }
                        )
                    }
                    composable("album/{albumId}") {
                        ScreenWithMiniPlayer(rootNavController) {
                            AlbumInfoScreen(
                                onBackClick = { rootNavController.popBackStack() },
                                id = it.requireString("albumId")
                            )
                        }
                    }
                    composable("player") {
                        PlayerScreen(
                            onBackClick = { rootNavController.popBackStack() }
                        )
                    }
                    composable("add_server") {
                        ServerScreen(
                            onBackClick = { rootNavController.popBackStack() },
                            onCloseScreen = { rootNavController.navigate("main") }
                        )
                    }
                    composable("load") {
                        Text(text = "Loading", modifier = Modifier.fillMaxSize())
                    }
                }
            }
        }
    }
}

@Composable
internal fun ScreenWithMiniPlayer(
    rootNavController: NavController,
    content: @Composable () -> Unit
) {
    Surface {
        Column {
            Box(modifier = Modifier.weight(1f)) {
                content.invoke()
            }
            MiniPlayer(
                onClick = {
                    rootNavController.navigate("player")
                }
            )
        }
    }
}

private fun NavBackStackEntry.requireString(key: String): String {
    return requireNotNull(arguments?.getString(key))
}