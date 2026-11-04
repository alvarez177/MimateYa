package com.salvarez.mimateyaandroid.feature.promos.domain

import com.salvarez.mimateyaandroid.core.error.AppError
import com.salvarez.mimateyaandroid.core.error.AppResult
import com.salvarez.mimateyaandroid.core.promo.CutCharge
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class CompleteCutUseCaseTest {

    private val completedCutRepository = mockk<CompletedCutRepository>()
    private val clientPromoRepository = mockk<ClientPromoRepository>()
    private val completeCut = CompleteCutDataUseCase(completedCutRepository, clientPromoRepository, PromoEngine())

    private val appointmentId = "appointment-1"
    private val clientId = "client-1"
    private val cutId = "appointment-1-cut"
    private val punchCardOfTen = Promo.PunchCard(cutsForFreeCut = 10)

    private fun paidCuts(count: Int): List<CompletedCut> =
        (1..count).map { CompletedCut(id = "old-$it-cut", charge = CutCharge.PAID) }

    private fun givenNotCompletedYet(promo: Promo?, history: List<CompletedCut>) {
        coEvery { completedCutRepository.findCompletedCut(cutId) } returns AppResult.Success(null)
        coEvery { clientPromoRepository.getPromo(clientId) } returns AppResult.Success(promo)
        coEvery { completedCutRepository.getCompletedCuts(clientId) } returns AppResult.Success(history)
    }

    @Test
    fun `given a punch card without a free cut yet when completing the cut then should save it as paid with the fixed id`() = runTest {
        givenNotCompletedYet(punchCardOfTen, paidCuts(3))
        val expectedCut = CompletedCut(id = cutId, charge = CutCharge.PAID)
        coEvery { completedCutRepository.saveCompletedCut(clientId, expectedCut) } returns AppResult.Success(Unit)

        val result = completeCut(appointmentId, clientId)

        assertEquals(AppResult.Success(CutCharge.PAID), result)
        coVerify(exactly = 1) { completedCutRepository.saveCompletedCut(clientId, expectedCut) }
    }

    @Test
    fun `given a punch card with a free cut available when completing the cut then should save it as free`() = runTest {
        givenNotCompletedYet(punchCardOfTen, paidCuts(10))
        val expectedCut = CompletedCut(id = cutId, charge = CutCharge.FREE_BY_PUNCH_CARD)
        coEvery { completedCutRepository.saveCompletedCut(clientId, expectedCut) } returns AppResult.Success(Unit)

        val result = completeCut(appointmentId, clientId)

        assertEquals(AppResult.Success(CutCharge.FREE_BY_PUNCH_CARD), result)
        coVerify(exactly = 1) { completedCutRepository.saveCompletedCut(clientId, expectedCut) }
    }

    @Test
    fun `given a package with cuts left when completing the cut then should save it as taken from the package`() = runTest {
        givenNotCompletedYet(Promo.PrepaidPackage(includedCuts = 10), emptyList())
        val expectedCut = CompletedCut(id = cutId, charge = CutCharge.FROM_PACKAGE)
        coEvery { completedCutRepository.saveCompletedCut(clientId, expectedCut) } returns AppResult.Success(Unit)

        val result = completeCut(appointmentId, clientId)

        assertEquals(AppResult.Success(CutCharge.FROM_PACKAGE), result)
        coVerify(exactly = 1) { completedCutRepository.saveCompletedCut(clientId, expectedCut) }
    }

    @Test
    fun `given the appointment already has its cut when completing it again then should return the original charge without saving another`() = runTest {
        val existingCut = CompletedCut(id = cutId, charge = CutCharge.FREE_BY_PUNCH_CARD)
        coEvery { completedCutRepository.findCompletedCut(cutId) } returns AppResult.Success(existingCut)

        val result = completeCut(appointmentId, clientId)

        assertEquals(AppResult.Success(CutCharge.FREE_BY_PUNCH_CARD), result)
        coVerify(exactly = 1) { completedCutRepository.findCompletedCut(cutId) }
        confirmVerified(completedCutRepository)
        verify { clientPromoRepository wasNot Called }
    }

    @Test
    fun `given checking for an existing cut fails when completing the cut then should fail without charging`() = runTest {
        coEvery { completedCutRepository.findCompletedCut(cutId) } returns AppResult.Failure(AppError.Network)

        val result = completeCut(appointmentId, clientId)

        assertEquals(AppResult.Failure(AppError.Network), result)
        coVerify(exactly = 1) { completedCutRepository.findCompletedCut(cutId) }
        confirmVerified(completedCutRepository)
        verify { clientPromoRepository wasNot Called }
    }

    @Test
    fun `given reading the client promo fails when completing the cut then should fail without saving`() = runTest {
        coEvery { completedCutRepository.findCompletedCut(cutId) } returns AppResult.Success(null)
        coEvery { clientPromoRepository.getPromo(clientId) } returns AppResult.Failure(AppError.Network)

        val result = completeCut(appointmentId, clientId)

        assertEquals(AppResult.Failure(AppError.Network), result)
        coVerify(exactly = 1) { completedCutRepository.findCompletedCut(cutId) }
        confirmVerified(completedCutRepository)
    }

    @Test
    fun `given reading the client history fails when completing the cut then should fail without saving`() = runTest {
        coEvery { completedCutRepository.findCompletedCut(cutId) } returns AppResult.Success(null)
        coEvery { clientPromoRepository.getPromo(clientId) } returns AppResult.Success(punchCardOfTen)
        coEvery { completedCutRepository.getCompletedCuts(clientId) } returns AppResult.Failure(AppError.Network)

        val result = completeCut(appointmentId, clientId)

        assertEquals(AppResult.Failure(AppError.Network), result)
        coVerify(exactly = 1) { completedCutRepository.findCompletedCut(cutId) }
        coVerify(exactly = 1) { completedCutRepository.getCompletedCuts(clientId) }
        confirmVerified(completedCutRepository)
    }

    @Test
    fun `given saving the cut fails when completing the cut then should return that error`() = runTest {
        givenNotCompletedYet(promo = null, history = emptyList())
        val expectedCut = CompletedCut(id = cutId, charge = CutCharge.PAID)
        coEvery { completedCutRepository.saveCompletedCut(clientId, expectedCut) } returns AppResult.Failure(AppError.Network)

        val result = completeCut(appointmentId, clientId)

        assertEquals(AppResult.Failure(AppError.Network), result)
    }
}
