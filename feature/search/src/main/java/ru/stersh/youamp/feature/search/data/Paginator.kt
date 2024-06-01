package ru.stersh.youamp.feature.search.data

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.stersh.youamp.core.api.SearchResult3
import ru.stersh.youamp.core.utils.Paginator

internal fun searchPaginator(
    query: String,
    onLoadData: suspend (identifier: SearchIdentifier) -> SearchResult3
): Paginator<SearchPaginatorResult, SearchIdentifier> {
    var hasNextSongsPage = true
    var hasNextAlbumsPage = true
    var hasNextArtistsPage = true

    val mutex = Mutex()

    return Paginator(
        startIdentifier = SearchIdentifier(query),
        onLoadData = {
            mutex.withLock {
                val data = onLoadData(it)
                hasNextSongsPage = data.song?.size == SearchIdentifier.Page.PAGE_SIZE
                hasNextAlbumsPage = data.album?.size == SearchIdentifier.Page.PAGE_SIZE
                hasNextArtistsPage = data.artist?.size == SearchIdentifier.Page.PAGE_SIZE

                SearchPaginatorResult(
                    searchResult3 = data,
                    hasMoreSongs = hasNextSongsPage,
                    hasMoreAlbums = hasNextAlbumsPage,
                    hasMoreArtists = hasNextArtistsPage
                )
            }
        },
        onNewIdentifier = { currentIdentifier, params ->
            return@Paginator when (params) {
                SearchLoadPage.Song -> {
                    if (hasNextSongsPage) {
                        currentIdentifier.copy(songsPage = currentIdentifier.songsPage.inc())
                    } else {
                        null
                    }
                }

                SearchLoadPage.Album -> {
                    if (hasNextAlbumsPage) {
                        currentIdentifier.copy(songsPage = currentIdentifier.albumsPage.inc())
                    } else {
                        null
                    }
                }

                SearchLoadPage.Artist -> {
                    if (hasNextArtistsPage) {
                        currentIdentifier.copy(songsPage = currentIdentifier.artistsPage.inc())
                    } else {
                        null
                    }
                }

                else -> SearchIdentifier(query)
            }
        }
    )
}

data class SearchPaginatorResult(
    val searchResult3: SearchResult3,
    val hasMoreSongs: Boolean,
    val hasMoreAlbums: Boolean,
    val hasMoreArtists: Boolean
)

enum class SearchLoadPage { Song, Album, Artist }

internal data class SearchIdentifier(
    val query: String,
    val songsPage: Page = Page.First,
    val albumsPage: Page = Page.First,
    val artistsPage: Page = Page.First
) {
    @JvmInline
    value class Page(private val page: Int) {

        val offset: Int
            get() {
                return if (page == 1) {
                    0
                } else {
                    (page - 1) * PAGE_SIZE
                }
            }

        fun inc(): Page {
            return Page(page.inc())
        }

        companion object {

            val First: Page
                get() = Page(1)

            const val PAGE_SIZE = 5
        }
    }
}