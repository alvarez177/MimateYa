package com.salvarez.mimateyaandroid.feature.promos.domain

import com.salvarez.mimateyaandroid.core.promo.CutCharge

/**
 * Pure business rules for promos (see docs/comportamientos-promos.md).
 * It does not save anything: it reads a client's promo and history and answers
 * what the balance is and how the next cut will be charged.
 */
class PromoEngine {

    fun status(promo: Promo?, completedCuts: List<CompletedCut>): PromoStatus =
        when (promo) {
            null -> PromoStatus.NoPromo
            is Promo.PunchCard -> {
                val paidCuts = paidCutsSinceLastFreeCut(completedCuts)
                PromoStatus.PunchCard(
                    paidCuts = paidCuts,
                    cutsForFreeCut = promo.cutsForFreeCut,
                    hasFreeCutAvailable = paidCuts >= promo.cutsForFreeCut,
                )
            }
            is Promo.PrepaidPackage -> {
                val usedCuts = completedCuts.count { it.charge == CutCharge.FROM_PACKAGE }
                PromoStatus.PrepaidPackage(
                    remainingCuts = (promo.includedCuts - usedCuts).coerceAtLeast(0),
                    includedCuts = promo.includedCuts,
                )
            }
        }

    fun nextCutCharge(promo: Promo?, completedCuts: List<CompletedCut>): CutCharge =
        when (val status = status(promo, completedCuts)) {
            PromoStatus.NoPromo -> CutCharge.PAID
            is PromoStatus.PunchCard ->
                if (status.hasFreeCutAvailable) CutCharge.FREE_BY_PUNCH_CARD else CutCharge.PAID
            is PromoStatus.PrepaidPackage ->
                if (status.remainingCuts > 0) CutCharge.FROM_PACKAGE else CutCharge.PAID
        }

    /** A free cut restarts the count, and it does not count as a paid cut itself. */
    private fun paidCutsSinceLastFreeCut(cuts: List<CompletedCut>): Int {
        var paidCuts = 0
        for (cut in cuts) {
            when (cut.charge) {
                CutCharge.PAID -> paidCuts++
                CutCharge.FREE_BY_PUNCH_CARD -> paidCuts = 0
                CutCharge.FROM_PACKAGE -> Unit
            }
        }
        return paidCuts
    }
}
