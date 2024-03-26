package com.example.paymentappnexos.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.paymentappnexos.models.AnnulmentTransaction
import com.example.paymentappnexos.models.Transaction

abstract class TransactionDAO {
    @Dao
    interface TransactionDao{
        @Insert
        suspend fun insertTransaction(transaction: Transaction)

        @Insert
        suspend fun insertAnnulmentTransaction(annulementTransaction: AnnulmentTransaction)

        @Query("SELECT * FROM 'transactions' WHERE receiptId = :receiptId AND statusDescription = 'Aprobada'")
        suspend fun getApprovedTransactions(receiptId: String, ): List<Transaction>

        @Query("SELECT * FROM 'annulment_transactions' WHERE receiptId = :receiptId")
        suspend fun getAnnulmentTransactions(receiptId: String, ): List<AnnulmentTransaction>

        @Query("SELECT * FROM 'transactions'")
        suspend fun getAllTransactions(): List<Transaction>
    }
}