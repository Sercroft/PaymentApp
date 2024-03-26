package com.example.paymentappnexos.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val receiptId: String,
    val commerceCode: String,
    val statusDescription: String,
    val amount: String,
    val card: String
)


