package ru.stersh.youamp.app.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.koin.compose.viewmodel.koinViewModel
import ru.stersh.youamp.core.ui.YouampPlayerTheme
import ru.stersh.youamp.feature.about.ui.AboutScreen
import ru.stersh.youamp.feature.album.favorites.ui.FavoriteAlbumsScreen
import ru.stersh.youamp.feature.album.info.ui.AlbumInfoScreen
import ru.stersh.youamp.feature.album.list.ui.AlbumsScreen
import ru.stersh.youamp.feature.artist.favorites.ui.FavoriteArtistsScreen
import ru.stersh.youamp.feature.artist.info.ui.ArtistInfoScreen
import ru.stersh.youamp.feature.artist.list.ui.ArtistsScreen
import ru.stersh.youamp.feature.explore.ui.ExploreScreen
import ru.stersh.youamp.feature.library.ui.LibraryScreen
import ru.stersh.youamp.feature.main.ui.MainScreen
import ru.stersh.youamp.feature.personal.ui.PersonalScreen
import ru.stersh.youamp.feature.player.mini.ui.MiniPlayer
import ru.stersh.youamp.feature.player.queue.ui.PlayerQueueScreen
import ru.stersh.youamp.feature.player.screen.ui.PlayerScreen
import ru.stersh.youamp.feature.playlist.info.ui.PlaylistInfoScreen
import ru.stersh.youamp.feature.playlist.list.ui.PlaylistsScreen
import ru.stersh.youamp.feature.search.ui.SearchScreen
import ru.stersh.youamp.feature.server.create.ui.ServerScreen
import ru.stersh.youamp.feature.server.list.ui.ServerListScreen
import ru.stersh.youamp.feature.settings.ui.SettingsScreen
import ru.stersh.youamp.feature.song.favorites.ui.FavoriteSongsScreen
import ru.stersh.youamp.feature.song.info.ui.SongInfoScreen
import ru.stersh.youamp.feature.song.random.ui.RandomSongsScreen

