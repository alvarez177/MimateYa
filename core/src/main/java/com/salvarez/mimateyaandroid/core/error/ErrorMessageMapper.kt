package com.salvarez.mimateyaandroid.core.error

import com.salvarez.mimateyaandroid.core.R

/** What to show for an error: a string resource id, resolved in the UI layer. */
data class ErrorMessage(val resId: Int)

/** The only place that decides which text the barber sees for each [AppError]. */
class ErrorMessageMapper {
    fun messageFor(error: AppError): ErrorMessage = ErrorMessage(
        when (error) {
            AppError.Network -> R.string.error_network
            AppError.InvalidCredentials -> R.string.error_invalid_credentials
            is AppError.Validation -> when (error.field) {
                ValidationField.EMAIL -> R.string.error_validation_email
                ValidationField.PASSWORD -> R.string.error_validation_password
                ValidationField.PHONE -> R.string.error_validation_phone
                ValidationField.NAME -> R.string.error_validation_name
            }
            AppError.NotFound -> R.string.error_not_found
            AppError.Conflict -> R.string.error_conflict
            is AppError.Unknown -> R.string.error_unknown
        },
    )
}
