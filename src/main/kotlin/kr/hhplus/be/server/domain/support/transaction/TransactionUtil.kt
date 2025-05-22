package kr.hhplus.be.server.domain.support.transaction

import org.springframework.transaction.support.TransactionSynchronization
import org.springframework.transaction.support.TransactionSynchronizationManager

fun hasTransaction(): Boolean =
    TransactionSynchronizationManager.isActualTransactionActive()

fun registerAfterTransactionCompletion(callback: () -> Unit) =
    TransactionSynchronizationManager.registerSynchronization(object : TransactionSynchronization {
        override fun afterCompletion(status: Int) {
            super.afterCompletion(status)
            callback()
        }
    })