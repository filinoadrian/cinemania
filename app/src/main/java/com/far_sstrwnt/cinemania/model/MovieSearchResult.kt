package com.far_sstrwnt.cinemania.model

sealed class MovieSearchResult {
    data class Success(val data: List<Movie>) : MovieSearchResult()
    data class Error(val error: Exception) : MovieSearchResult()
}