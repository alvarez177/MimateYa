package com.salvarez.mimateyaandroid.feature.promos.domain

/** How a completed cut was charged. It is decided when the cut is completed and never changes. */
enum class CutCharge {
    PAID,
    FREE_BY_PUNCH_CARD,
    FROM_PACKAGE,
}

/**
 * A cut that was completed, and how it was charged. A client's history is the list of
 * these cuts. The balance is never stored: it is always calculated from that list,
 * in order (see [PromoEngine]).
 *
 * Reverting a cut completed by mistake deletes it and puts the appointment back
 * to scheduled, so the history only holds cuts that really happened.
 */
data class CompletedCut(val id: String, val charge: CutCharge)
