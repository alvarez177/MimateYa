package com.salvarez.mimateyaandroid.core.error

/** Outcome of an operation: a value or a typed [AppError]. Use cases return this instead of throwing. */
sealed interface AppResult<out T> {
    data class Success<out T>(val value: T) : AppResult<T>
    data class Failure(val error: AppError) : AppResult<Nothing>
}

/** Transforms the value of a [AppResult.Success]; a [AppResult.Failure] is returned untouched. */
inline fun <T, R> AppResult<T>.resolve(transform: (T) -> R): AppResult<R> = when (this) {
    is AppResult.Success -> AppResult.Success(transform(value))
    is AppResult.Failure -> this
}
