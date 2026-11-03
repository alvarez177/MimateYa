package com.salvarez.mimateyaandroid.feature.promos.domain

/** The single promo a client can have at a time. */
sealed interface Promo {

    /** Tiquetera: after [cutsForFreeCut] paid cuts, the next cut is free. */
    data class PunchCard(val cutsForFreeCut: Int) : Promo {
        init {
            require(cutsForFreeCut > 0) { "A punch card needs at least 1 paid cut, was $cutsForFreeCut" }
        }
    }

    /** Paquete prepago: [includedCuts] cuts already paid for. */
    data class PrepaidPackage(val includedCuts: Int) : Promo {
        init {
            require(includedCuts > 0) { "A package needs at least 1 cut, was $includedCuts" }
        }
    }
}
