package com.example.paymentappnexos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnOpenAuthTransaction          = findViewById<Button>(R.id.btnOpenAuthTransaction)
        val btnOpenSearchTransaction        = findViewById<Button>(R.id.btnOpenSearchTransaction)
        val btnOpenAllTransactions          = findViewById<Button>(R.id.btnOpenAllTransactions)
        val btnOpenAnnulmentTransaction     = findViewById<Button>(R.id.btnOpenAnnulmentTransaction)

        btnOpenAuthTransaction.setOnClickListener {
            openActivity(AuthorizationActivity::class.java)
        }

        btnOpenSearchTransaction.setOnClickListener {
            openActivity(SearchTransactionActivity::class.java)
        }

        btnOpenAllTransactions.setOnClickListener {
            openActivity(AllTransactionsActivity::class.java)
        }

        btnOpenAnnulmentTransaction.setOnClickListener {
            openActivity(AnnulmentActivity::class.java)
        }
    }

    private fun openActivity(activity: Class<*>){
        val intent = Intent(this, activity)
        startActivity(intent)
    }
}