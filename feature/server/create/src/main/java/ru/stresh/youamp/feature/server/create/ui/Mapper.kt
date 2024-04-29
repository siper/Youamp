package ru.stresh.youamp.feature.server.create.ui

import ru.stresh.youamp.feature.server.create.domain.Server

internal fun ServerUi.toDomain(): Server {
    return Server(
        name, url, username, password, useLegacyAuth
    )
}

internal fun Server.toUi(): ServerUi {
    return ServerUi(
        name, url, username, password, useLegacyAuth
    )
}