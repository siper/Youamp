package ru.stresh.youamp.main.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.koinViewModel
import ru.stresh.youamp.core.ui.YouAmpPlayerTheme
import ru.stresh.youamp.feature.album.ui.AlbumInfoScreen
import ru.stresh.youamp.feature.albums.ui.AlbumsScreen
import ru.stresh.youamp.feature.artists.ui.ArtistsScreen
import ru.stresh.youamp.feature.main.ui.MainScreen
import ru.stresh.youamp.feature.player.mini.ui.MiniPlayer
import ru.stresh.youamp.feature.player.screen.ui.PlayerScreen
import ru.stresh.youamp.feature.playlists.ui.PlaylistsScreen
import ru.stresh.youamp.feature.server.create.ui.ServerScreen
import ru.stresh.youamp.feature.server.list.ui.ServerListScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YouAmpPlayerTheme {
                val viewModel: MainViewModel = koinViewModel()

                val state by viewModel.state.collectAsState()

                val rootNavController = rememberNavController()
                val viewModelStoreOwner = requireNotNull(LocalViewModelStoreOwner.current)

                when (state) {
                    StateUi.Content -> {
                        Content(
                            rootNavController = rootNavController,
                            viewModelStoreOwner = viewModelStoreOwner
                        )
                    }

                    StateUi.CreateNewServer -> {
                        ServerScreen(
                            onBackClick = { rootNavController.popBackStack() },
                            onCloseScreen = { rootNavController.navigate("main") }
                        )
                    }

                    StateUi.Progress -> {

                    }
                }
            }
        }
    }

    @Composable
    private fun Content(
        rootNavController: NavHostController,
        viewModelStoreOwner: ViewModelStoreOwner
    ) {
        NavHost(
            navController = rootNavController,
            startDestination = "main",
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            composable(
                route = "main",
                popEnterTransition = {
                    fadeIn()
                },
                popExitTransition = {
                    fadeOut()
                },
            ) {
                MainScreen(
                    miniPlayer = {
                        MiniPlayer(
                            viewModelStoreOwner = viewModelStoreOwner,
                            onClick = {
                                rootNavController.navigate("player")
                            }
                        )
                    },
                    albumsScreen = {
                        AlbumsScreen(
                            viewModelStoreOwner = viewModelStoreOwner,
                            onAlbumClick = {
                                rootNavController.navigate("album/$it")
                            }
                        )
                    },
                    artistsScreen = {
                        ArtistsScreen(
                            viewModelStoreOwner = viewModelStoreOwner,
                            onArtistClick = {
                            }
                        )
                    },
                    playlistsScreen = {
                        PlaylistsScreen(
                            viewModelStoreOwner = viewModelStoreOwner,
                            onPlaylistClick = {

                            }
                        )
                    },
                    onProfileClick = {
                        rootNavController.navigate("server_list")
                    }
                )
            }
            composable("album/{albumId}") {
                ScreenWithMiniPlayer(
                    viewModelStoreOwner = viewModelStoreOwner,
                    onMiniPlayerClick = {
                        rootNavController.navigate("player")
                    }
                ) {
                    AlbumInfoScreen(
                        onBackClick = { rootNavController.popBackStack() },
                        id = it.requireString("albumId")
                    )
                }
            }
            composable(
                route = "player",
                popEnterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Up
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Down
                    )
                },
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Up
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Down
                    )
                }
            ) {
                PlayerScreen(
                    onBackClick = { rootNavController.popBackStack() }
                )
            }
            composable("edit_server/{serverId}") {
                ServerScreen(
                    serverId = it.requireString("serverId").toLong(),
                    onBackClick = { rootNavController.popBackStack() },
                    onCloseScreen = { rootNavController.popBackStack() }
                )
            }
            composable("server_list") {
                ServerListScreen(
                    onBackClick = { rootNavController.popBackStack() },
                    onAddServerClick = { rootNavController.navigate("add_server") },
                    onEditServerClick = {
                        rootNavController.navigate("edit_server/$it")
                    }
                )
            }
        }
    }
}

@Composable
internal fun ScreenWithMiniPlayer(
    viewModelStoreOwner: ViewModelStoreOwner,
    onMiniPlayerClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Surface {
        Column {
            Box(modifier = Modifier.weight(1f)) {
                content.invoke()
            }
            MiniPlayer(
                viewModelStoreOwner = viewModelStoreOwner,
                onClick = onMiniPlayerClick
            )
        }
    }
}

private fun NavBackStackEntry.requireString(key: String): String {
    return requireNotNull(arguments?.getString(key))
}

private fun NavBackStackEntry.requireLong(key: String): Long {
    return requireNotNull(arguments?.getLong(key))
}