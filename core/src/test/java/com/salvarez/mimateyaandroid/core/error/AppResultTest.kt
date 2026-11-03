package com.salvarez.mimateyaandroid.core.error

import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

class AppResultTest {

    @Test
    fun `given a successful result when resolving the value then should return a success with the transformed value`() {
        val result: AppResult<Int> = AppResult.Success(2)

        val resolved = result.resolve { it * 10 }

        assertEquals(AppResult.Success(20), resolved)
    }

    @Test
    fun `given a failed result when resolving the value then should keep the same failure and not run the transform`() {
        val failure: AppResult<Int> = AppResult.Failure(AppError.Network)
        var transformRan = false

        val resolved = failure.resolve { transformRan = true; it * 10 }

        assertSame(failure, resolved)
        assertEquals(false, transformRan)
    }
}
