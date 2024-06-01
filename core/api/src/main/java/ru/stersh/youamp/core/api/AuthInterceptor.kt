package ru.stersh.youamp.core.api

import okhttp3.Interceptor
import okhttp3.Response

internal class AuthInterceptor(
    private val username: String,
    private val password: String,
    private val apiVersion: String,
    private val clientId: String,
    private val useLegacyAuth: Boolean,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val urlBuilder = request
            .url
            .newBuilder()
            .addQueryParameter("u", username)
            .addQueryParameter("v", apiVersion)
            .addQueryParameter("c", clientId)
            .addQueryParameter("f", "json")
        if (useLegacyAuth) {
            urlBuilder.addQueryParameter("p", password)
        } else {
            val salt = Security.generateSalt()
            val token = Security.getToken(salt, password)
            urlBuilder
                .addQueryParameter("s", salt)
                .addQueryParameter("t", token)
        }
        val url = urlBuilder.build()
        return chain.proceed(request.newBuilder().url(url).build())
    }
}
