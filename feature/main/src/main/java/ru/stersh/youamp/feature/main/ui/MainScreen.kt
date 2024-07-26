package ru.stersh.youamp.feature.main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.stersh.youamp.core.ui.YouampPlayerTheme
import ru.stersh.youamp.feature.main.R

@Composable
fun MainScreen(
    topBar: @Composable () -> Unit,
    miniPlayer: @Composable () -> Unit,
    albumsScreen: @Composable () -> Unit,
    artistsScreen: @Composable () -> Unit,
    playlistsScreen: @Composable () -> Unit,
    favoritesScreen: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            Toolbar()
        },
        bottomBar = {
            Column {
                miniPlayer()
                NavigationBar {
                    NavigationBarItem(
                        selected = true,
                        onClick = { /*TODO*/ },
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
                        selected = false,
                        onClick = { /*TODO*/ },
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
                        selected = false,
                        onClick = { /*TODO*/ },
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
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        val tabData = arrayOf(
            stringResource(R.string.albums_title),
            stringResource(R.string.artists_title),
            stringResource(R.string.playlists_title),
            stringResource(R.string.favorites_title)
        )
        val pagerState = rememberPagerState { tabData.size }
        val scope = rememberCoroutineScope()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                edgePadding = 0.dp,
                indicator = { tabPositions ->
                    TabRowDefaults.PrimaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage])
                    )
                },
                divider = {},
                modifier = Modifier.fillMaxWidth()
            ) {
                tabData.forEachIndexed { index, tabTitle ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = {
                            Text(text = tabTitle)
                        }
                    )
                }
            }
            HorizontalDivider()
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .clipToBounds()
                    .weight(1f)
            ) {
                when (it) {
                    0 -> albumsScreen()
                    1 -> artistsScreen()
                    2 -> playlistsScreen()
                    3 -> favoritesScreen()
                    else -> {}
                }
            }
        }
    }
}

@Composable
@Preview
private fun MainScreenPreview() {
    YouampPlayerTheme {
        MainScreen(
            topBar = {},
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
            albumsScreen = {},
            artistsScreen = {},
            playlistsScreen = {},
            favoritesScreen = {}
        )
    }
}