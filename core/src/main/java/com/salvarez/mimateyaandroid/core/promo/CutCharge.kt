package com.salvarez.mimateyaandroid.core.promo

/**
 * How a completed cut was charged. It is decided when the cut is completed and never changes.
 * It lives in :core because the agenda reads it (for example to show "Corte gratis").
 */
enum class CutCharge {
    PAID,
    FREE_BY_PUNCH_CARD,
    FROM_PACKAGE,
}
