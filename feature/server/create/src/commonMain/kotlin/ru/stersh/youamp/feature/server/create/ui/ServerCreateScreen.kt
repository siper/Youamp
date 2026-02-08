package ru.stersh.youamp.feature.server.create.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Dns
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.stersh.youamp.core.ui.BackNavigationButton
import youamp.feature.server.create.generated.resources.Res
import youamp.feature.server.create.generated.resources.additional_settings_title
import youamp.feature.server.create.generated.resources.edit_server_title
import youamp.feature.server.create.generated.resources.hide_password_description
import youamp.feature.server.create.generated.resources.new_server_title
import youamp.feature.server.create.generated.resources.server_add_action_title
import youamp.feature.server.create.generated.resources.server_address_title
import youamp.feature.server.create.generated.resources.server_icon_description
import youamp.feature.server.create.generated.resources.server_name_title
import youamp.feature.server.create.generated.resources.server_password_title
import youamp.feature.server.create.generated.resources.server_save_action_title
import youamp.feature.server.create.generated.resources.server_test_error_message
import youamp.feature.server.create.generated.resources.server_test_success_message
import youamp.feature.server.create.generated.resources.server_test_title
import youamp.feature.server.create.generated.resources.server_use_legacy_auth_title
import youamp.feature.server.create.generated.resources.server_username_title
import youamp.feature.server.create.generated.resources.show_password_description
import youamp.feature.server.create.generated.resources.use_legacy_auth_subtitle
import youamp.feature.server.create.generated.resources.user_icon_description

