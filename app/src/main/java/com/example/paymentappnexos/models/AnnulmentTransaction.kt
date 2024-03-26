package com.example.paymentappnexos.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "annulment_transactions")
data class AnnulmentTransaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val receiptId: String,
    val rrn: String,
    val statusCode: String,
    val statusDescription: String
)
