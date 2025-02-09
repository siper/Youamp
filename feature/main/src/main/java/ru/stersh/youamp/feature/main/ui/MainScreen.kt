package ru.stersh.youamp.feature.main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import ru.stersh.youamp.core.ui.YouampPlayerTheme

@Immutable
internal data class MainStateUi(
    val serverInfo: ServerInfo? = null
) {
    @Immutable
    data class ServerInfo(
        val name: String,
        val avatarUrl: String?
    )
}

@Composable
fun MainScreen(
    miniPlayer: @Composable () -> Unit,
    personal: @Composable () -> Unit,
    explore: @Composable () -> Unit,
    library: @Composable () -> Unit,
    onAvatarClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val viewModel: MainViewModel = koinViewModel()
    val state: MainStateUi by viewModel.state.collectAsState()

    MainScreen(
        state = state,
        miniPlayer = miniPlayer,
        personal = personal,
        explore = explore,
        library = library,
        onAvatarClick = onAvatarClick,
        onSettingsClick = onSettingsClick
    )
}
@Composable
internal fun MainScreen(
    state: MainStateUi,
    miniPlayer: @Composable () -> Unit,
    personal: @Composable () -> Unit,
    explore: @Composable () -> Unit,
    library: @Composable () -> Unit,
    onAvatarClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    var currentDestination by rememberSaveable { mutableStateOf(MainDestination.PERSONAL) }
    NavigationSuiteScaffold(
        navigationSuiteItems = {
            MainDestination.entries.forEach { dest ->
                item(
                    icon = {
                        Icon(
                            imageVector = dest.icon,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(text = stringResource(dest.titleResId))
                    },
                    selected = currentDestination == dest,
                    onClick = {
                        currentDestination = dest
                    }
                )
            }
        }
    ) {
        Column {
            Toolbar(
                serverName = state.serverInfo?.name,
                avatarUrl = state.serverInfo?.avatarUrl,
                onAvatarClick = onAvatarClick,
                onSettingsClick = onSettingsClick
            )
            Box(modifier = Modifier.weight(1f)) {
                when (currentDestination) {
                    MainDestination.PERSONAL -> personal()
                    MainDestination.EXPLORE -> explore()
                    MainDestination.LIBRARY -> library()
                }
            }
            miniPlayer()
        }
    }
}

@Composable
@Preview
private fun MainScreenPreview() {
    YouampPlayerTheme {
        MainScreen(
            state = MainStateUi(
                serverInfo = MainStateUi.ServerInfo(
                    name = "Gonic",
                    avatarUrl = null
                )
            ),
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
            personal = { },
            explore = {},
            library = {},
            onAvatarClick = {},
            onSettingsClick = {}
        )
    }
}