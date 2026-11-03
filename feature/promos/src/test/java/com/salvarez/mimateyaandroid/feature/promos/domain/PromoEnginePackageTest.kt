package com.salvarez.mimateyaandroid.feature.promos.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class PromoEnginePackageTest {

    private val engine = PromoEngine()
    private val packageOfTen = Promo.PrepaidPackage(includedCuts = 10)

    private fun packageCuts(count: Int): List<CompletedCut> =
        (1..count).map { CompletedCut(id = "cut-$it", charge = CutCharge.FROM_PACKAGE) }

    @Test
    fun `given a package just sold when checking the status then should have all 10 cuts left`() {
        val status = engine.status(packageOfTen, emptyList())

        assertEquals(PromoStatus.PrepaidPackage(remainingCuts = 10, includedCuts = 10), status)
    }

    @Test
    fun `given a package with 3 used cuts when checking the status then should have 7 cuts left`() {
        val status = engine.status(packageOfTen, packageCuts(3))

        assertEquals(PromoStatus.PrepaidPackage(remainingCuts = 7, includedCuts = 10), status)
    }

    @Test
    fun `given a package with cuts left when checking the next cut then should come from the package`() {
        assertEquals(CutCharge.FROM_PACKAGE, engine.nextCutCharge(packageOfTen, packageCuts(9)))
    }

    @Test
    fun `given an exhausted package when checking the next cut then should be paid`() {
        assertEquals(CutCharge.PAID, engine.nextCutCharge(packageOfTen, packageCuts(10)))
    }

    @Test
    fun `given an exhausted package and extra paid cuts when checking the status then should stay at 0 cuts left`() {
        val completedCuts = packageCuts(10) + CompletedCut(id = "cut-11", charge = CutCharge.PAID)

        val status = engine.status(packageOfTen, completedCuts)

        assertEquals(PromoStatus.PrepaidPackage(remainingCuts = 0, includedCuts = 10), status)
    }

    @Test
    fun `given more package cuts recorded than included when checking the status then should never show negative cuts left`() {
        val status = engine.status(packageOfTen, packageCuts(11))

        assertEquals(PromoStatus.PrepaidPackage(remainingCuts = 0, includedCuts = 10), status)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `given a package with 0 included cuts when creating it then should be rejected`() {
        Promo.PrepaidPackage(includedCuts = 0)
    }
}
