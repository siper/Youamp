package ru.stersh.youamp.feature.server.list.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import org.koin.androidx.compose.koinViewModel
import ru.stersh.youamp.core.ui.BackNavigationButton
import ru.stersh.youamp.core.ui.YouampPlayerTheme
import ru.stersh.youamp.feature.server.list.R

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
    state: StateUi,
    onBackClick: () -> Unit,
    onAddServerClick: () -> Unit,
    onEditServer: (serverId: Long) -> Unit,
    onActiveServer: (serverId: Long) -> Unit,
    onDeleteServer: (serverId: Long) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        topBar = {
            LargeTopAppBar(
                navigationIcon = {
                    BackNavigationButton(onClick = onBackClick)
                },
                actions = {
                    IconButton(onClick = { onAddServerClick() }) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = stringResource(R.string.add_server_title)
                        )
                    }
                },
                title = {
                    Text(text = stringResource(R.string.server_screen_title))
                }
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        if (state.progress) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(it)
            ) {
                items(
                    items = state.items,
                    key = { "server_${it.id}" },
                    contentType = { "server" }
                ) { server ->
                    ServerItem(
                        item = server,
                        onActive = onActiveServer,
                        onDelete = onDeleteServer,
                        onEdit = onEditServer
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
        headlineContent = {
            Text(
                text = item.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        supportingContent = {
            Text(
                text = item.url,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
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
                            text = { Text(text = stringResource(R.string.activate_server_title)) },
                            onClick = {
                                menuExpanded = false
                                onActive(item.id)
                            }
                        )
                    }
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.edit_server_title)) },
                        onClick = {
                            menuExpanded = false
                            onEdit(item.id)
                        }
                    )
                    if (!item.isActive) {
                        DropdownMenuItem(
                            text = { Text(text = stringResource(R.string.delete_server_title)) },
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
    val state = StateUi(
        progress = false,
        items = persistentListOf(
            ServerUi(
                id = 1,
                title = "Test server with very long name",
                url = "http://myserver.very.long.address.com:4040/",
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
    YouampPlayerTheme {
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
    val state = StateUi()
    YouampPlayerTheme {
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