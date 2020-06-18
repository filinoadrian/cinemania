package com.far_sstrwnt.cinemania.ui

import androidx.lifecycle.*
import com.far_sstrwnt.cinemania.data.MovieRepository
import com.far_sstrwnt.cinemania.model.MovieSearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class SearchViewModel(private val repository: MovieRepository) : ViewModel() {

    companion object {
        private const val VISIBLE_THRESHOLD = 5
    }

    private val queryLiveData = MutableLiveData<String>()
    val movieResult: LiveData<MovieSearchResult> = queryLiveData.switchMap { queryString ->
        liveData {
            val movies = repository.getSearchResultStream(queryString).asLiveData(Dispatchers.Main)
            emitSource(movies)
        }
    }

    fun searchMovie(queryString: String) {
        queryLiveData.postValue(queryString)
    }

    fun listScrolled(visibleItemCount: Int, lastVisibleItemPosition: Int, totalItemCount: Int) {
        if (visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount) {
            val immutableQuery = queryLiveData.value
            if (immutableQuery != null) {
                viewModelScope.launch {
                    repository.requestMore(immutableQuery)
                }
            }
        }
    }
}