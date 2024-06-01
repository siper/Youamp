package ru.stersh.youamp.core.api

import java.math.BigInteger
import java.security.MessageDigest
import kotlin.random.Random

internal object Security {

    fun generateSalt(): String {
        val length = Random.nextInt(10, 17)
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    fun getToken(salt: String, password: String): String {
        return md5(password + salt)
    }

    private fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray()))
            .toString(16)
            .padStart(32, '0')
    }
}
