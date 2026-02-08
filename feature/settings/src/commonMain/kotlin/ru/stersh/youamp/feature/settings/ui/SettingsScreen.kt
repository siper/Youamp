package ru.stersh.youamp.feature.settings.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Dns
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.stringResource
import ru.stersh.youamp.core.ui.BackNavigationButton
import ru.stersh.youamp.core.ui.RoundedIcon
import ru.stersh.youamp.core.ui.SingleLineText
import ru.stersh.youamp.core.ui.YouampPlayerTheme
import youamp.feature.settings.generated.resources.Res
import youamp.feature.settings.generated.resources.about_icon_description
import youamp.feature.settings.generated.resources.about_subtitle
import youamp.feature.settings.generated.resources.about_title
import youamp.feature.settings.generated.resources.servers_icon_description
import youamp.feature.settings.generated.resources.servers_subtitle
import youamp.feature.settings.generated.resources.servers_title
import youamp.feature.settings.generated.resources.settings_title

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onServersClick: () -> Unit,
    onAboutClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            LargeTopAppBar(
                navigationIcon = {
                    BackNavigationButton(onClick = onBackClick)
                },
                title = {
                    Text(text = stringResource(Res.string.settings_title))
                },
            )
        },
    ) {
        Column(
            modifier =
                Modifier
                    .padding(it)
                    .fillMaxSize()
                    .verticalScroll(
                        state = rememberScrollState(),
                    ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ListItem(
                leadingContent = {
                    RoundedIcon(
                        imageVector = Icons.Rounded.Dns,
                        contentDescription = stringResource(Res.string.servers_icon_description),
                    )
                },
                headlineContent = {
                    SingleLineText(text = stringResource(Res.string.servers_title))
                },
                supportingContent = {
                    SingleLineText(text = stringResource(Res.string.servers_subtitle))
                },
                modifier = Modifier.clickable(onClick = onServersClick),
            )
            ListItem(
                leadingContent = {
                    RoundedIcon(
                        imageVector = Icons.Rounded.Info,
                        contentDescription = stringResource(Res.string.about_icon_description),
                    )
                },
                headlineContent = {
                    SingleLineText(text = stringResource(Res.string.about_title))
                },
                supportingContent = {
                    SingleLineText(text = stringResource(Res.string.about_subtitle))
                },
                modifier = Modifier.clickable(onClick = onAboutClick),
            )
        }
    }
}

@Composable
@Preview
private fun SettingsScreenPreview() {
    YouampPlayerTheme {
        SettingsScreen(
            onBackClick = {},
            onServersClick = {},
            onAboutClick = {},
        )
    }
}
