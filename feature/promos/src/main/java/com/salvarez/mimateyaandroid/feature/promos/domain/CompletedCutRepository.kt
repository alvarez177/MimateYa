package com.salvarez.mimateyaandroid.feature.promos.domain

import com.salvarez.mimateyaandroid.core.error.AppResult

/** The history of completed cuts. Its implementation (Firestore) arrives in step 6. */
interface CompletedCutRepository {

    /** The cut with that [id], or null if there is none. */
    suspend fun findCompletedCut(id: String): AppResult<CompletedCut?>

    /** All the client's cuts, in the order they were completed (the punch card depends on it). */
    suspend fun getCompletedCuts(clientId: String): AppResult<List<CompletedCut>>

    suspend fun saveCompletedCut(clientId: String, cut: CompletedCut): AppResult<Unit>

    /** Deleting a cut that does not exist is a success, so reverting twice is not an error. */
    suspend fun deleteCompletedCut(id: String): AppResult<Unit>
}
