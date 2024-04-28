package ru.stresh.youamp.feature.server.list.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Dns
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.stresh.youamp.core.ui.YouAmpPlayerTheme

@Composable
fun ServerListScreen(
    onBackClick: () -> Unit,
    onAddServerClick: () -> Unit
) {
    val viewModel: ServerListViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    ServerListScreen(
        state = state,
        onBackClick = onBackClick,
        onAddServerClick = onAddServerClick,
        onActiveServer = viewModel::setActiveServer
    )
}

@Composable
private fun ServerListScreen(
    state: ServerListViewModel.StateUi,
    onBackClick: () -> Unit,
    onAddServerClick: () -> Unit,
    onActiveServer: (serverId: Long) -> Unit
) {
    Scaffold(
        topBar = {
            LargeTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onAddServerClick() }) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = "Add"
                        )
                    }
                },
                title = {
                    Text(text = "Server screen")
                }
            )
        }
    ) {
        when (state) {
            is ServerListViewModel.StateUi.Content -> {
                Column(modifier = Modifier.padding(it)) {
                    for (server in state.items) {
                        ServerItem(
                            item = server,
                            onActive = onActiveServer
                        )
                    }
                }
            }

            is ServerListViewModel.StateUi.Progress -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
private fun ServerItem(
    item: ServerUi,
    onActive: (serverId: Long) -> Unit
) {
    ListItem(
        headlineContent = { Text(item.title) },
        supportingContent = { Text(item.url) },
        leadingContent = {
            Icon(
                imageVector = Icons.Rounded.Dns,
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = CircleShape
                    )
                    .padding(8.dp)
            )
        },
        trailingContent = {
            RadioButton(
                selected = item.isActive,
                onClick = { onActive(item.id) }
            )
        }
    )
}

@Composable
@Preview(name = "Content")
private fun ServerListScreenPreview() {
    val state = ServerListViewModel.StateUi.Content(
        items = listOf(
            ServerUi(
                id = 1,
                title = "Test server",
                url = "http://myserver.com:4040/",
                isActive = true
            ),
            ServerUi(
                id = 2,
                title = "Other server",
                url = "http://otherserver.com:4040/",
                isActive = false
            )
        )
    )
    YouAmpPlayerTheme {
        ServerListScreen(
            state = state,
            onBackClick = {},
            onAddServerClick = {},
            onActiveServer = {}
        )
    }
}

@Composable
@Preview(name = "Progress")
private fun ServerListScreenProgressPreview() {
    val state = ServerListViewModel.StateUi.Progress
    YouAmpPlayerTheme {
        ServerListScreen(
            state = state,
            onBackClick = {},
            onAddServerClick = {},
            onActiveServer = {}
        )
    }
}