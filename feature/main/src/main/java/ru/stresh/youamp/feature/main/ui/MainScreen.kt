package ru.stresh.youamp.feature.main.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun MainScreen(
    miniPlayer: @Composable () -> Unit,
    albumsScreen: @Composable () -> Unit,
    artistsScreen: @Composable () -> Unit,
    onProfileClick: () -> Unit
) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "YouAMP"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onProfileClick() }) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        bottomBar = {
            MainNavigationBar(
                miniPlayer = miniPlayer,
                navController = navController
            )
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavigation.Albums.destination,
            modifier = Modifier.padding(padding)
        ) {
            composable(BottomNavigation.Albums.destination) {
                albumsScreen()
            }
            composable(BottomNavigation.Artists.destination) {
                artistsScreen()
            }
            composable(BottomNavigation.Playlists.destination) {
                Text(text = "Playlists screen")
            }
        }
    }
}

@Composable
private fun MainNavigationBar(
    miniPlayer: @Composable () -> Unit,
    navController: NavHostController
) {
    val items = listOf(
        BottomNavigation.Albums,
        BottomNavigation.Artists,
        BottomNavigation.Playlists
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Column {
        miniPlayer.invoke()
        NavigationBar {
            items.forEach { navigationItem ->
                NavigationBarItem(
                    selected = currentRoute == navigationItem.destination,
                    onClick = { navController.navigate(navigationItem.destination) },
                    label = {
                        Text(
                            text = navigationItem.title,
                            fontWeight = FontWeight.SemiBold,
                        )
                    },
                    icon = {
                        Icon(
                            imageVector = navigationItem.icon,
                            contentDescription = null,
                        )
                    }
                )
            }
        }
    }
}