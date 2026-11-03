package com.salvarez.mimateyaandroid.core.error

import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

/** The only place where exceptions become [AppError]s. */
interface ErrorMapper {
    fun map(throwable: Throwable): AppError
}

/**
 * Maps plain Java/Kotlin exceptions. Firebase exceptions are added in the Firebase setup step
 * by wrapping this mapper, so `:core` never depends on Firebase.
 */
class DefaultErrorMapper : ErrorMapper {
    override fun map(throwable: Throwable): AppError = when (throwable) {
        // SocketTimeoutException and UnknownHostException are IOExceptions too.
        is IOException -> AppError.Network
        else -> AppError.Unknown(throwable)
    }
}

/**
 * Runs [block] and wraps the outcome in an [AppResult]. A [CancellationException] is rethrown
 * on purpose: swallowing it would break coroutine cancellation.
 */
inline fun <T> appResultOf(mapper: ErrorMapper, block: () -> T): AppResult<T> =
    try {
        AppResult.Success(block())
    } catch (cancellation: CancellationException) {
        throw cancellation
    } catch (throwable: Throwable) {
        AppResult.Failure(mapper.map(throwable))
    }
