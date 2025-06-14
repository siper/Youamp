package ru.stersh.youamp.feature.player.queue.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DragHandle
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import ru.stersh.youamp.core.ui.Artwork
import ru.stersh.youamp.core.ui.ArtworkMaskColor
import ru.stersh.youamp.core.ui.BackNavigationButton
import ru.stersh.youamp.core.ui.DragAndDropLazyColumn
import ru.stersh.youamp.core.ui.SkeletonLayout
import ru.stersh.youamp.core.ui.SkeletonScope
import ru.stersh.youamp.core.ui.SongPlayAnimation
import ru.stersh.youamp.core.ui.YouampPlayerTheme
import youamp.feature.player.queue.generated.resources.Res
import youamp.feature.player.queue.generated.resources.play_queue_title

@Composable
fun PlayerQueueScreen(onBackClick: () -> Unit) {
    val viewModel: PlayerQueueViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val menuState by remember { derivedStateOf { state.menuSongState } }

    PlayerQueueScreen(
        state = state,
        onSongClick = viewModel::playSong,
        onSongLongClick = viewModel::openSongMenu,
        onMoveSong = viewModel::moveSong,
        onBackClick = onBackClick,
    )

    menuState?.let {
        PlayQueueSongMenu(
            state = it,
            onPlaySong = viewModel::playSong,
            onRemoveSong = viewModel::removeSong,
            onDismiss = viewModel::dismissSongMenu,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlayerQueueScreen(
    state: StateUi,
    onSongClick: (index: Int) -> Unit,
    onSongLongClick: (index: Int) -> Unit,
    onMoveSong: (from: Int, to: Int) -> Unit,
    onBackClick: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(text = stringResource(Res.string.play_queue_title))
                },
                navigationIcon = {
                    BackNavigationButton(onClick = onBackClick)
                },
                scrollBehavior = scrollBehavior,
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { padding ->
        if (state.progress) {
            Progress(
                modifier =
                    Modifier
                        .padding(padding)
                        .fillMaxSize(),
            )
        } else {
            DragAndDropLazyColumn(
                items = state.songs,
                onSwap = {
                    onMoveSong(
                        it.from,
                        it.to,
                    )
                },
                modifier = Modifier.padding(padding),
            ) { index, item ->
                SongItem(
                    song = item,
                    onClick = {
                        onSongClick(index)
                    },
                    onLongClick = {
                        onSongLongClick(index)
                    },
                )
            }
        }
    }
}

@Composable
private fun Progress(modifier: Modifier = Modifier) {
    SkeletonLayout(modifier = modifier) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            ProgressItem()
            ProgressItem()
            ProgressItem()
        }
    }
}

@Composable
private fun SkeletonScope.ProgressItem() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SkeletonItem(
            modifier = Modifier.size(48.dp),
            shape = MaterialTheme.shapes.medium,
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.weight(1f),
        ) {
            SkeletonItem(
                modifier =
                    Modifier.size(
                        160.dp,
                        16.dp,
                    ),
            )
            SkeletonItem(
                modifier =
                    Modifier.size(
                        100.dp,
                        16.dp,
                    ),
            )
        }
        SkeletonItem(
            modifier =
                Modifier
                    .padding(end = 8.dp)
                    .size(
                        24.dp,
                        12.dp,
                    ),
            shape = MaterialTheme.shapes.small,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SongItem(
    song: SongUi,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {
    ListItem(
        headlineContent = {
            Text(
                text = song.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        supportingContent = {
            song.artist?.let {
                Text(
                    text = it,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        },
        leadingContent = {
            Artwork(
                artworkUrl = song.artworkUrl,
                placeholder = Icons.Rounded.MusicNote,
                modifier = Modifier.size(48.dp),
            )
            if (song.isCurrent) {
                SongPlayAnimation(
                    isPlaying = song.isPlaying,
                    modifier =
                        Modifier
                            .size(48.dp)
                            .background(
                                color = ArtworkMaskColor,
                                shape = MaterialTheme.shapes.large,
                            ).padding(12.dp),
                )
            }
        },
        trailingContent = {
            Icon(
                imageVector = Icons.Rounded.DragHandle,
                contentDescription = "Drag handle",
            )
        },
        modifier =
            Modifier
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = onLongClick,
                ),
    )
}

@Composable
@Preview
private fun PlayerQueueScreenPreview() {
    YouampPlayerTheme {
        val state =
            StateUi(
                progress = false,
                songs =
                    persistentListOf(
                        SongUi(
                            id = "1",
                            title = "Best song in the world with very long title",
                            artist = "Best artist in the world",
                            artworkUrl = null,
                            isCurrent = false,
                            isPlaying = false,
                        ),
                        SongUi(
                            id = "1",
                            title = "Best song in the world",
                            artist = null,
                            artworkUrl = null,
                            isCurrent = true,
                            isPlaying = false,
                        ),
                    ),
            )
        PlayerQueueScreen(
            state = state,
            onSongClick = {},
            onSongLongClick = {},
            onMoveSong = { from, to -> },
            onBackClick = {},
        )
    }
}

@Composable
@Preview
private fun PlayerQueueScreenProgressPreview() {
    YouampPlayerTheme {
        val state = StateUi()
        PlayerQueueScreen(
            state = state,
            onSongClick = {},
            onSongLongClick = {},
            onMoveSong = { from, to -> },
            onBackClick = {},
        )
    }
}
