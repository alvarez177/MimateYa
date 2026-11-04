package com.salvarez.mimateyaandroid.feature.promos.domain

import com.salvarez.mimateyaandroid.core.promo.CutCharge
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Reverting a cut deletes its movement from the history (and the appointment goes
 * back to scheduled). These tests check that the balance is right once it is gone.
 */
class PromoEngineReversalTest {

    private val engine = PromoEngine()
    private val punchCardOfTen = Promo.PunchCard(cutsForFreeCut = 10)
    private val packageOfTen = Promo.PrepaidPackage(includedCuts = 10)

    private fun cuts(count: Int, charge: CutCharge, startAt: Int = 1): List<CompletedCut> =
        (startAt until startAt + count).map { CompletedCut(id = "cut-$it", charge = charge) }

    private fun List<CompletedCut>.revert(id: String): List<CompletedCut> = filterNot { it.id == id }

    @Test
    fun `given a reverted paid cut on a punch card when checking the status then should be exactly as before that cut`() {
        val before = engine.status(punchCardOfTen, cuts(4, CutCharge.PAID))
        val afterRevert = cuts(5, CutCharge.PAID).revert("cut-5")

        assertEquals(before, engine.status(punchCardOfTen, afterRevert))
    }

    @Test
    fun `given a reverted package cut when checking the status then should give that cut back`() {
        val afterRevert = cuts(3, CutCharge.FROM_PACKAGE).revert("cut-3")

        assertEquals(PromoStatus.PrepaidPackage(remainingCuts = 8, includedCuts = 10), engine.status(packageOfTen, afterRevert))
    }

    @Test
    fun `given a reverted free cut when checking the next cut then should be free again`() {
        val afterRevert = (cuts(10, CutCharge.PAID) + cuts(1, CutCharge.FREE_BY_PUNCH_CARD, startAt = 11)).revert("cut-11")

        assertEquals(CutCharge.FREE_BY_PUNCH_CARD, engine.nextCutCharge(punchCardOfTen, afterRevert))
    }

    @Test
    fun `given an old paid cut reverted after a free cut when checking the status then should keep the free cut as already used`() {
        val afterRevert = (cuts(10, CutCharge.PAID) + cuts(1, CutCharge.FREE_BY_PUNCH_CARD, startAt = 11)).revert("cut-3")

        assertEquals(
            PromoStatus.PunchCard(paidCuts = 0, cutsForFreeCut = 10, hasFreeCutAvailable = false),
            engine.status(punchCardOfTen, afterRevert),
        )
    }

    @Test
    fun `given a client without a promo when checking the next cut then should be paid`() {
        assertEquals(CutCharge.PAID, engine.nextCutCharge(promo = null, completedCuts = cuts(3, CutCharge.PAID)))
    }

    @Test
    fun `given a client without a promo when checking the status then should have no promo`() {
        assertEquals(PromoStatus.NoPromo, engine.status(promo = null, completedCuts = emptyList()))
    }
}
