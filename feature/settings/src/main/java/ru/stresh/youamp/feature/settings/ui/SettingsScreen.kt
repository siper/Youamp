package ru.stresh.youamp.feature.settings.ui

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Dns
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.stersh.youamp.core.ui.BackNavigationButton
import ru.stersh.youamp.core.ui.RoundedIcon
import ru.stersh.youamp.core.ui.SingleLineText
import ru.stersh.youamp.core.ui.YouampPlayerTheme
import ru.stersh.youamp.feature.settings.R

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onServersClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    Scaffold(
        topBar = {
            LargeTopAppBar(
                navigationIcon = {
                    BackNavigationButton(onClick = onBackClick)
                },
                title = {
                    Text(text = stringResource(R.string.settings_title))
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .verticalScroll(
                    state = rememberScrollState()
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ListItem(
                leadingContent = {
                    RoundedIcon(
                        imageVector = Icons.Rounded.Dns,
                        contentDescription = stringResource(R.string.servers_icon_description)
                    )
                },
                headlineContent = {
                    SingleLineText(text = stringResource(R.string.servers_title))
                },
                supportingContent = {
                    SingleLineText(text = stringResource(R.string.servers_subtitle))
                },
                modifier = Modifier.clickable(onClick = onServersClick)
            )
            ListItem(
                leadingContent = {
                    RoundedIcon(
                        imageVector = Icons.Rounded.Info,
                        contentDescription = stringResource(R.string.about_icon_description)
                    )
                },
                headlineContent = {
                    SingleLineText(text = stringResource(R.string.about_title))
                },
                supportingContent = {
                    SingleLineText(text = stringResource(R.string.about_subtitle))
                },
                modifier = Modifier.clickable(onClick = onAboutClick)
            )
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview
private fun SettingsScreenPreview() {
    YouampPlayerTheme {
        SettingsScreen(
            onBackClick = {},
            onServersClick = {},
            onAboutClick = {}
        )
    }
}