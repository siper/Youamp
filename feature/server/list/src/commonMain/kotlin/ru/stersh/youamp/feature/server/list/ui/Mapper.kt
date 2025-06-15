package ru.stersh.youamp.feature.server.list.ui

import ru.stersh.youamp.feature.server.list.domain.Server

internal fun Server.toUi(): ServerUi =
    ServerUi(
        id = id,
        title = title,
        url = url,
        isActive = isActive,
    )
