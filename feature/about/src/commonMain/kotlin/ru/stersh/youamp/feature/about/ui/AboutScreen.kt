package ru.stersh.youamp.feature.about.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import compose.icons.SimpleIcons
import compose.icons.simpleicons.Crowdin
import compose.icons.simpleicons.FDroid
import compose.icons.simpleicons.Github
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import ru.stersh.youamp.core.ui.BackNavigationButton
import ru.stersh.youamp.core.ui.SingleLineText
import ru.stersh.youamp.core.ui.YouampPlayerTheme
import ru.stersh.youamp.feature.about.AppIcon
import ru.stersh.youamp.feature.about.launchSafeAnyUrl
import youamp.feature.about.generated.resources.Res
import youamp.feature.about.generated.resources.about_title
import youamp.feature.about.generated.resources.app_icon_description
import youamp.feature.about.generated.resources.crowdin_icon_description
import youamp.feature.about.generated.resources.crowdin_subtitle
import youamp.feature.about.generated.resources.crowdin_title
import youamp.feature.about.generated.resources.f_droid_icon_description
import youamp.feature.about.generated.resources.f_droid_subtitle
import youamp.feature.about.generated.resources.f_droid_title
import youamp.feature.about.generated.resources.github_icon_description
import youamp.feature.about.generated.resources.github_subtitle
import youamp.feature.about.generated.resources.github_title

@Composable
fun AboutScreen(
    onBackClick: () -> Unit
) {
    val viewModel: AboutAppViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    AboutScreen(
        state = state,
        onBackClick = onBackClick,
        onGithubClick = {
            launchSafeAnyUrl(state.githubUri)
        },
        onFDroidClick = {
            launchSafeAnyUrl(state.fdroidUri)
        },
        onCrowdinClick = {
            launchSafeAnyUrl(state.crwodinUri)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AboutScreen(
    state: AboutStateUi,
    onBackClick: () -> Unit,
    onGithubClick: () -> Unit,
    onFDroidClick: () -> Unit,
    onCrowdinClick: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        topBar = {
            LargeTopAppBar(
                navigationIcon = {
                    BackNavigationButton(onClick = onBackClick)
                },
                title = {
                    Text(text = stringResource(Res.string.about_title))
                },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .verticalScroll(
                    state = rememberScrollState()
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                imageVector = AppIcon,
                contentDescription = stringResource(Res.string.app_icon_description),
                modifier = Modifier
                    .padding(top = 8.dp)
                    .shadow(10.dp, shape = CircleShape)
                    .background(color = Color.White, shape = CircleShape)
                    .size(160.dp)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = state.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = state.version,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Column {
                GithubItem(
                    onClick = onGithubClick
                )
                FDroidItem(
                    onClick = onFDroidClick
                )
                CrowdinItem(
                    onClick = onCrowdinClick
                )
            }
        }
    }
}

@Composable
private fun CrowdinItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ListItem(
        leadingContent = {
            Icon(
                imageVector = SimpleIcons.Crowdin,
                contentDescription = stringResource(Res.string.crowdin_icon_description),
                modifier = Modifier.size(36.dp)
            )
        },
        headlineContent = {
            SingleLineText(
                text = stringResource(Res.string.crowdin_title)
            )
        },
        supportingContent = {
            SingleLineText(
                text = stringResource(Res.string.crowdin_subtitle)
            )
        },
        modifier = modifier.clickable(onClick = onClick)
    )
}

@Composable
private fun FDroidItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ListItem(
        leadingContent = {
            Icon(
                imageVector = SimpleIcons.FDroid,
                contentDescription = stringResource(Res.string.f_droid_icon_description),
                modifier = Modifier.size(36.dp)
            )
        },
        headlineContent = {
            SingleLineText(
                text = stringResource(Res.string.f_droid_title)
            )
        },
        supportingContent = {
            SingleLineText(
                text = stringResource(Res.string.f_droid_subtitle)
            )
        },
        modifier = modifier.clickable(onClick = onClick)
    )
}

@Composable
private fun GithubItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ListItem(
        leadingContent = {
            Icon(
                imageVector = SimpleIcons.Github,
                contentDescription = stringResource(Res.string.github_icon_description),
                modifier = Modifier.size(36.dp)
            )
        },
        headlineContent = {
            SingleLineText(
                text = stringResource(Res.string.github_title)
            )
        },
        supportingContent = {
            SingleLineText(
                text = stringResource(Res.string.github_subtitle)
            )
        },
        modifier = modifier.clickable(onClick = onClick)
    )
}

@Composable
@Preview
private fun AboutScreenPreview() {
    YouampPlayerTheme {
        AboutScreen(
            state = AboutStateUi(
                name = "Youamp",
                version = "1.0.0-alpha04",
                githubUri = "",
                fdroidUri = "",
                crwodinUri = ""
            ),
            onBackClick = {},
            onGithubClick = {},
            onFDroidClick = {},
            onCrowdinClick = {}
        )
    }
}