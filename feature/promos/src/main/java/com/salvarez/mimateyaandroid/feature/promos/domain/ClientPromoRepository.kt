package com.salvarez.mimateyaandroid.feature.promos.domain

import com.salvarez.mimateyaandroid.core.error.AppResult

/** The promo assigned to each client. Its implementation (Firestore) arrives in step 6. */
interface ClientPromoRepository {

    /** The client's single promo, or null if the client has none. */
    suspend fun getPromo(clientId: String): AppResult<Promo?>
}
