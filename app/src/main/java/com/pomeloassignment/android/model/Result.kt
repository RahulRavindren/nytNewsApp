package com.pomeloassignment.android.model

sealed class Result<out T> {
    data class Success<out T>(val value: T) : Result<T>()
    data class GenericError(val code: Int? = null, val error: Throwable? = null) : Result<Nothing>()
    object ApiError : Result<Nothing>()
}