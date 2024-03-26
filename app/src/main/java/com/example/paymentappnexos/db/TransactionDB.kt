package com.example.paymentappnexos.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.paymentappnexos.dao.TransactionDAO
import com.example.paymentappnexos.models.AnnulmentTransaction
import com.example.paymentappnexos.models.Transaction

@Database(entities = [Transaction::class, AnnulmentTransaction::class], version = 2)
abstract class TransactionDB: RoomDatabase() {
    abstract fun transactionDAO(): TransactionDAO.TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: TransactionDB? = null

        fun getDatabase(context: Context): TransactionDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TransactionDB::class.java,
                    "transaction_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}