package com.salvarez.mimateyaandroid.core.promo

import com.salvarez.mimateyaandroid.core.error.AppResult

/**
 * Reverts a cut completed by mistake: its completed cut is deleted, so the balance
 * goes back to how it was. Reverting a cut that is already gone is not an error.
 */
interface RevertCutUseCase {

    suspend operator fun invoke(appointmentId: String): AppResult<Unit>
}
