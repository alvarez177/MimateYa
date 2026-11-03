package com.salvarez.mimateyaandroid.core.error

/**
 * Every failure the app can show or handle. Exceptions are converted into one of these
 * in a single place (see [ErrorMapper]); the rest of the code never sees raw exceptions.
 */
sealed interface AppError {
    /** No connection, or the request timed out. */
    data object Network : AppError

    /** Wrong email or password. */
    data object InvalidCredentials : AppError

    /** A user-provided value is not valid. */
    data class Validation(val field: ValidationField) : AppError

    /** The client or appointment does not exist (anymore). */
    data object NotFound : AppError

    /** The data already exists, for example a repeated phone number. */
    data object Conflict : AppError

    /** Anything unexpected. The [cause] is kept for logging and is never shown to the user. */
    data class Unknown(val cause: Throwable? = null) : AppError
}

enum class ValidationField { EMAIL, PASSWORD, PHONE, NAME }
