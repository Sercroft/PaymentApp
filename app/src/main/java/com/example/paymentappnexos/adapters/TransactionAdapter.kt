package com.example.paymentappnexos.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.paymentappnexos.R
import com.example.paymentappnexos.models.AnnulmentTransaction
import com.example.paymentappnexos.models.Transaction
import com.example.paymentappnexos.models.TransactionsItem

class TransactionAdapter : ListAdapter<TransactionsItem, TransactionAdapter.TransactionViewHolder>(TransactionCallBack()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        when (val transactionsItem = getItem(position)){
            is TransactionsItem.TransactionItem -> holder.bind(transactionsItem.transaction)
            is TransactionsItem.AnnulmentTransactionItem -> holder.bindAnnulment(transactionsItem.annulmentTransaction)
        }
    }

    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val tvReceiptId     : TextView = itemView.findViewById(R.id.tvReceiptId)
        private val tvRnn           : TextView = itemView.findViewById(R.id.tvRnn)
        private val tvStatus        : TextView = itemView.findViewById(R.id.tvStatus)
        private val tvCommerceCode  : TextView = itemView.findViewById(R.id.tvCommerceCode)
        private val tvAmount        : TextView = itemView.findViewById(R.id.tvAmount)
        private val tvCard          : TextView = itemView.findViewById(R.id.tvCard)


        private val ctx: Context = itemView.context

        @SuppressLint("SetTextI18n")
        fun bind(transaction: Transaction) {
            if(transaction.receiptId.isNotEmpty()){
                tvReceiptId.text    = "Número de recibo: " +transaction.receiptId
                tvStatus.text       = "Estado: " + transaction.statusDescription
                tvCommerceCode.text = "Código de Comercio: " + transaction.commerceCode
                tvAmount.text       = "Monto: $ " + transaction.amount
                tvCard.text         = "Número de tarjeta: " + transaction.card
            }
        }

        @SuppressLint("SetTextI18n")
        fun bindAnnulment(annulmentTransaction: AnnulmentTransaction){
            itemView.setOnClickListener(this)

            if(annulmentTransaction.receiptId.isNotEmpty()){
                showAnnulmentText()
                hideTransactionsText()

                tvReceiptId.text    = "Número de recibo: " + annulmentTransaction.receiptId
                tvRnn.text          = "Código rnn: " + annulmentTransaction.rrn
                tvStatus.text       = "Estado: " + annulmentTransaction.statusDescription
            }
        }

        private fun showAnnulmentText(){
            tvRnn.visibility            = View.VISIBLE
        }

        private fun hideTransactionsText(){
            tvCommerceCode.visibility   = View.GONE
            tvAmount.visibility         = View.GONE
            tvCard.visibility           = View.GONE
        }

        override fun onClick(view: View?){
            showCancelTransactionDialog(ctx)
        }

        private fun showCancelTransactionDialog(ctx: Context){
            val alertDialogBuilder = AlertDialog.Builder(ctx)

            // Build dialog!
            alertDialogBuilder.setTitle("Anular transacción")
            alertDialogBuilder.setMessage("¿Deseas anular esta transacción?")
            alertDialogBuilder.setPositiveButton("Sí") { dialog, _ ->
                annulmentTransaction()
                dialog.dismiss()
            }
            alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }

        private fun annulmentTransaction(){

            Toast.makeText(ctx, "Transacción anulada", Toast.LENGTH_SHORT).show()
        }
    }
}


class TransactionCallBack : DiffUtil.ItemCallback<TransactionsItem>() {
    override fun areItemsTheSame(oldItem: TransactionsItem, newItem: TransactionsItem): Boolean {
        return when {
            oldItem is TransactionsItem.TransactionItem && newItem is TransactionsItem.TransactionItem ->
                oldItem.transaction.id == newItem.transaction.id
            oldItem is TransactionsItem.AnnulmentTransactionItem && newItem is TransactionsItem.AnnulmentTransactionItem ->
                oldItem.annulmentTransaction.id == newItem.annulmentTransaction.id
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: TransactionsItem, newItem: TransactionsItem): Boolean {
        return oldItem == newItem
    }
}