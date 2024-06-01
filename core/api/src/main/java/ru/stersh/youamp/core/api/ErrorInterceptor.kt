package ru.stersh.youamp.core.api

import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonEncodingException
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody

internal class ErrorInterceptor(moshi: Moshi) : Interceptor {
    private val errorResponseAdapter = moshi.adapter(ErrorResponse::class.java)

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        val body = response.body?.string() ?: return response
        tryThrowApiError(body)

        val newBody: ResponseBody = body.toResponseBody(response.body?.contentType())
        return response
            .newBuilder()
            .body(newBody)
            .build()
    }

    private fun tryThrowApiError(body: String) {
        try {
            val errorResponse = errorResponseAdapter.fromJson(body) ?: return
            throw ApiException(errorResponse.data.error.code, errorResponse.data.error.message)
        } catch (exception: JsonDataException) {
            // Json body not valid or body not error
        } catch (exception: JsonEncodingException) {
            // Json body not valid or body not error
        }
    }
}