@Composable
fun YouampApp() {
    YouampPlayerTheme {
        val viewModel: MainViewModel = koinViewModel()

        val state by viewModel.state.collectAsState()

        val rootNavController = rememberNavController()
        val viewModelStoreOwner = requireNotNull(LocalViewModelStoreOwner.current)

        when (state.screen) {
            MainScreen.Main -> {
                Content(
                    rootNavController = rootNavController,
                    viewModelStoreOwner = viewModelStoreOwner,
                )
            }

            MainScreen.AddServer -> {
                ServerScreen(
                    onBackClick = { rootNavController.popBackStack() },
                    onCloseScreen = {},
                )
            }

            MainScreen.Progress -> {
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    rootNavController: NavHostController,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    NavHost(
        navController = rootNavController,
        startDestination = Main,
        modifier =
            Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
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
                miniPlayer = {
                    MiniPlayer(
                        viewModelStoreOwner = viewModelStoreOwner,
                        onClick = {
                            rootNavController.navigate(Player)
                        },
                    )
                },
                personal = {
                    PersonalScreen(
                        onSongClick = {
                            rootNavController.navigate(
                                SongInfo(
                                    songId = it,
                                    showAlbum = true,
                                ),
                            )
                        },
                        onAlbumClick = { rootNavController.navigate(AlbumInfo(it)) },
                        onArtistClick = { rootNavController.navigate(ArtistInfo(it)) },
                        onPlaylistClick = { rootNavController.navigate(PlaylistInfo(it)) },
                        onPlaylistsClick = { rootNavController.navigate(Playlists) },
                        onFavoriteSongsClick = { rootNavController.navigate(FavoriteSongs) },
                        onFavoriteAlbumsClick = { rootNavController.navigate(FavoriteAlbums) },
                        onFavoriteArtistsClick = { rootNavController.navigate(FavoriteArtists) },
                    )
                },
                explore = {
                    ExploreScreen(
                        onSearchClick = {
                            rootNavController.navigate(Search)
                        },
                        onRandomSongsClick = {
                            rootNavController.navigate(RandomSongs)
                        },
                        onSongClick = {
                            rootNavController.navigate(
                                SongInfo(
                                    songId = it,
                                    showAlbum = true,
                                ),
                            )
                        },
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
                        },
                    )
                },
                onSettingsClick = {
                    rootNavController.navigate(Settings)
                },
                onAvatarClick = {
                    rootNavController.navigate(ServerList)
                },
            )
        }
        composable<AlbumInfo> { backStackEntry ->
            ScreenWithMiniPlayer(
                viewModelStoreOwner = viewModelStoreOwner,
                onMiniPlayerClick = {
                    rootNavController.navigate(Player)
                },
            ) {
                AlbumInfoScreen(
                    onOpenSongInfo = { songId ->
                        rootNavController.navigate(
                            SongInfo(
                                songId = songId,
                                showAlbum = false,
                            ),
                        )
                    },
                    onBackClick = { rootNavController.popBackStack() },
                    id = backStackEntry.toRoute<AlbumInfo>().albumId,
                )
            }
        }
        composable<Player>(
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Up,
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Down,
                )
            },
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Up,
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Down,
                )
            },
        ) {
            PlayerScreen(
                onBackClick = { rootNavController.popBackStack() },
                onPlayQueueClick = { rootNavController.navigate(PlayQueue) },
            )
        }
        composable<AddServer> {
            ServerScreen(
                onBackClick = { rootNavController.popBackStack() },
                onCloseScreen = { rootNavController.popBackStack() },
            )
        }
        composable<ServerEditor> {
            ServerScreen(
                serverId = it.toRoute<ServerEditor>().serverId,
                onBackClick = { rootNavController.popBackStack() },
                onCloseScreen = { rootNavController.popBackStack() },
            )
        }
        composable<ServerList> {
            ServerListScreen(
                onBackClick = { rootNavController.popBackStack() },
                onAddServerClick = { rootNavController.navigate(AddServer) },
                onEditServerClick = { serverId ->
                    rootNavController.navigate(ServerEditor(serverId))
                },
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
                    rootNavController.navigate(
                        SongInfo(
                            songId = songId,
                            showAlbum = true,
                        ),
                    )
                },
                onOpenArtistInfo = { artistId ->
                    rootNavController.navigate(ArtistInfo(artistId))
                },
            )
        }
        composable<PlayQueue> {
            PlayerQueueScreen(
                onBackClick = { rootNavController.popBackStack() },
            )
        }
        composable<ArtistInfo> {
            ScreenWithMiniPlayer(
                viewModelStoreOwner = viewModelStoreOwner,
                onMiniPlayerClick = {
                    rootNavController.navigate(Player)
                },
            ) {
                ArtistInfoScreen(
                    id = it.toRoute<ArtistInfo>().artistId,
                    onAlbumClick = { albumId ->
                        rootNavController.navigate(AlbumInfo(albumId))
                    },
                    onBackClick = {
                        rootNavController.popBackStack()
                    },
                )
            }
        }
        composable<PlaylistInfo> {
            ScreenWithMiniPlayer(
                viewModelStoreOwner = viewModelStoreOwner,
                onMiniPlayerClick = {
                    rootNavController.navigate(Player)
                },
            ) {
                PlaylistInfoScreen(
                    id = it.toRoute<PlaylistInfo>().playlistId,
                    onBackClick = {
                        rootNavController.popBackStack()
                    },
                )
            }
        }
        composable<Settings> {
            ScreenWithMiniPlayer(
                viewModelStoreOwner = viewModelStoreOwner,
                onMiniPlayerClick = {
                    rootNavController.navigate(Player)
                },
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
                    },
                )
            }
        }
        composable<About> {
            ScreenWithMiniPlayer(
                viewModelStoreOwner = viewModelStoreOwner,
                onMiniPlayerClick = {
                    rootNavController.navigate(Player)
                },
            ) {
                AboutScreen(
                    onBackClick = {
                        rootNavController.popBackStack()
                    },
                )
            }
        }
        composable<Albums> {
            ScreenWithMiniPlayer(
                viewModelStoreOwner = viewModelStoreOwner,
                onMiniPlayerClick = {
                    rootNavController.navigate(Player)
                },
            ) {
                AlbumsScreen(
                    onBackClick = {
                        rootNavController.popBackStack()
                    },
                    onAlbumClick = { albumId ->
                        rootNavController.navigate(AlbumInfo(albumId))
                    },
                )
            }
        }
        composable<Artists> {
            ScreenWithMiniPlayer(
                viewModelStoreOwner = viewModelStoreOwner,
                onMiniPlayerClick = {
                    rootNavController.navigate(Player)
                },
            ) {
                ArtistsScreen(
                    onBackClick = {
                        rootNavController.popBackStack()
                    },
                    onArtistClick = { artistId ->
                        rootNavController.navigate(ArtistInfo(artistId))
                    },
                )
            }
        }
        composable<Playlists> {
            ScreenWithMiniPlayer(
                viewModelStoreOwner = viewModelStoreOwner,
                onMiniPlayerClick = {
                    rootNavController.navigate(Player)
                },
            ) {
                PlaylistsScreen(
                    onBackClick = {
                        rootNavController.popBackStack()
                    },
                    onPlaylistClick = {
                        rootNavController.navigate(PlaylistInfo(it))
                    },
                )
            }
        }
        composable<FavoriteSongs> {
            ScreenWithMiniPlayer(
                viewModelStoreOwner = viewModelStoreOwner,
                onMiniPlayerClick = {
                    rootNavController.navigate(Player)
                },
            ) {
                FavoriteSongsScreen(
                    onBackClick = {
                        rootNavController.popBackStack()
                    },
                    onSongClick = {
                        rootNavController.navigate(
                            SongInfo(
                                songId = it,
                                showAlbum = true,
                            ),
                        )
                    },
                )
            }
        }
        composable<RandomSongs> {
            ScreenWithMiniPlayer(
                viewModelStoreOwner = viewModelStoreOwner,
                onMiniPlayerClick = {
                    rootNavController.navigate(Player)
                },
            ) {
                RandomSongsScreen(
                    onBackClick = {
                        rootNavController.popBackStack()
                    },
                    onSongClick = {
                        rootNavController.navigate(
                            SongInfo(
                                songId = it,
                                showAlbum = true,
                            ),
                        )
                    },
                )
            }
        }
        composable<FavoriteAlbums> {
            ScreenWithMiniPlayer(
                viewModelStoreOwner = viewModelStoreOwner,
                onMiniPlayerClick = {
                    rootNavController.navigate(Player)
                },
            ) {
                FavoriteAlbumsScreen(
                    onBackClick = {
                        rootNavController.popBackStack()
                    },
                    onAlbumClick = {
                        rootNavController.navigate(AlbumInfo(it))
                    },
                )
            }
        }
        composable<FavoriteArtists> {
            ScreenWithMiniPlayer(
                viewModelStoreOwner = viewModelStoreOwner,
                onMiniPlayerClick = {
                    rootNavController.navigate(Player)
                },
            ) {
                FavoriteArtistsScreen(
                    onBackClick = {
                        rootNavController.popBackStack()
                    },
                    onArtistClick = {
                        rootNavController.navigate(ArtistInfo(it))
                    },
                )
            }
        }
        dialog<SongInfo> {
            val songInfo = it.toRoute<SongInfo>()
            ModalBottomSheet(
                containerColor = MaterialTheme.colorScheme.surface,
                onDismissRequest = { rootNavController.popBackStack() },
                contentWindowInsets = {
                    WindowInsets(
                        0.dp,
                        0.dp,
                        0.dp,
                        0.dp,
                    )
                },
                dragHandle = {},
            ) {
                SongInfoScreen(
                    id = songInfo.songId,
                    showAlbum = songInfo.showAlbum,
                    onDismiss = { rootNavController.popBackStack() },
                    onOpenAlbum = { albumId ->
                        rootNavController.navigate(AlbumInfo(albumId))
                    },
                    onOpenArtist = { artistId ->
                        rootNavController.navigate(ArtistInfo(artistId))
                    },
                )
            }
        }
    }
}

@Composable
internal fun ScreenWithMiniPlayer(
    viewModelStoreOwner: ViewModelStoreOwner,
    onMiniPlayerClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    Column {
        Box(
            modifier =
                Modifier
                    .weight(1f)
                    .consumeWindowInsets(WindowInsets.navigationBars),
        ) {
            content.invoke()
        }
        MiniPlayer(
            viewModelStoreOwner = viewModelStoreOwner,
            onClick = onMiniPlayerClick,
            modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
        )
    }
}
