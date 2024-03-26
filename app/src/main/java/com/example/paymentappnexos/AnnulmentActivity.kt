package com.example.paymentappnexos

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.paymentappnexos.adapters.TransactionAdapter
import com.example.paymentappnexos.db.TransactionDB
import com.example.paymentappnexos.models.AnnulmentTransaction
import com.example.paymentappnexos.models.TransactionsItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AnnulmentActivity : AppCompatActivity() {
    private lateinit var editTextReceiptId: EditText
    private lateinit var btnSearch: Button
    private lateinit var rvTransactions: RecyclerView
    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_annulment_transaction)

        editTextReceiptId   = findViewById(R.id.etAnnulmentReceiptId)
        btnSearch           = findViewById(R.id.btnAnnulmentSearch)
        rvTransactions      = findViewById(R.id.rvAnnulmentTransactions)

        btnSearch.setOnClickListener {
            val receiptId = editTextReceiptId.text.toString()

            if(receiptId.isNotEmpty()){
                searchTransaction(receiptId)
            }else{
                Toast.makeText(this, "Por favor, escribe un Número de recibo para buscar", Toast.LENGTH_SHORT).show()
            }

            // Set config to Recylcer view
            rvTransactions.layoutManager    = LinearLayoutManager(this)
            transactionAdapter              = TransactionAdapter()
            rvTransactions.adapter          = transactionAdapter
        }
    }

    private fun searchTransaction(receiptId: String){
        GlobalScope.launch(Dispatchers.IO) {
            val transactions = TransactionDB.getDatabase(this@AnnulmentActivity)
                .transactionDAO().getAnnulmentTransactions(receiptId)

            runOnUiThread {
                if(transactions.isEmpty()){
                    showEmptyTransactionsMessage()
                }else {
                    updateTransactionList(transactions)
                }
            }
        }
    }

    private fun showEmptyTransactionsMessage() {
        Toast.makeText(this@AnnulmentActivity, "No hay transacciones para mostrar", Toast.LENGTH_SHORT).show()
    }


    private fun updateTransactionList(transactions: List<AnnulmentTransaction>) {
        val transactionsItems = transactions.map { TransactionsItem.AnnulmentTransactionItem(it) }
        transactionAdapter.submitList(transactionsItems)
    }
}