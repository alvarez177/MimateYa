package com.salvarez.mimateyaandroid.core.error

import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class DefaultErrorMapperTest {

    private val mapper = DefaultErrorMapper()

    @Test
    fun `given an IOException when mapping the error then should be Network`() {
        assertEquals(AppError.Network, mapper.map(IOException("socket closed")))
    }

    @Test
    fun `given a socket timeout when mapping the error then should be Network`() {
        assertEquals(AppError.Network, mapper.map(SocketTimeoutException()))
    }

    @Test
    fun `given an unknown host when mapping the error then should be Network`() {
        assertEquals(AppError.Network, mapper.map(UnknownHostException("firestore.googleapis.com")))
    }

    @Test
    fun `given an unexpected exception when mapping the error then should be Unknown and keep the cause`() {
        val cause = IllegalStateException("boom")

        val error = mapper.map(cause)

        assertTrue(error is AppError.Unknown)
        assertSame(cause, (error as AppError.Unknown).cause)
    }
}
