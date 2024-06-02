package ru.stersh.youamp.core.api

import android.net.Uri
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class SubsonicApi(
    val url: String,
    val username: String,
    val password: String,
    val apiVersion: String,
    val clientId: String,
    val useLegacyAuth: Boolean
) {
    private val moshi = Moshi.Builder().build()
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val responseInterceptor = ResponseInterceptor()
    private val authInterceptor = AuthInterceptor(
        username,
        password,
        apiVersion,
        clientId,
        useLegacyAuth
    )
    private val errorInterceptor = ErrorInterceptor(moshi)
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(responseInterceptor)
        .addInterceptor(errorInterceptor)
        .addInterceptor(authInterceptor)
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(client)
        .build()
    private val api = retrofit.create(Api::class.java)

    suspend fun ping(): PingResponse = api.ping()

    suspend fun getSong(id: String): Song = api.getSong(id).song

    suspend fun getArtist(id: String): Artist = api.getArtist(id).artist

    suspend fun getAlbum(id: String): Album = api.getAlbum(id).album

    suspend fun getPlaylist(id: String): Playlist = api.getPlaylist(id).playlist

    suspend fun getPlaylists(): List<Playlist> = api.getPlaylists().playlists.playlist

    suspend fun getAlbumList(
        type: ListType,
        size: Int? = null,
        offset: Int? = null,
        fromYear: Int? = null,
        toYear: Int? = null,
        genre: String? = null,
        musicFolderId: String? = null,
    ): List<Album> {
        return api.getAlbumList(
            type.value,
            size,
            offset,
            fromYear,
            toYear,
            genre,
            musicFolderId,
        ).albumList.albums ?: emptyList()
    }

    suspend fun getAlbumList2(
        type: ListType,
        size: Int? = null,
        offset: Int? = null,
        fromYear: Int? = null,
        toYear: Int? = null,
        genre: String? = null,
        musicFolderId: String? = null,
    ): List<Album> {
        return api.getAlbumList2(
            type.value,
            size,
            offset,
            fromYear,
            toYear,
            genre,
            musicFolderId,
        ).albumList.albums ?: emptyList()
    }

    suspend fun getArtists(): List<Artist> {
        return api
            .getArtists()
            .artists
            .indices
            .flatMap { it.artists }
    }

    suspend fun getPlayQueue(): PlayQueueResponse = api.getPlayQueue()

    suspend fun savePlayQueue(
        id: List<String>,
        current: String? = null,
        position: Long? = null
    ) = api.savePlayQueue(id, current, position)

    suspend fun starSong(vararg songId: String, ) = api.star(id = songId.asList())

    suspend fun unstarSong(vararg id: String) = api.unstar(id = id.asList())

    suspend fun scrobble(
        id: String,
        time: Long? = null,
        submission: Boolean? = null,
    ) = api.scrobble(id, time, submission)

    suspend fun search3(
        query: String,
        songCount: Int? = null,
        songOffset: Int? = null,
        albumCount: Int? = null,
        albumOffset: Int? = null,
        artistCount: Int? = null,
        artistOffset: Int? = null
    ) = api.search3(query, songCount, songOffset, albumCount, albumOffset, artistCount, artistOffset)

    fun getCoverArtUrl(
        id: String,
        size: Int? = null,
        auth: Boolean = false
    ): String {
        return buildUrl("getCoverArt", mapOf("id" to id, "size" to size), auth)
    }

    fun downloadUrl(
        id: String,
    ): String = buildUrl("download", mapOf("id" to id))

    fun streamUrl(
        id: String,
    ): String = buildUrl("stream", mapOf("id" to id))

    fun avatarUrl(
        username: String,
        auth: Boolean = false
    ): String = buildUrl("getAvatar", mapOf("username" to username), auth)

    fun appendAuth(url: String): String {
        val builder = Uri
            .parse(url)
            .buildUpon()

        builder.appendAuth()

        return builder
            .build()
            .toString()
    }

    fun availableListTypes(): List<String> {
        val types = mutableListOf("random", "newest", "highest", "frequent", "recent")
        if (sinceVersion("1.8.0")) {
            types.add("alphabeticalByName")
            types.add("alphabeticalByArtist")
            types.add("starred")
        }
        if (sinceVersion("1.10.1")) {
            types.add("byYear")
            types.add("byGenre")
        }
        return types.toList()
    }

    private fun buildUrl(
        path: String,
        queryMap: Map<String, Any?>,
        auth: Boolean = true
    ): String {
        val uriBuilder = Uri
            .parse(url)
            .buildUpon()
            .appendPath("rest")
            .appendPath(path)

        queryMap.forEach { entry ->
            if (entry.value != null) {
                uriBuilder.appendQueryParameter(entry.key, entry.value.toString())
            }
        }

        if (auth) {
            uriBuilder.appendAuth()
        }

        return uriBuilder
            .build()
            .toString()
    }

    private fun Uri.Builder.appendAuth() {
        appendQueryParameter("u", username)
        appendQueryParameter("c", clientId)
        appendQueryParameter("v", apiVersion)
        appendQueryParameter("f", "json")

        if (useLegacyAuth) {
            appendQueryParameter("p", password)
        } else {
            val salt = Security.generateSalt()
            val token = Security.getToken(salt, password)
            appendQueryParameter("s", salt)
            appendQueryParameter("t", token)
        }
    }

    private fun sinceVersion(required: String): Boolean {
        val apiVersion = apiVersion
            .replace(".", "")
            .toInt()
        val requiredVersion = required
            .replace(".", "")
            .toInt()
        return apiVersion >= requiredVersion
    }
}