package ru.stersh.youamp.core.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SkeletonLayout(
    modifier: Modifier = Modifier,
    content: @Composable SkeletonScope.() -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "skeleton_animation")
    val alphaAnimation by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "skeleton_alpha_animation"
    )
    Box(modifier = modifier.graphicsLayer(alpha = alphaAnimation)) {
        content(SkeletonScope)
    }
}

object SkeletonScope {
    @Composable
    fun SkeletonItem(
        modifier: Modifier = Modifier,
        shape: Shape = remember {
            RoundedCornerShape(16.dp)
        },
        color: Color = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        Box(
            modifier = modifier.background(
                color = color,
                shape = shape
            ),
        )
    }
}

@Composable
fun SkeletonScope.PlaylistSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SkeletonItem(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.size(AlbumItemDefaults.Width),
        )
        SkeletonItem(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.size(
                120.dp,
                24.dp
            ),
        )
    }
}

@Composable
fun SkeletonScope.AlbumSkeleton(
    showArtist: Boolean = true,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        SkeletonItem(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.size(AlbumItemDefaults.Width)
        )
        Column(
            modifier = Modifier.padding(top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            SkeletonItem(
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.size(
                    120.dp,
                    24.dp
                )
            )
            if (showArtist) {
                SkeletonItem(
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.size(
                        80.dp,
                        20.dp
                    )
                )
            }
        }
    }
}

@Composable
fun SkeletonScope.ArtistSkeleton(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        SkeletonItem(
            shape = CircleShape,
            modifier = Modifier.size(ArtistItemDefaults.Width),
        )

        SkeletonItem(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.size(
                120.dp,
                24.dp
            )
        )
    }
}

@Composable
fun SkeletonScope.SongCardChunkSkeleton(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        SkeletonItem(
            shape = SongCardDefaults.TopShape,
            modifier = Modifier.size(
                SongCardDefaults.Width,
                64.dp
            ),
        )

        SkeletonItem(
            shape = SongCardDefaults.CenterShape,
            modifier = Modifier.size(
                SongCardDefaults.Width,
                64.dp
            ),
        )

        SkeletonItem(
            shape = SongCardDefaults.BottomShape,
            modifier = Modifier.size(
                SongCardDefaults.Width,
                64.dp
            ),
        )
    }
}

@Composable
fun SkeletonScope.SongSkeleton(modifier: Modifier = Modifier) {
    ListItem(
        headlineContent = {
            SkeletonItem(
                shape = MaterialTheme.shapes.medium,
                modifier =
                    Modifier.size(
                        width = 180.dp,
                        height = 24.dp,
                    ),
            )
        },
        supportingContent = {
            SkeletonItem(
                shape = MaterialTheme.shapes.medium,
                modifier =
                    Modifier
                        .padding(2.dp)
                        .size(
                            width = 120.dp,
                            height = 20.dp,
                        ),
            )
        },
        leadingContent = {
            SkeletonItem(
                modifier = Modifier.size(48.dp),
            )
        },
        modifier = modifier,
    )
}

@Composable
fun SkeletonScope.SectionSkeleton(modifier: Modifier = Modifier) {
    SkeletonItem(
        modifier =
            Modifier
                .padding(vertical = 28.dp)
                .size(
                    200.dp,
                    32.dp
                ),
    )
}

@Composable
@Preview
private fun SkeletonItemPreview() {
    YouampPlayerTheme {
        Surface {
            SkeletonLayout {
                Column {
                    SkeletonItem(
                        modifier =
                            Modifier
                                .size(
                                    width = 60.dp,
                                    height = 48.dp
                                ),
                    )
                    SkeletonItem(
                        modifier =
                            Modifier
                                .size(
                                    width = 130.dp,
                                    height = 20.dp
                                ),
                    )
                }
            }
        }
    }
}
