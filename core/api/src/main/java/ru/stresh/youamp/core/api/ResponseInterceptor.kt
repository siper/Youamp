package ru.stresh.youamp.core.api

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject

internal class ResponseInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        val contentType = response.body?.contentType()
        val body = response.body?.string()

        val newBody = cleanBody(body) ?: body

        return response
            .newBuilder()
            .body(newBody?.toResponseBody(contentType))
            .build()
    }

    private fun cleanBody(body: String?): String? {
        if (body == null) {
            return null
        }
        return runCatching {
            JSONObject(body).get("subsonic-response").toString()
        }.getOrNull()
    }
}