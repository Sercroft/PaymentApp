package com.example.paymentappnexos

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.paymentappnexos.adapters.TransactionAdapter
import com.example.paymentappnexos.db.TransactionDB
import com.example.paymentappnexos.models.Transaction
import com.example.paymentappnexos.models.TransactionsItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AllTransactionsActivity : AppCompatActivity() {

    private lateinit var rvAllTransactions  : RecyclerView
    private lateinit var transactionAdapter : TransactionAdapter
    private lateinit var tvNoTransactions   : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_transactions)

        rvAllTransactions = findViewById(R.id.rvAllTransactions)
        tvNoTransactions = findViewById(R.id.tvNoTransactions)

        // Set config to Recycler view
        rvAllTransactions.layoutManager = LinearLayoutManager(this)
        transactionAdapter = TransactionAdapter()
        rvAllTransactions.adapter = transactionAdapter

        getTransactions()
    }

    private fun getTransactions(){
        GlobalScope.launch(Dispatchers.IO) {
            val transactions = TransactionDB.getDatabase(this@AllTransactionsActivity)
                .transactionDAO().getAllTransactions()

            Log.e("RESPONSE", transactions.toString())

            runOnUiThread {
                if(transactions.isEmpty()){
                    showEmptyTransactionsMessage()
                }else{
                    showTransactions(transactions)
                }
            }
        }
    }

    private fun showEmptyTransactionsMessage() {
        tvNoTransactions.visibility = View.VISIBLE
        rvAllTransactions.visibility = View.GONE
    }

    private fun showTransactions(transactions: List<Transaction>) {
        tvNoTransactions.visibility = View.GONE
        rvAllTransactions.visibility = View.VISIBLE
        updateTransactionList(transactions)
    }

    private fun updateTransactionList(transactions: List<Transaction>) {
        val transactionsItems = transactions.map { TransactionsItem.TransactionItem(it) }
        transactionAdapter.submitList(transactionsItems)
    }
}