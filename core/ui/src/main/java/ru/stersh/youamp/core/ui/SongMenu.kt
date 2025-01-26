package ru.stersh.youamp.core.ui

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.QueuePlayNext
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun SongMenu(
    progress: Boolean = true,
    artwork: @Composable () -> Unit,
    title: @Composable () -> Unit,
    artist: @Composable () -> Unit,
    items: SongMenuScope.() -> Unit,
) {
    val scope = remember { SongMenuScopeImpl() }
    items.invoke(scope)

    Surface(
        color = MaterialTheme.colorScheme.surface
    ) {
        if (progress) {
            Progress(scope.items.size)
        } else {
            Content(
                artwork = artwork,
                title = title,
                artist = artist,
                menuItems = scope.items
            )
        }
    }
}

@Composable
private fun Progress(menuItemCount: Int) {
    SkeletonLayout {
        Column(
            modifier = Modifier
                .padding(top = 8.dp)
                .windowInsetsPadding(WindowInsets.navigationBars),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SkeletonItem(
                    modifier = Modifier.size(64.dp)
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    SkeletonItem(
                        modifier = Modifier.size(height = 12.dp, width = 80.dp)
                    )
                    SkeletonItem(
                        modifier = Modifier.size(height = 12.dp, width = 68.dp)
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 12.dp)
            ) {
                repeat(menuItemCount) {
                    SkeletonItem(
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .size(height = 32.dp, width = Random.nextInt(100, 264).dp)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun Content(
    artwork: @Composable () -> Unit,
    title: @Composable () -> Unit,
    artist: @Composable () -> Unit,
    menuItems: List<@Composable () -> Unit>
) {
    Column(
        modifier = Modifier
            .padding(top = 8.dp)
            .windowInsetsPadding(WindowInsets.navigationBars),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(64.dp)
            ) {
                artwork()
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                CompositionLocalProvider(
                    LocalTextStyle provides MaterialTheme.typography.bodyLarge
                ) {
                    title()
                }
                CompositionLocalProvider(
                    LocalTextStyle provides MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.secondary),
                ) {
                    artist()
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        menuItems.forEach {
            it.invoke()
        }
    }
}

@Composable
private fun MenuItem(
    icon: @Composable () -> Unit,
    title: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = remember { RoundedCornerShape(16.dp) }
    Row(
        modifier = Modifier
            .clip(shape)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 24.dp)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CompositionLocalProvider(
            LocalContentColor provides MaterialTheme.colorScheme.secondary
        ) {
            icon()
        }
        Box(modifier = Modifier.weight(1f)) {
            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.bodyMedium
            ) {
                title()
            }
        }
    }
}

interface SongMenuScope {

    fun item(
        icon: @Composable () -> Unit,
        title: @Composable () -> Unit,
        onClick: () -> Unit
    )
}

private class SongMenuScopeImpl : SongMenuScope {

    val items = mutableListOf<@Composable () -> Unit>()

    override fun item(
        icon: @Composable () -> Unit,
        title: @Composable () -> Unit,
        onClick: () -> Unit
    ) {
        items.add {
            MenuItem(
                icon = icon,
                title = title,
                onClick = onClick
            )
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview
private fun AlbumInfoScreenPreview() {
    YouampPlayerTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SongMenu(
                progress = false,
                artwork = {
                    Artwork(
                        artworkUrl = null,
                        placeholder = Icons.Rounded.Album,
                        modifier = Modifier.fillMaxSize()
                    )
                },
                title = {
                    Text("Best song in the world")
                },
                artist = {
                    Text("Best artist")
                },
                items = {
                    item(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.PlayArrow,
                                contentDescription = null
                            )
                        },
                        title = {
                            Text(text = "Play")
                        },
                        onClick = {

                        }
                    )
                    item(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.QueuePlayNext,
                                contentDescription = null
                            )
                        },
                        title = {
                            Text(text = "Play next in queue")
                        },
                        onClick = {

                        }
                    )
                }
            )
        }
    }
}