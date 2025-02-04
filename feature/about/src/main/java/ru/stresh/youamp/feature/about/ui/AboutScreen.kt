package ru.stresh.youamp.feature.about.ui

import android.content.res.Configuration
import android.net.Uri
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import compose.icons.SimpleIcons
import compose.icons.simpleicons.Crowdin
import compose.icons.simpleicons.FDroid
import compose.icons.simpleicons.Github
import org.koin.androidx.compose.koinViewModel
import ru.stersh.youamp.core.ui.BackNavigationButton
import ru.stersh.youamp.core.ui.SingleLineText
import ru.stersh.youamp.core.ui.YouampPlayerTheme
import ru.stersh.youamp.core.utils.launchSafeAnyUri
import ru.stersh.youamp.feature.about.R

@Composable
fun AboutScreen(
    onBackClick: () -> Unit
) {
    val viewModel: AboutAppViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    AboutScreen(
        state = state,
        onBackClick = onBackClick,
        onGithubClick = {
            context.launchSafeAnyUri(state.githubUri)
        },
        onFDroidClick = {
            context.launchSafeAnyUri(state.fdroidUri)
        },
        onCrowdinClick = {
            context.launchSafeAnyUri(state.crwodinUri)
        }
    )
}

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
                    Text(text = stringResource(R.string.about_title))
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
                painter = painterResource(id = R.drawable.ic_app_icon),
                contentDescription = stringResource(R.string.app_icon_description),
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
                contentDescription = stringResource(R.string.crowdin_icon_description),
                modifier = Modifier.size(36.dp)
            )
        },
        headlineContent = {
            SingleLineText(
                text = stringResource(R.string.crowdin_title)
            )
        },
        supportingContent = {
            SingleLineText(
                text = stringResource(R.string.crowdin_subtitle)
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
                contentDescription = stringResource(R.string.f_droid_icon_description),
                modifier = Modifier.size(36.dp)
            )
        },
        headlineContent = {
            SingleLineText(
                text = stringResource(R.string.f_droid_title)
            )
        },
        supportingContent = {
            SingleLineText(
                text = stringResource(R.string.f_droid_subtitle)
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
                contentDescription = stringResource(R.string.github_icon_description),
                modifier = Modifier.size(36.dp)
            )
        },
        headlineContent = {
            SingleLineText(
                text = stringResource(R.string.github_title)
            )
        },
        supportingContent = {
            SingleLineText(
                text = stringResource(R.string.github_subtitle)
            )
        },
        modifier = modifier.clickable(onClick = onClick)
    )
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview
private fun AboutScreenPreview() {
    YouampPlayerTheme {
        AboutScreen(
            state = AboutStateUi(
                name = "Youamp",
                version = "1.0.0-alpha04",
                githubUri = Uri.EMPTY,
                fdroidUri = Uri.EMPTY,
                crwodinUri = Uri.EMPTY
            ),
            onBackClick = {},
            onGithubClick = {},
            onFDroidClick = {},
            onCrowdinClick = {}
        )
    }
}