package com.example.paymentappnexos.models

data class TransactionRequest(
    val id: String,
    val commerceCode: String,
    val terminalCode: String,
    val amount: String,
    val card: String
)