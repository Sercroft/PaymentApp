package com.example.paymentappnexos.models

data class TransactionResponse(
    val receiptId: String,
    val rrn: String,
    val statusCode: String,
    val statusDescription: String
)