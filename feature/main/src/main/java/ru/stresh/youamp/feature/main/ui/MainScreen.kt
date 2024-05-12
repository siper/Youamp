package ru.stresh.youamp.feature.main.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    topBar: @Composable () -> Unit,
    miniPlayer: @Composable () -> Unit,
    albumsScreen: @Composable () -> Unit,
    artistsScreen: @Composable () -> Unit,
    playlistsScreen: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            topBar()
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                miniPlayer()
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        val tabData = arrayOf("Albums", "Artists", "Playlists")
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
                tabData.forEachIndexed { index, s ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = {
                            Text(text = s)
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
                    else -> {}
                }
            }
        }
    }
}