package com.example.paymentappnexos.models


sealed class TransactionsItem {
    data class TransactionItem(val transaction: Transaction) : TransactionsItem()
    data class AnnulmentTransactionItem(val annulmentTransaction: AnnulmentTransaction): TransactionsItem()
}
