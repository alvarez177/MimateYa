package com.salvarez.mimateyaandroid.core.error

import com.salvarez.mimateyaandroid.core.R
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class ErrorMessageMapperTest {

    private val mapper = ErrorMessageMapper()

    @Test
    fun `given a network error when asking for its message then should use the offline message`() {
        assertEquals(R.string.error_network, mapper.messageFor(AppError.Network).resId)
    }

    @Test
    fun `given invalid credentials when asking for the message then should use the credentials message`() {
        assertEquals(R.string.error_invalid_credentials, mapper.messageFor(AppError.InvalidCredentials).resId)
    }

    @Test
    fun `given a not found error when asking for the message then should use the not found message`() {
        assertEquals(R.string.error_not_found, mapper.messageFor(AppError.NotFound).resId)
    }

    @Test
    fun `given a conflict error when asking for the message then should use the conflict message`() {
        assertEquals(R.string.error_conflict, mapper.messageFor(AppError.Conflict).resId)
    }

    @Test
    fun `given a validation error when asking for the message then should have one distinct message per field`() {
        assertEquals(R.string.error_validation_email, mapper.messageFor(AppError.Validation(ValidationField.EMAIL)).resId)
        assertEquals(R.string.error_validation_password, mapper.messageFor(AppError.Validation(ValidationField.PASSWORD)).resId)
        assertEquals(R.string.error_validation_phone, mapper.messageFor(AppError.Validation(ValidationField.PHONE)).resId)
        assertEquals(R.string.error_validation_name, mapper.messageFor(AppError.Validation(ValidationField.NAME)).resId)
    }

    @Test
    fun `given an unknown error when asking for the message then should be the same message whatever the cause`() {
        val withCause = mapper.messageFor(AppError.Unknown(IllegalStateException("secret detail")))
        val withoutCause = mapper.messageFor(AppError.Unknown())

        assertEquals(R.string.error_unknown, withCause.resId)
        assertEquals(withCause, withoutCause)
    }

    @Test
    fun `given different error types when asking for their messages then should never share the same message`() {
        val ids = listOf(
            AppError.Network, AppError.InvalidCredentials, AppError.NotFound,
            AppError.Conflict, AppError.Unknown(),
        ).map { mapper.messageFor(it).resId }

        assertEquals(ids.size, ids.toSet().size)
        assertNotEquals(0, ids.first())
    }
}
