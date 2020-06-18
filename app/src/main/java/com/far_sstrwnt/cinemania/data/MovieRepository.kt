package com.far_sstrwnt.cinemania.data

import com.far_sstrwnt.cinemania.api.TmdbService
import com.far_sstrwnt.cinemania.model.Movie
import com.far_sstrwnt.cinemania.model.MovieSearchResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import retrofit2.HttpException
import java.io.IOException


private const val TMDB_STARTING_PAGE_INDEX = 1

@ExperimentalCoroutinesApi
class MovieRepository(private val service: TmdbService) {

    private val inMemoryCache = mutableListOf<Movie>()

    private val searchResults = ConflatedBroadcastChannel<MovieSearchResult>()

    private var lastRequestedPage = TMDB_STARTING_PAGE_INDEX

    private var isRequestInProgress = false

    suspend fun getSearchResultStream(query: String): Flow<MovieSearchResult> {
        lastRequestedPage = 1
        inMemoryCache.clear()
        requestAndSaveData(query)

        return searchResults.asFlow()
    }

    suspend fun requestMore(query: String) {
        if (isRequestInProgress) return
        val successful = requestAndSaveData(query)
        if (successful) {
            lastRequestedPage++
        }
    }

    suspend fun retry(query: String) {
        if (isRequestInProgress) return
        requestAndSaveData(query)
    }

    private suspend fun requestAndSaveData(query: String): Boolean {
        isRequestInProgress = true
        var successful = false

        try {
            val response = service.searchMovies(query, lastRequestedPage)
            val movies = response.results
            inMemoryCache.addAll(movies)
            val moviesByTitle = moviesByTitle(query)
            searchResults.offer(MovieSearchResult.Success(moviesByTitle))
            successful = true
        } catch (exception: IOException) {
            searchResults.offer(MovieSearchResult.Error(exception))
        } catch (exception: HttpException) {
            searchResults.offer(MovieSearchResult.Error(exception))
        }
        isRequestInProgress = false
        return successful
    }

    private fun moviesByTitle(query: String): List<Movie> {
        return inMemoryCache.filter {
            it.title.contains(query, true) ||
                    (it.overview != null && it.overview.contains(query, true))
        }.sortedWith(compareByDescending<Movie> { it.popularity }.thenBy { it.title })
    }
}