package com.far_sstrwnt.cinemania.shared.result

sealed class Result<out T> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    data class Loading<out T>(val data: T? = null) : Result<T>()

    companion object {
        fun <T> loading(value: T?): Result<T> = Loading(value)

        fun <T> success(value: T): Result<T> = Success(value)

        fun <T> error(message: String): Result<T> = Error(Exception(message))
    }
}