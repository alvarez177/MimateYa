package com.salvarez.mimateyaandroid.core.error

import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.coroutines.cancellation.CancellationException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.fail
import org.junit.Test

class AppResultOfTest {

    private val mapper = mockk<ErrorMapper>()

    @Test
    fun `given a block that succeeds when running appResultOf then should return success and never use the mapper`() {
        val result = appResultOf(mapper) { 42 }

        assertEquals(AppResult.Success(42), result)
        verify { mapper wasNot Called }
    }

    @Test
    fun `given a block that throws when running appResultOf then should return a failure with the mapped error`() {
        val boom = RuntimeException("boom")
        every { mapper.map(boom) } returns AppError.Conflict

        val result = appResultOf(mapper) { throw boom }

        assertEquals(AppResult.Failure(AppError.Conflict), result)
        verify(exactly = 1) { mapper.map(boom) }
    }

    @Test
    fun `given a block that is cancelled when running appResultOf then should rethrow and never map the cancellation`() {
        val cancellation = CancellationException("scope cancelled")

        try {
            appResultOf(mapper) { throw cancellation }
            fail("CancellationException must propagate")
        } catch (e: CancellationException) {
            assertSame(cancellation, e)
        }
        verify { mapper wasNot Called }
    }
}
