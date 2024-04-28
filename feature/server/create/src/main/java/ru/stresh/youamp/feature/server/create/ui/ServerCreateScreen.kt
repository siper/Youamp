package ru.stresh.youamp.feature.server.create.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.koinViewModel

@Composable
fun ServerScreen(
    onBackClick: () -> Unit,
    onCloseScreen: () -> Unit
) {
    ServerScreen(
        viewModel = koinViewModel(),
        onBackClick = onBackClick,
        onCloseScreen = onCloseScreen
    )
}

@Composable
@Preview
private fun ServerScreenPreview() {
    MaterialTheme {
        ServerScreen(
            viewModel = viewModel(),
            onBackClick = {},
            onCloseScreen = {}
        )
    }
}

@Composable
private fun ServerScreen(
    viewModel: ServerCreateViewModel,
    onBackClick: () -> Unit,
    onCloseScreen: () -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var url by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var useLegacyAuth by rememberSaveable { mutableStateOf(false) }
    val buttonsEnabled by viewModel.buttonsEnabled.collectAsStateWithLifecycle()
    val returnAvailable by viewModel.returnAvailable.collectAsStateWithLifecycle()

    LaunchedEffect("exit_navigation") {
        viewModel
            .exit
            .onEach {
                onCloseScreen()
            }
            .launchIn(this)
    }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = "New Subsonic server"
                    )
                },
                navigationIcon = {
                    if (returnAvailable) {
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
                    viewModel.checkValues(name, url, username, password)
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
                    viewModel.checkValues(name, url, username, password)
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
                    viewModel.checkValues(name, url, username, password)
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
                    viewModel.checkValues(name, url, username, password)
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
                        viewModel.test(url, username, password, useLegacyAuth)
                    },
                    enabled = buttonsEnabled,
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(end = 8.dp)
                ) {
                    Text(text = "Test")
                }
                Button(
                    onClick = {
                        viewModel.add(name, url, username, password, useLegacyAuth)
                    },
                    enabled = buttonsEnabled,
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(start = 8.dp)
                ) {
                    Text(text = "Add")
                }
            }
        }
    }
}