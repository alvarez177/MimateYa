package com.salvarez.mimateyaandroid.core.promo

import com.salvarez.mimateyaandroid.core.error.AppResult

/**
 * Completes the cut of an appointment and charges it exactly once.
 * It lives in :core so the agenda can call it without depending on :feature:promos,
 * which implements it.
 */
interface CompleteCutUseCase {

    /**
     * Returns how the cut was charged. Calling it again for the same appointment
     * does not charge twice: it returns the charge of the cut that already exists.
     */
    suspend operator fun invoke(appointmentId: String, clientId: String): AppResult<CutCharge>
}
