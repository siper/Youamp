package ru.stersh.youamp.main.ui

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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.koin.androidx.compose.koinViewModel
import ru.stersh.youamp.core.ui.YouampPlayerTheme
import ru.stersh.youamp.feature.album.ui.AlbumInfoScreen
import ru.stersh.youamp.feature.albums.ui.AlbumsScreen
import ru.stersh.youamp.feature.artist.ui.ArtistInfoScreen
import ru.stersh.youamp.feature.artists.ui.ArtistsScreen
import ru.stersh.youamp.feature.main.ui.MainScreen
import ru.stersh.youamp.feature.personal.ui.PersonalScreen
import ru.stersh.youamp.feature.player.mini.ui.MiniPlayer
import ru.stersh.youamp.feature.player.queue.ui.PlayerQueueScreen
import ru.stersh.youamp.feature.player.screen.ui.PlayerScreen
import ru.stersh.youamp.feature.playlist.ui.PlaylistInfoScreen
import ru.stersh.youamp.feature.playlists.ui.PlaylistsScreen
import ru.stersh.youamp.feature.search.ui.SearchScreen
import ru.stersh.youamp.feature.server.create.ui.ServerScreen
import ru.stersh.youamp.feature.server.list.ui.ServerListScreen
import ru.stersh.youamp.feature.song.info.ui.SongInfoScreen
import ru.stresh.youamp.feature.about.ui.AboutScreen
import ru.stresh.youamp.feature.explore.ui.ExploreScreen
import ru.stresh.youamp.feature.favorite.list.ui.FavoriteSongsScreen
import ru.stresh.youamp.feature.library.ui.LibraryScreen
import ru.stresh.youamp.feature.settings.ui.SettingsScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YouampPlayerTheme {
                val viewModel: MainViewModel = koinViewModel()

                val state by viewModel.state.collectAsState()

                val rootNavController = rememberNavController()
                val viewModelStoreOwner = requireNotNull(LocalViewModelStoreOwner.current)

                when (state.screen) {
                    MainScreen.Main -> {
                        Content(
                            rootNavController = rootNavController,
                            viewModelStoreOwner = viewModelStoreOwner
                        )
                    }

                    MainScreen.AddServer -> {
                        ServerScreen(
                            onBackClick = { rootNavController.popBackStack() },
                            onCloseScreen = {}
                        )
                    }

                    MainScreen.Progress -> {

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
        var songInfoProperties by remember { mutableStateOf<SongInfoProperties?>(null) }

        NavHost(
            navController = rootNavController,
            startDestination = Main,
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            composable<Main>(
                popEnterTransition = {
                    fadeIn()
                },
                popExitTransition = {
                    fadeOut()
                },
            ) {
                MainScreen(
                    bigPlayer = {
                        PlayerScreen(
                            onBackClick = { rootNavController.popBackStack() },
                            onPlayQueueClick = { rootNavController.navigate(PlayQueue) }
                        )
                    },
                    miniPlayer = {
                        MiniPlayer(
                            viewModelStoreOwner = viewModelStoreOwner,
                            onClick = {
                                rootNavController.navigate(Player)
                            }
                        )
                    },
                    personal = {
                        PersonalScreen(
                            onSongClick = {
                                songInfoProperties = SongInfoProperties(
                                    songId = it,
                                    showAlbum = true
                                )
                            },
                            onAlbumClick = { rootNavController.navigate(AlbumInfo(it)) },
                            onArtistClick = { rootNavController.navigate(ArtistInfo(it)) },
                            onPlaylistClick = { rootNavController.navigate(PlaylistInfo(it)) },
                            onPlaylistsClick = { rootNavController.navigate(Playlists) },
                            onFavoriteSongsClick = { rootNavController.navigate(FavoriteSongs) },
                            onFavoriteAlbumsClick = {},
                            onFavoriteArtistsClick = {}
                        )
                    },
                    explore = {
                        ExploreScreen(
                            onSearchClick = {
                                rootNavController.navigate(Search)
                            },
                            onSongClick = {
                                songInfoProperties = SongInfoProperties(
                                    songId = it,
                                    showAlbum = true
                                )
                            }
                        )
                    },
                    library = {
                        LibraryScreen(
                            onAlbumClick = {
                                rootNavController.navigate(AlbumInfo(it))
                            },
                            onArtistClick = {
                                rootNavController.navigate(ArtistInfo(it))
                            },
                            onArtistsClick = {
                                rootNavController.navigate(Artists)
                            },
                            onAlbumsClick = {
                                rootNavController.navigate(Albums)
                            }
                        )
                    },
                    onSettingsClick = {
                        rootNavController.navigate(Settings)
                    },
                    onAvatarClick = {
                        rootNavController.navigate(ServerList)
                    }
                )
            }
            composable<AlbumInfo> { backStackEntry ->
                ScreenWithMiniPlayer(
                    viewModelStoreOwner = viewModelStoreOwner,
                    onMiniPlayerClick = {
                        rootNavController.navigate(Player)
                    }
                ) {
                    AlbumInfoScreen(
                        onOpenSongInfo = { songId ->
                            songInfoProperties = SongInfoProperties(
                                songId = songId,
                                showAlbum = false
                            )
                        },
                        onBackClick = { rootNavController.popBackStack() },
                        id = backStackEntry.toRoute<AlbumInfo>().albumId
                    )
                }
            }
            composable<Player>(
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
                    onBackClick = { rootNavController.popBackStack() },
                    onPlayQueueClick = { rootNavController.navigate(PlayQueue) }
                )
            }
            composable<AddServer> {
                ServerScreen(
                    onBackClick = { rootNavController.popBackStack() },
                    onCloseScreen = { rootNavController.popBackStack() }
                )
            }
            composable<ServerEditor> {
                ServerScreen(
                    serverId = it.toRoute<ServerEditor>().serverId,
                    onBackClick = { rootNavController.popBackStack() },
                    onCloseScreen = { rootNavController.popBackStack() }
                )
            }
            composable<ServerList> {
                ServerListScreen(
                    onBackClick = { rootNavController.popBackStack() },
                    onAddServerClick = { rootNavController.navigate(AddServer) },
                    onEditServerClick = { serverId ->
                        rootNavController.navigate(ServerEditor(serverId))
                    }
                )
            }
            composable<Search> {
                SearchScreen(
                    onBack = {
                        rootNavController.popBackStack()
                    },
                    onOpenAlbumInfo = { albumId ->
                        rootNavController.navigate(AlbumInfo(albumId))
                    },
                    onOpenSongInfo = { songId ->
                        songInfoProperties = SongInfoProperties(
                            songId = songId,
                            showAlbum = true
                        )
                    },
                    onOpenArtistInfo = { artistId ->
                        rootNavController.navigate(ArtistInfo(artistId))
                    }
                )
            }
            composable<PlayQueue> {
                PlayerQueueScreen(
                    onBackClick = { rootNavController.popBackStack() }
                )
            }
            composable<ArtistInfo> {
                ScreenWithMiniPlayer(
                    viewModelStoreOwner = viewModelStoreOwner,
                    onMiniPlayerClick = {
                        rootNavController.navigate(Player)
                    }
                ) {
                    ArtistInfoScreen(
                        id = it.toRoute<ArtistInfo>().artistId,
                        onAlbumClick = { albumId ->
                            rootNavController.navigate(AlbumInfo(albumId))
                        },
                        onBackClick = {
                            rootNavController.popBackStack()
                        }
                    )
                }
            }
            composable<PlaylistInfo> {
                ScreenWithMiniPlayer(
                    viewModelStoreOwner = viewModelStoreOwner,
                    onMiniPlayerClick = {
                        rootNavController.navigate(Player)
                    }
                ) {
                    PlaylistInfoScreen(
                        id = it.toRoute<PlaylistInfo>().playlistId,
                        onBackClick = {
                            rootNavController.popBackStack()
                        }
                    )
                }
            }
            composable<Settings> {
                ScreenWithMiniPlayer(
                    viewModelStoreOwner = viewModelStoreOwner,
                    onMiniPlayerClick = {
                        rootNavController.navigate(Player)
                    }
                ) {
                    SettingsScreen(
                        onAboutClick = {
                            rootNavController.navigate(About)
                        },
                        onServersClick = {
                            rootNavController.navigate(ServerList)
                        },
                        onBackClick = {
                            rootNavController.popBackStack()
                        }
                    )
                }
            }
            composable<About> {
                ScreenWithMiniPlayer(
                    viewModelStoreOwner = viewModelStoreOwner,
                    onMiniPlayerClick = {
                        rootNavController.navigate(Player)
                    }
                ) {
                    AboutScreen(
                        onBackClick = {
                            rootNavController.popBackStack()
                        }
                    )
                }
            }
            composable<Albums> {
                ScreenWithMiniPlayer(
                    viewModelStoreOwner = viewModelStoreOwner,
                    onMiniPlayerClick = {
                        rootNavController.navigate(Player)
                    }
                ) {
                    AlbumsScreen(
                        onBackClick = {
                            rootNavController.popBackStack()
                        },
                        onAlbumClick = { albumId ->
                            rootNavController.navigate(AlbumInfo(albumId))
                        }
                    )
                }
            }
            composable<Artists> {
                ScreenWithMiniPlayer(
                    viewModelStoreOwner = viewModelStoreOwner,
                    onMiniPlayerClick = {
                        rootNavController.navigate(Player)
                    }
                ) {
                    ArtistsScreen(
                        onBackClick = {
                            rootNavController.popBackStack()
                        },
                        onArtistClick = { artistId ->
                            rootNavController.navigate(ArtistInfo(artistId))
                        }
                    )
                }
            }
            composable<Playlists> {
                ScreenWithMiniPlayer(
                    viewModelStoreOwner = viewModelStoreOwner,
                    onMiniPlayerClick = {
                        rootNavController.navigate(Player)
                    }
                ) {
                    PlaylistsScreen(
                        onBackClick = {
                            rootNavController.popBackStack()
                        },
                        onPlaylistClick = {
                            rootNavController.navigate(PlaylistInfo(it))
                        }
                    )
                }
            }
            composable<FavoriteSongs> {
                ScreenWithMiniPlayer(
                    viewModelStoreOwner = viewModelStoreOwner,
                    onMiniPlayerClick = {
                        rootNavController.navigate(Player)
                    }
                ) {
                    FavoriteSongsScreen(
                        onBackClick = {
                            rootNavController.popBackStack()
                        },
                        onSongClick = {
                            songInfoProperties = SongInfoProperties(
                                songId = it,
                                showAlbum = true
                            )
                        }
                    )
                }
            }
        }
        songInfoProperties?.let { songProperties ->
            ModalBottomSheet(
                containerColor = MaterialTheme.colorScheme.surface,
                onDismissRequest = { songInfoProperties = null },
                contentWindowInsets = {
                    WindowInsets(
                        0.dp,
                        0.dp,
                        0.dp,
                        0.dp
                    )
                },
                dragHandle = {}
            ) {
                SongInfoScreen(
                    id = songProperties.songId,
                    showAlbum = songProperties.showAlbum,
                    onDismiss = {
                        songInfoProperties = null
                    },
                    onOpenAlbum = { albumId ->
                        rootNavController.navigate(AlbumInfo(albumId))
                    },
                    onOpenArtist = { artistId ->
                        rootNavController.navigate(ArtistInfo(artistId))
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
    Column {
        Box(modifier = Modifier.weight(1f)) {
            content.invoke()
        }
        MiniPlayer(
            viewModelStoreOwner = viewModelStoreOwner,
            onClick = onMiniPlayerClick,
            modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
        )
    }
}