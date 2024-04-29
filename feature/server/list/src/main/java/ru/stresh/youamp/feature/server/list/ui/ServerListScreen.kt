package ru.stresh.youamp.feature.server.list.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
    onAddServerClick: () -> Unit,
    onEditServerClick: (serverId: Long) -> Unit
) {
    val viewModel: ServerListViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    ServerListScreen(
        state = state,
        onBackClick = onBackClick,
        onAddServerClick = onAddServerClick,
        onEditServer = onEditServerClick,
        onActiveServer = viewModel::setActiveServer,
        onDeleteServer = viewModel::deleteServer
    )
}

@Composable
private fun ServerListScreen(
    state: ServerListViewModel.StateUi,
    onBackClick: () -> Unit,
    onAddServerClick: () -> Unit,
    onEditServer: (serverId: Long) -> Unit,
    onActiveServer: (serverId: Long) -> Unit,
    onDeleteServer: (serverId: Long) -> Unit,
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
                            onActive = onActiveServer,
                            onDelete = onDeleteServer,
                            onEdit = onEditServer
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
    onActive: (serverId: Long) -> Unit,
    onDelete: (serverId: Long) -> Unit,
    onEdit: (serverId: Long) -> Unit,
) {
    var menuExpanded by rememberSaveable { mutableStateOf(false) }
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
            Box {
                RadioButton(
                    selected = item.isActive,
                    onClick = { onActive(item.id) }
                )
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    if (!item.isActive) {
                        DropdownMenuItem(
                            text = { Text(text = "Activate") },
                            onClick = {
                                menuExpanded = false
                                onActive(item.id)
                            }
                        )
                    }
                    DropdownMenuItem(
                        text = { Text(text = "Edit") },
                        onClick = {
                            menuExpanded = false
                            onEdit(item.id)
                        }
                    )
                    if (!item.isActive) {
                        DropdownMenuItem(
                            text = { Text(text = "Delete") },
                            onClick = {
                                menuExpanded = false
                                onDelete(item.id)
                            }
                        )
                    }
                }
            }
        },
        modifier = Modifier.clickable {
            menuExpanded = true
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
            onEditServer = {},
            onAddServerClick = {},
            onActiveServer = {},
            onDeleteServer = {}
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
            onEditServer = {},
            onAddServerClick = {},
            onActiveServer = {},
            onDeleteServer = {}
        )
    }
}