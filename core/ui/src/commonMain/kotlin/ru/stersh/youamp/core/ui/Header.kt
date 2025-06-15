package ru.stersh.youamp.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun HeaderLayout(
    image: @Composable (() -> Unit)? = null,
    title: @Composable HeaderTitleScope.() -> Unit = {},
    subtitle: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    modifier: Modifier = Modifier,
) {
    if (isExpandedWidth) {
        HeaderLayoutExpanded(
            image = image,
            title = title,
            subtitle = subtitle,
            actions = actions,
            modifier = modifier,
        )
    } else {
        HeaderLayoutCompact(
            image = image,
            title = title,
            subtitle = subtitle,
            actions = actions,
            modifier = modifier,
        )
    }
}

@Composable
private fun HeaderLayoutExpanded(
    image: @Composable (() -> Unit)?,
    title: @Composable HeaderTitleScope.() -> Unit,
    subtitle: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit,
    modifier: Modifier,
    style: HeaderStyle = HeaderDefaults.expanded(),
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        modifier =
            modifier
                .padding(
                    start = 48.dp,
                    end = 48.dp,
                    bottom = 24.dp,
                ).requiredHeight(HeaderDefaults.ExpandedImageSize),
    ) {
        image?.let {
            Box(
                modifier =
                    Modifier
                        .size(HeaderDefaults.ExpandedImageSize)
                        .aspectRatio(1f),
            ) {
                image()
            }
        }
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxHeight(),
        ) {
            Column {
                ProvideTextStyle(style.titleStyle) {
                    title(HeaderTitleExpandedScopeInstance)
                }
                ProvideTextStyle(style.subtitleStyle) {
                    subtitle()
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                actions()
            }
        }
    }
}

@Composable
private fun HeaderLayoutCompact(
    image: @Composable (() -> Unit)?,
    title: @Composable HeaderTitleScope.() -> Unit,
    subtitle: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit,
    modifier: Modifier,
    style: HeaderStyle = HeaderDefaults.compact(),
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier =
            modifier
                .padding(horizontal = 24.dp)
                .padding(bottom = 12.dp)
                .fillMaxWidth(),
    ) {
        image?.invoke()
        Column(modifier = Modifier.fillMaxWidth()) {
            ProvideTextStyle(style.titleStyle) {
                title(HeaderTitleCompactScopeInstance)
            }
            ProvideTextStyle(style.subtitleStyle) {
                subtitle()
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            actions()
        }
    }
}

@Immutable
interface HeaderTitleScope {
    @Stable
    fun Modifier.singleHeader(): Modifier
}

private object HeaderTitleCompactScopeInstance : HeaderTitleScope {
    @Stable
    override fun Modifier.singleHeader(): Modifier =
        this
            .padding(
                horizontal = 40.dp,
                vertical = 80.dp,
            ).fillMaxWidth()
}

private object HeaderTitleExpandedScopeInstance : HeaderTitleScope {
    @Stable
    override fun Modifier.singleHeader(): Modifier =
        this
            .fillMaxWidth()
}

@Immutable
data class HeaderStyle(
    val titleStyle: TextStyle,
    val subtitleStyle: TextStyle,
)

@Stable
object HeaderDefaults {
    val ExpandedImageSize = 200.dp

    @Composable
    fun expanded(): HeaderStyle =
        HeaderStyle(
            titleStyle = MaterialTheme.typography.displayMedium.merge(textAlign = TextAlign.Start),
            subtitleStyle =
                MaterialTheme.typography.titleMedium.merge(
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.secondary,
                ),
        )

    @Composable
    fun compact(): HeaderStyle =
        HeaderStyle(
            titleStyle = MaterialTheme.typography.headlineLarge.merge(textAlign = TextAlign.Center),
            subtitleStyle =
                MaterialTheme.typography.bodyLarge.merge(
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.secondary,
                ),
        )
}
