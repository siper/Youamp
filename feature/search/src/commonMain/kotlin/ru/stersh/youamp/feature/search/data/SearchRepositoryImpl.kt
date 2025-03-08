package ru.stersh.youamp.feature.search.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import ru.stersh.subsonic.api.model.SearchResult3
import ru.stersh.youamp.core.api.ApiProvider
import ru.stersh.youamp.core.utils.Content
import ru.stersh.youamp.core.utils.Paginator
import ru.stersh.youamp.feature.search.domain.SearchRepository
import ru.stersh.youamp.feature.search.domain.SearchResult

internal class SearchRepositoryImpl(private val apiProvider: ApiProvider) : SearchRepository {

    private val searchQuery = MutableStateFlow("")

    private var paginator: Paginator<SearchPaginatorResult, SearchIdentifier>? = null

    override fun searchResults(): Flow<SearchResult> {
        return searchQuery
            .debounce {
                if (it.isEmpty()) {
                    0
                } else {
                    300
                }
            }
            .flatMapLatest { query ->
                if (query.isEmpty()) {
                    return@flatMapLatest flowOf(SearchResult.Empty)
                }
                val api = apiProvider.getApi()
                val paginator = searchPaginator(query) {
                    search(
                        query = it.query,
                        songsOffset = it.songsPage.offset,
                        albumsOffset = it.albumsPage.offset,
                        artistsOffset = it.artistsPage.offset
                    )
                }
                paginator.restart()
                this.paginator = paginator
                paginator.pages().map { pages ->
                    val content = pages.filterIsInstance<Content<SearchPaginatorResult, SearchIdentifier>>()
                    return@map SearchResult(
                        songs = content
                            .flatMap { it.content.searchResult3.song.toDomainSongs(api) }
                            .distinctBy { it.id },
                        albums = content
                            .flatMap { it.content.searchResult3.album.toDomainAlbums(api) }
                            .distinctBy { it.id },
                        artists = content
                            .flatMap { it.content.searchResult3.artist.toDomainArtists(api) }
                            .distinctBy { it.id },
                        hasMoreSongs = content.lastOrNull()?.content?.hasMoreSongs == true,
                        hasMoreAlbums = content.lastOrNull()?.content?.hasMoreAlbums == true,
                        hasMoreArtists = content.lastOrNull()?.content?.hasMoreArtists == true,
                    )
                }
            }
    }

    override suspend fun search(query: String) {
        searchQuery.value = query
    }

    private suspend fun search(
        query: String,
        songsOffset: Int,
        albumsOffset: Int,
        artistsOffset: Int
    ): SearchResult3 {
        val api = apiProvider.getApi()
        return api.search3(
            query = query,
            songCount = SearchIdentifier.Page.PAGE_SIZE,
            songOffset = songsOffset,
            albumCount = SearchIdentifier.Page.PAGE_SIZE,
            albumOffset = albumsOffset,
            artistCount = SearchIdentifier.Page.PAGE_SIZE,
            artistOffset = artistsOffset
        ).data.searchResult3
    }

    override suspend fun loadMoreSongs() {
        paginator?.loadNextPage(SearchLoadPage.Song)
    }

    override suspend fun loadMoreAlbums() {
        paginator?.loadNextPage(SearchLoadPage.Album)
    }

    override suspend fun loadMoreArtists() {
        paginator?.loadNextPage(SearchLoadPage.Artist)
    }
}