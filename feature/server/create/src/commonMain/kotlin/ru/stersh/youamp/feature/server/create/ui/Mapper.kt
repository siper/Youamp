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
        useLegacyAuth = useLegacyAuth,
    )
}

internal fun Server.toInfo(): ServerInfoUi =
    ServerInfoUi(
        name = name,
        url = url,
        username = username,
        password = password,
        useLegacyAuth = useLegacyAuth,
    )

private fun String.tryAddingSlashAtEnd(): String =
    if (!this.endsWith("/")) {
        "$this/"
    } else {
        this
    }
