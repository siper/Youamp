package ru.stersh.youamp.feature.main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.koinViewModel
import ru.stersh.youamp.core.ui.YouampPlayerTheme

@Immutable
internal data class MainStateUi(
    val serverInfo: ServerInfo? = null
) {
    @Immutable
    data class ServerInfo(
        val name: String,
        val avatarUrl: String?
    )
}

@Composable
fun MainScreen(
    miniPlayer: @Composable () -> Unit,
    bigPlayer: @Composable () -> Unit,
    personal: @Composable () -> Unit,
    explore: @Composable () -> Unit,
    library: @Composable () -> Unit,
    onAvatarClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val viewModel: MainViewModel = koinViewModel()
    val state: MainStateUi by viewModel.state.collectAsState()

    MainScreen(
        state = state,
        miniPlayer = miniPlayer,
        bigPlayer = bigPlayer,
        personal = personal,
        explore = explore,
        library = library,
        onAvatarClick = onAvatarClick,
        onSettingsClick = onSettingsClick
    )
}

private val PERSONAL = "personal"
private val EXPLORE = "explore"
private val LIBRARY = "library"

@Composable
internal fun MainScreen(
    state: MainStateUi,
    miniPlayer: @Composable () -> Unit,
    bigPlayer: @Composable () -> Unit,
    personal: @Composable () -> Unit,
    explore: @Composable () -> Unit,
    library: @Composable () -> Unit,
    onAvatarClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val navController = rememberNavController()
    ExpandableScaffold(
        topBar = {
            Toolbar(
                serverName = state.serverInfo?.name,
                avatarUrl = state.serverInfo?.avatarUrl,
                onAvatarClick = onAvatarClick,
                onSettingsClick = onSettingsClick
            )
        },
        collapsedContent = {
            miniPlayer()
        },
        expandedContent = {
            bigPlayer()
        },
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.route == PERSONAL } == true,
                    onClick = {
                        navController.navigate(PERSONAL) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    label = {
                        Text(text = "Personal")
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.Person,
                            contentDescription = null
                        )
                    },
                )
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.route == EXPLORE } == true,
                    onClick = {
                        navController.navigate(EXPLORE) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    label = {
                        Text(text = "Explore")
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = null
                        )
                    }
                )
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.route == LIBRARY } == true,
                    onClick = {
                        navController.navigate(LIBRARY) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    label = {
                        Text(text = "Library")
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.MusicNote,
                            contentDescription = null
                        )
                    }
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(navController, startDestination = PERSONAL) {
            composable(PERSONAL) { personal() }
            composable(EXPLORE) { explore() }
            composable(LIBRARY) { library() }
        }
    }
}

@Composable
@Preview
private fun MainScreenPreview() {
    YouampPlayerTheme {
        MainScreen(
            state = MainStateUi(
                serverInfo = MainStateUi.ServerInfo(
                    name = "Gonic",
                    avatarUrl = null
                )
            ),
            miniPlayer = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceContainerHighest
                        )
                )
            },
            bigPlayer = {},
            personal = { },
            explore = {},
            library = {},
            onAvatarClick = {},
            onSettingsClick = {}
        )
    }
}