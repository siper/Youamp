package ru.stersh.youamp.core.api

import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("rest/ping")
    suspend fun ping(): PingResponse

    @GET("rest/getSong")
    suspend fun getSong(
        @Query("id") id: String,
    ): SongResponse

    @GET("rest/getArtist")
    suspend fun getArtist(
        @Query("id") id: String,
    ): ArtistResponse

    @GET("rest/getAlbum")
    suspend fun getAlbum(
        @Query("id") id: String,
    ): AlbumResponse

    @GET("rest/getPlaylist")
    suspend fun getPlaylist(
        @Query("id") id: String,
    ): PlaylistResponse

    @GET("rest/getPlaylists")
    suspend fun getPlaylists(): PlaylistsResponse

    @GET("rest/getArtists")
    suspend fun getArtists(): ArtistsResponse

    @GET("rest/getAlbumList")
    suspend fun getAlbumList(
        @Query("type") type: String,
        @Query("size") size: Int?,
        @Query("offset") offset: Int?,
        @Query("fromYear") fromYear: Int?,
        @Query("toYear") toYear: Int?,
        @Query("genre") genre: String?,
        @Query("musicFolderId") musicFolderId: String?,
    ): AlbumListResponse

    @GET("rest/getAlbumList2")
    suspend fun getAlbumList2(
        @Query("type") type: String,
        @Query("size") size: Int?,
        @Query("offset") offset: Int?,
        @Query("fromYear") fromYear: Int?,
        @Query("toYear") toYear: Int?,
        @Query("genre") genre: String?,
        @Query("musicFolderId") musicFolderId: String?,
    ): AlbumList2Response

    @GET("rest/getPlayQueue")
    suspend fun getPlayQueue(): PlayQueueResponse

    @GET("rest/savePlayQueue")
    suspend fun savePlayQueue(
        @Query("id")
        id: List<String>,
        @Query("current")
        current: String?,
        @Query("position")
        position: Long?
    )

    @GET("rest/star")
    suspend fun star(
        @Query("id") id: List<String>? = null,
        @Query("albumId") albumIds: List<String>? = null,
        @Query("artistId") artistIds: List<String>? = null,
    )

    @GET("rest/unstar")
    suspend fun unstar(
        @Query("id") id: List<String>? = null,
        @Query("albumId") albumIds: List<String>? = null,
        @Query("artistId") artistIds: List<String>? = null,
    )

    @GET("rest/scrobble")
    suspend fun scrobble(
        @Query("id") id: String,
        @Query("time") time: Long?,
        @Query("submission") submission: Boolean?,
    )

    @GET("rest/search3")
    suspend fun search3(
        @Query("query") query: String,
        @Query("songCount") songCount: Int?,
        @Query("songOffset") songOffset: Int?,
        @Query("albumCount") albumCount: Int?,
        @Query("albumOffset") albumOffset: Int?,
        @Query("artistCount") artistCount: Int?,
        @Query("artistOffset") artistOffset: Int?
    ): SearchResult3Response

    @GET("rest/getStarred2")
    suspend fun getStarred2(
        @Query("musicFolderId") musicFolderId: String?
    ): Starred2Response

    @GET("rest/getRandomSongs")
    suspend fun getRandomSongs(
        @Query("size") size: Int?,
        @Query("genre") genre: String?,
        @Query("fromYear") fromYear: Int?,
        @Query("toYear") toYear: Int?,
        @Query("musicFolderId") musicFolderId: String?
    ): RandomSongsResponse
}