package ru.stresh.youamp.feature.server.create.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ServerScreen(
    serverId: Long? = null,
    onBackClick: () -> Unit,
    onCloseScreen: () -> Unit
) {
    val viewModel: ServerCreateViewModel = koinViewModel {
        parametersOf(serverId)
    }
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect("exit_navigation") {
        viewModel
            .exit
            .onEach {
                onCloseScreen()
            }
            .launchIn(this)
    }
    LaunchedEffect("test_snackbar") {
        viewModel
            .testResult
            .onEach {
                if (it == ServerTestResultUi.SUCCESS) {
                    snackbarHostState.showSnackbar("Test success")
                } else {
                    snackbarHostState.showSnackbar("Test error")
                }
            }
            .launchIn(this)
    }

    ServerScreen(
        snackbarHostState,
        state = state,
        isNewServer = serverId == null,
        onValidateInput = viewModel::validateInput,
        onTest = viewModel::test,
        onAdd = viewModel::add,
        onBackClick = onBackClick
    )
}

@Composable
private fun ServerScreen(
    snackbarHostState: SnackbarHostState,
    state: StateUi,
    isNewServer: Boolean,
    onValidateInput: (input: ServerInputUi) -> Unit,
    onTest: (server: ServerUi) -> Unit,
    onAdd: (server: ServerUi) -> Unit,
    onBackClick: () -> Unit,
) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = if (isNewServer) {
                            "New Subsonic server"
                        } else {
                            "Edit server"
                        }
                    )
                },
                navigationIcon = {
                    if (state.returnAvailable) {
                        IconButton(onClick = { onBackClick() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        when (state) {
            is StateUi.Content -> {
                ContentState(
                    state = state,
                    isNewServer = isNewServer,
                    padding = padding,
                    onValidateInput = onValidateInput,
                    onTest = onTest,
                    onAdd = onAdd
                )
            }

            is StateUi.Progress -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
private fun ContentState(
    state: StateUi.Content,
    isNewServer: Boolean,
    padding: PaddingValues,
    onValidateInput: (input: ServerInputUi) -> Unit,
    onTest: (server: ServerUi) -> Unit,
    onAdd: (server: ServerUi) -> Unit,
) {
    var name by rememberSaveable { mutableStateOf(state.initWith?.name.orEmpty()) }
    var url by rememberSaveable { mutableStateOf(state.initWith?.url.orEmpty()) }
    var username by rememberSaveable { mutableStateOf(state.initWith?.username.orEmpty()) }
    var password by rememberSaveable { mutableStateOf(state.initWith?.password.orEmpty()) }
    var useLegacyAuth by rememberSaveable { mutableStateOf(state.initWith?.useLegacyAuth == true) }

    val input = remember(name, url, username, password) {
        ServerInputUi(name, url, username, password)
    }
    val server = remember(name, url, username, password, useLegacyAuth) {
        ServerUi(name, url, username, password, useLegacyAuth)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 16.dp)
    ) {
        OutlinedTextField(
            value = name,
            label = {
                Text(text = "Name")
            },
            onValueChange = {
                name = it
                onValidateInput(input)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp, top = 12.dp)
        )

        OutlinedTextField(
            value = url,
            label = {
                Text(text = "Address")
            },
            onValueChange = {
                url = it
                onValidateInput(input)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp, top = 12.dp)
        )

        OutlinedTextField(
            value = username,
            label = {
                Text(text = "Username")
            },
            onValueChange = {
                username = it
                onValidateInput(input)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = password,
            label = {
                Text(text = "Password")
            },
            onValueChange = {
                password = it
                onValidateInput(input)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Use legacy auth",
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = useLegacyAuth,
                onCheckedChange = { useLegacyAuth = it }
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            OutlinedButton(
                onClick = {
                    onTest(server)
                },
                enabled = state.buttonsEnabled,
                modifier = Modifier
                    .weight(0.5f)
                    .padding(end = 8.dp)
            ) {
                Text(text = "Test")
            }
            Button(
                onClick = {
                    onAdd(server)
                },
                enabled = state.buttonsEnabled,
                modifier = Modifier
                    .weight(0.5f)
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = if (isNewServer) {
                        "Add"
                    } else {
                        "Save"
                    }
                )
            }
        }
    }
}

@Composable
@Preview(name = "Content")
private fun ServerScreenContentPreview() {
    val state = StateUi.Content(
        buttonsEnabled = true,
        returnAvailable = true,
        initWith = null
    )
    val snackbarHostState = remember { SnackbarHostState() }
    MaterialTheme {
        ServerScreen(
            snackbarHostState = snackbarHostState,
            state = state,
            isNewServer = true,
            onValidateInput = {},
            onTest = {},
            onAdd = {},
            onBackClick = {}
        )
    }
}

@Composable
@Preview(name = "Progress")
private fun ServerScreenProgressPreview() {
    val state = StateUi.Progress
    val snackbarHostState = remember { SnackbarHostState() }
    MaterialTheme {
        ServerScreen(
            snackbarHostState = snackbarHostState,
            state = state,
            isNewServer = false,
            onValidateInput = {},
            onTest = {},
            onAdd = {},
            onBackClick = {}
        )
    }
}