package com.salvarez.mimateyaandroid.feature.promos.domain

import com.salvarez.mimateyaandroid.core.error.AppError
import com.salvarez.mimateyaandroid.core.error.AppResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Reverting twice (V2) is covered by the repository contract: deleting a cut that
 * no longer exists is a success, so the use case just returns that success.
 */
class RevertCutUseCaseTest {

    private val completedCutRepository = mockk<CompletedCutRepository>()
    private val revertCut = RevertCutDataUseCase(completedCutRepository)

    @Test
    fun `given a completed cut when reverting it then should delete the cut with the fixed id`() = runTest {
        coEvery { completedCutRepository.deleteCompletedCut("appointment-1-cut") } returns AppResult.Success(Unit)

        val result = revertCut("appointment-1")

        assertEquals(AppResult.Success(Unit), result)
        coVerify(exactly = 1) { completedCutRepository.deleteCompletedCut("appointment-1-cut") }
    }

    @Test
    fun `given deleting the cut fails when reverting it then should return that error`() = runTest {
        coEvery { completedCutRepository.deleteCompletedCut("appointment-1-cut") } returns AppResult.Failure(AppError.Network)

        val result = revertCut("appointment-1")

        assertEquals(AppResult.Failure(AppError.Network), result)
    }
}
