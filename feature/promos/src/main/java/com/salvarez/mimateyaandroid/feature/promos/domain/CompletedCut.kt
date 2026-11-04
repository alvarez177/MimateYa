package com.salvarez.mimateyaandroid.feature.promos.domain

import com.salvarez.mimateyaandroid.core.promo.CutCharge

/**
 * A cut that was completed, and how it was charged. A client's history is the list of
 * these cuts. The balance is never stored: it is always calculated from that list,
 * in order (see [PromoEngine]).
 *
 * Reverting a cut completed by mistake deletes it and puts the appointment back
 * to scheduled, so the history only holds cuts that really happened.
 */
data class CompletedCut(val id: String, val charge: CutCharge) {

    companion object {
        /** Fixed id: an appointment has at most one completed cut, so completing twice cannot charge twice. */
        fun idFor(appointmentId: String): String = "$appointmentId-cut"
    }
}
