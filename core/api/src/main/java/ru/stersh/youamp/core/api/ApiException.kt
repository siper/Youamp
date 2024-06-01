package ru.stersh.youamp.core.api

import okio.IOException

data class ApiException(val code: Int, override val message: String) : IOException()
