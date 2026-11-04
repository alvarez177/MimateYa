package com.salvarez.mimateyaandroid.feature.promos.domain

import com.salvarez.mimateyaandroid.core.error.AppResult
import com.salvarez.mimateyaandroid.core.error.resolve
import com.salvarez.mimateyaandroid.core.promo.CompleteCutUseCase
import com.salvarez.mimateyaandroid.core.promo.CutCharge

/**
 * Charges a cut exactly once (see docs/comportamientos-promos.md, C1 to C4).
 * The charge is decided here, from the history, and saved with the cut so it never changes.
 */
class CompleteCutDataUseCase(
    private val completedCutRepository: CompletedCutRepository,
    private val clientPromoRepository: ClientPromoRepository,
    private val promoEngine: PromoEngine,
) : CompleteCutUseCase {

    override suspend fun invoke(appointmentId: String, clientId: String): AppResult<CutCharge> {
        val cutId = CompletedCut.idFor(appointmentId)

        val findResult = completedCutRepository.findCompletedCut(cutId)

        val existingCut = when (findResult) {
            is AppResult.Failure -> return findResult
            is AppResult.Success -> findResult.value
        }

        // Already completed (double tap): keep the original charge. Recalculating it would
        // count the first cut too and could, for example, turn a free cut into a paid one.
        if (existingCut != null) {
            return AppResult.Success(existingCut.charge)
        }

        val promoResult = clientPromoRepository.getPromo(clientId)
        val promo = when (promoResult) {
            is AppResult.Failure -> return promoResult
            is AppResult.Success -> promoResult.value
        }

        val historyResult = completedCutRepository.getCompletedCuts(clientId)
        val history = when (historyResult) {
            is AppResult.Failure -> return historyResult
            is AppResult.Success -> historyResult.value
        }

        val charge = promoEngine.nextCutCharge(promo, history)
        val cut = CompletedCut(id = cutId, charge = charge)

        val saveResult = completedCutRepository.saveCompletedCut(clientId, cut)
        return saveResult.resolve { charge }
    }
}
