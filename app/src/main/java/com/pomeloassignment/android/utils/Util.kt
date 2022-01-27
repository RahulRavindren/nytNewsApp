package com.pomeloassignment.android

import com.pomeloassignment.android.model.Result
import java.io.IOException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException


//enclose a wrapper around api calls for safe error handling
suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    apiCall: suspend () -> T
): Result<T> {
    return withContext(dispatcher) {
        try {
            Result.Success(apiCall.invoke())
        } catch (e: Throwable) {
            when (e) {
                is IOException -> Result.ApiError
                is HttpException -> {
                    val code = e.code()
                    //add for 401 exception to propogate as a customexception
                    Result.GenericError(code, e)
                }
                else -> Result.GenericError(null, e)
            }
        }
    }
}

//reference - https://stackoverflow.com/a/57252799
fun <T> debounce(
    waitMs: Long = 300L,
    coroutineScope: CoroutineScope,
    destinationFunction: (T) -> Unit
): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        debounceJob?.cancel()
        debounceJob = coroutineScope.launch {
            delay(waitMs)
            destinationFunction(param)
        }
    }
}