package ru.stersh.youamp.feature.server.create.ui

import ru.stersh.youamp.feature.server.create.domain.Server

internal fun ServerUi.toDomain(): Server {
    val formattedUrl =
        url
            .filter { !it.isWhitespace() }
            .tryAddingSlashAtEnd()
    return Server(
        name = name,
        url = formattedUrl,
        username = username,
        password = password,
        authType = authType.toDomain(),
    )
}

internal fun Server.toInfo(): ServerInfoUi =
    ServerInfoUi(
        name = name,
        url = url,
        username = username,
        password = password,
        authType = authType.toUi(),
    )

private fun String.tryAddingSlashAtEnd(): String =
    if (!this.endsWith("/")) {
        "$this/"
    } else {
        this
    }

private fun AuthTypeUi.toDomain(): Server.AuthType =
    when (this) {
        AuthTypeUi.Unsecure -> Server.AuthType.Unsecure
        AuthTypeUi.EncodedPassword -> Server.AuthType.EncodedPassword
        AuthTypeUi.Token -> Server.AuthType.Token
    }

private fun Server.AuthType.toUi(): AuthTypeUi =
    when (this) {
        Server.AuthType.Unsecure -> AuthTypeUi.Unsecure
        Server.AuthType.EncodedPassword -> AuthTypeUi.EncodedPassword
        Server.AuthType.Token -> AuthTypeUi.Token
    }