@Composable
fun ServerScreen(
    serverId: Long? = null,
    onBackClick: () -> Unit,
    onCloseScreen: () -> Unit,
) {
    val viewModel: ServerCreateViewModel =
        koinViewModel {
            parametersOf(serverId)
        }
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val successTestMessage = stringResource(Res.string.server_test_success_message)
    val errorTestMessage = stringResource(Res.string.server_test_error_message)

    LaunchedEffect(Unit) {
        viewModel
            .exit
            .onEach {
                onCloseScreen()
            }.launchIn(this)
    }
    LaunchedEffect(Unit) {
        viewModel
            .testResult
            .onEach {
                if (it == ServerTestResultUi.Success) {
                    snackbarHostState.showSnackbar(successTestMessage)
                } else {
                    snackbarHostState.showSnackbar(errorTestMessage)
                }
            }.launchIn(this)
    }

    ServerScreen(
        snackbarHostState = snackbarHostState,
        state = state,
        isNewServer = serverId == null,
        onValidateInput = viewModel::validateInput,
        onTest = viewModel::test,
        onAdd = viewModel::add,
        onBackClick = onBackClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ServerScreen(
    snackbarHostState: SnackbarHostState,
    state: ServerCreateStateUi,
    isNewServer: Boolean,
    onValidateInput: (input: ServerInputUi) -> Unit,
    onTest: (server: ServerUi) -> Unit,
    onAdd: (server: ServerUi) -> Unit,
    onBackClick: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text =
                            if (isNewServer) {
                                stringResource(Res.string.new_server_title)
                            } else {
                                stringResource(Res.string.edit_server_title)
                            },
                    )
                },
                navigationIcon = {
                    if (state.closeAvailable) {
                        BackNavigationButton(onClick = onBackClick)
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { padding ->
        if (!state.progress) {
            ContentState(
                state = state,
                isNewServer = isNewServer,
                padding = padding,
                onValidateInput = onValidateInput,
                onTest = onTest,
                onAdd = onAdd,
            )
        }
    }
}

@Composable
private fun ContentState(
    state: ServerCreateStateUi,
    isNewServer: Boolean,
    padding: PaddingValues,
    onValidateInput: (input: ServerInputUi) -> Unit,
    onTest: (server: ServerUi) -> Unit,
    onAdd: (server: ServerUi) -> Unit,
) {
    var name by rememberSaveable { mutableStateOf(state.serverInfo.name) }
    var url by rememberSaveable { mutableStateOf(state.serverInfo.url) }
    var username by rememberSaveable { mutableStateOf(state.serverInfo.username) }
    var password by rememberSaveable { mutableStateOf(state.serverInfo.password) }
    var useLegacyAuth by rememberSaveable { mutableStateOf(state.serverInfo.useLegacyAuth) }

    val server =
        remember(
            name,
            url,
            username,
            password,
            useLegacyAuth,
        ) {
            ServerUi(
                name,
                url,
                username,
                password,
                useLegacyAuth,
            )
        }

    val scrollState = rememberScrollState()
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = Icons.Rounded.Dns,
                    contentDescription = stringResource(Res.string.server_icon_description),
                    modifier =
                        Modifier.padding(
                            top = 8.dp,
                            end = 16.dp,
                        ),
                )
                OutlinedTextField(
                    value = name,
                    label = {
                        Text(text = stringResource(Res.string.server_name_title))
                    },
                    onValueChange = {
                        if (name != it) {
                            name = it
                            onValidateInput(ServerInputUi(it, url, username, password))
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            OutlinedTextField(
                value = url,
                label = {
                    Text(text = stringResource(Res.string.server_address_title))
                },
                onValueChange = {
                    if (url != it) {
                        url = it
                        onValidateInput(ServerInputUi(name, it, username, password))
                    }
                },
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Uri,
                        autoCorrectEnabled = false,
                    ),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 40.dp),
            )
        }

        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = Icons.Rounded.Person,
                    contentDescription = stringResource(Res.string.user_icon_description),
                    modifier =
                        Modifier.padding(
                            top = 8.dp,
                            end = 16.dp,
                        ),
                )
                OutlinedTextField(
                    value = username,
                    label = {
                        Text(text = stringResource(Res.string.server_username_title))
                    },
                    onValueChange = {
                        if (username != it) {
                            username = it
                            onValidateInput(ServerInputUi(name, url, it, password))
                        }
                    },
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                )
            }

            var passwordVisible by rememberSaveable { mutableStateOf(false) }
            OutlinedTextField(
                value = password,
                label = {
                    Text(text = stringResource(Res.string.server_password_title))
                },
                onValueChange = {
                    if (password != it) {
                        password = it
                        onValidateInput(ServerInputUi(name, url, username, it))
                    }
                },
                visualTransformation =
                    if (passwordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                trailingIcon = {
                    IconButton(
                        onClick = { passwordVisible = !passwordVisible },
                    ) {
                        Icon(
                            imageVector =
                                if (passwordVisible) {
                                    Icons.Rounded.Visibility
                                } else {
                                    Icons.Rounded.VisibilityOff
                                },
                            contentDescription =
                                if (passwordVisible) {
                                    stringResource(Res.string.hide_password_description)
                                } else {
                                    stringResource(Res.string.show_password_description)
                                },
                        )
                    }
                },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 40.dp),
            )
        }

        Column {
            Text(
                text = stringResource(Res.string.additional_settings_title),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            ListItem(
                headlineContent = {
                    Text(
                        text = stringResource(Res.string.server_use_legacy_auth_title),
                    )
                },
                supportingContent = {
                    Text(
                        text = stringResource(Res.string.use_legacy_auth_subtitle),
                    )
                },
                trailingContent = {
                    Switch(
                        checked = useLegacyAuth,
                        onCheckedChange = { useLegacyAuth = it },
                        modifier = Modifier.padding(vertical = 20.dp),
                    )
                },
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
            ) {
                OutlinedButton(
                    onClick = {
                        onTest(server)
                    },
                    enabled = state.buttonsEnabled,
                    modifier =
                        Modifier
                            .weight(0.5f)
                            .padding(end = 8.dp),
                ) {
                    Text(text = stringResource(Res.string.server_test_title))
                }
                Button(
                    onClick = {
                        onAdd(server)
                    },
                    enabled = state.buttonsEnabled,
                    modifier =
                        Modifier
                            .weight(0.5f)
                            .padding(start = 8.dp),
                ) {
                    Text(
                        text =
                            if (isNewServer) {
                                stringResource(Res.string.server_add_action_title)
                            } else {
                                stringResource(Res.string.server_save_action_title)
                            },
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun ServerScreenContentPreview() {
    val state = ServerCreateStateUi()
    val snackbarHostState = remember { SnackbarHostState() }
    MaterialTheme {
        ServerScreen(
            snackbarHostState = snackbarHostState,
            state = state,
            isNewServer = true,
            onValidateInput = {},
            onTest = {},
            onAdd = {},
            onBackClick = {},
        )
    }
}
