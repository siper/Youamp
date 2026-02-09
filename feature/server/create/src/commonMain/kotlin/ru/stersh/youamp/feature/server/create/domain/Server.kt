package ru.stersh.youamp.feature.server.create.domain

internal data class Server(
    val name: String,
    val url: String,
    val username: String,
    val password: String,
    val authType: AuthType,
) {
    enum class AuthType {
        Unsecure,
        EncodedPassword,
        Token,
    }
}
