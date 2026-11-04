package com.salvarez.mimateyaandroid.feature.promos.domain

import com.salvarez.mimateyaandroid.core.error.AppResult
import com.salvarez.mimateyaandroid.core.promo.RevertCutUseCase

/**
 * Deletes the cut of an appointment completed by mistake (see docs/comportamientos-promos.md,
 * V1 to V3). Putting the appointment back to scheduled in the same write comes in step 11.
 */
class RevertCutDataUseCase(
    private val completedCutRepository: CompletedCutRepository,
) : RevertCutUseCase {

    override suspend fun invoke(appointmentId: String): AppResult<Unit> {
        val cutId = CompletedCut.idFor(appointmentId)

        return completedCutRepository.deleteCompletedCut(cutId)
    }
}
