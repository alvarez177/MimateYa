package com.salvarez.mimateyaandroid.core.dispatcher

import javax.inject.Qualifier

/**
 * Marks the [kotlinx.coroutines.CoroutineDispatcher] for disk and network work.
 * It is injected (never written as Dispatchers.IO inside the code) so tests can
 * replace it with a test dispatcher.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher
