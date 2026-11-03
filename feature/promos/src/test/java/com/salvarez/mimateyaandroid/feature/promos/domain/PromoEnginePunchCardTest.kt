package com.salvarez.mimateyaandroid.feature.promos.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class PromoEnginePunchCardTest {

    private val engine = PromoEngine()
    private val punchCardOfTen = Promo.PunchCard(cutsForFreeCut = 10)

    private fun paidCuts(count: Int, startAt: Int = 1): List<CompletedCut> =
        (startAt until startAt + count).map { CompletedCut(id = "cut-$it", charge = CutCharge.PAID) }

    @Test
    fun `given a punch card with 4 paid cuts when checking the status then should show 4 of 10 without a free cut`() {
        val status = engine.status(punchCardOfTen, paidCuts(4))

        assertEquals(PromoStatus.PunchCard(paidCuts = 4, cutsForFreeCut = 10, hasFreeCutAvailable = false), status)
    }

    @Test
    fun `given a punch card with 9 paid cuts when checking the next cut then should be paid`() {
        assertEquals(CutCharge.PAID, engine.nextCutCharge(punchCardOfTen, paidCuts(9)))
    }

    @Test
    fun `given a punch card with 10 paid cuts when checking the next cut then should be free`() {
        assertEquals(CutCharge.FREE_BY_PUNCH_CARD, engine.nextCutCharge(punchCardOfTen, paidCuts(10)))
    }

    @Test
    fun `given a punch card with 10 paid cuts when checking the status then should have a free cut available`() {
        val status = engine.status(punchCardOfTen, paidCuts(10))

        assertEquals(PromoStatus.PunchCard(paidCuts = 10, cutsForFreeCut = 10, hasFreeCutAvailable = true), status)
    }

    @Test
    fun `given a used free cut when checking the status then should restart the count at 0`() {
        val completedCuts = paidCuts(10) + CompletedCut(id = "cut-11", charge = CutCharge.FREE_BY_PUNCH_CARD)

        val status = engine.status(punchCardOfTen, completedCuts)

        assertEquals(PromoStatus.PunchCard(paidCuts = 0, cutsForFreeCut = 10, hasFreeCutAvailable = false), status)
    }

    @Test
    fun `given a used free cut followed by one paid cut when checking the status then should count only that paid cut`() {
        val completedCuts = paidCuts(10) +
            CompletedCut(id = "cut-11", charge = CutCharge.FREE_BY_PUNCH_CARD) +
            paidCuts(1, startAt = 12)

        val status = engine.status(punchCardOfTen, completedCuts)

        assertEquals(PromoStatus.PunchCard(paidCuts = 1, cutsForFreeCut = 10, hasFreeCutAvailable = false), status)
    }

    @Test
    fun `given a pending free cut when more cuts are checked later then should still be free because it never expires`() {
        val completedCuts = paidCuts(10)

        assertEquals(CutCharge.FREE_BY_PUNCH_CARD, engine.nextCutCharge(punchCardOfTen, completedCuts))
        assertEquals(CutCharge.FREE_BY_PUNCH_CARD, engine.nextCutCharge(punchCardOfTen, completedCuts))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `given a punch card that needs 0 cuts when creating it then should be rejected`() {
        Promo.PunchCard(cutsForFreeCut = 0)
    }
}
