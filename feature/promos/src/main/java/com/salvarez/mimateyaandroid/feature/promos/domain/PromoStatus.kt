package com.salvarez.mimateyaandroid.feature.promos.domain

/** What the client's promo looks like right now. */
sealed interface PromoStatus {

    data object NoPromo : PromoStatus

    /** Shown as "[paidCuts] de [cutsForFreeCut]". */
    data class PunchCard(
        val paidCuts: Int,
        val cutsForFreeCut: Int,
        val hasFreeCutAvailable: Boolean,
    ) : PromoStatus

    data class PrepaidPackage(
        val remainingCuts: Int,
        val includedCuts: Int,
    ) : PromoStatus
}
