package com.example.paymentappnexos

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.paymentappnexos.db.TransactionDB
import com.example.paymentappnexos.models.AnnulmentTransaction
import com.example.paymentappnexos.models.TransactionRequest
import com.example.paymentappnexos.models.TransactionResponse
import com.example.paymentappnexos.models.Transaction
import com.example.paymentappnexos.network.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import java.text.DecimalFormat
import java.util.UUID


class AuthorizationActivity : AppCompatActivity() {
    private lateinit var tvReceiptId: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)

        // Elements layout
        val editTextCard        = findViewById<EditText>(R.id.etCard)
        val editTextAmount      = findViewById<EditText>(R.id.etAmount)
        val btnAuthorization    = findViewById<Button>(R.id.btnAuthotization)
        tvReceiptId             = findViewById(R.id.tvAuthReceiptId)

        editTextAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                editTextAmount.removeTextChangedListener(this)

                try{
                    var originalString = s.toString()

                    originalString = originalString.replace(",", "")

                    if(originalString.isNotEmpty()){
                        val amount = originalString.toDouble()

                        // Get the integer part and the decimal part
                        val integerPart = (amount / 100).toInt()
                        val decimalPart = amount % 100
                        val integerFormatter = DecimalFormat("#,###") // Decimal format
                        val formattedInteger = integerFormatter.format(integerPart)

                        // Apply format
                        val decimalFormatter = DecimalFormat("00")
                        val formattedDecimal = decimalFormatter.format(decimalPart)
                        val formattedString = "$formattedInteger,$formattedDecimal"

                            // Setting the decimal format on edit text
                        editTextAmount.setText(formattedString)
                        editTextAmount.setSelection(editTextAmount.text.length)

                    }
                }catch (nfe: NumberFormatException) {
                    nfe.printStackTrace()
                }
                editTextAmount.addTextChangedListener(this)
            }
        })



        btnAuthorization.setOnClickListener {
            val amount = editTextAmount.text.toString()
            val card = editTextCard.text.toString()

            if(amount.isEmpty()){
                Toast.makeText(this@AuthorizationActivity, "Por favor, digita un monto para realizar la transacción", Toast.LENGTH_SHORT).show()
            }else{
                CoroutineScope(Dispatchers.IO).launch{
                    setDataAuth(amount, card)
                }
            }
        }
    }

    private suspend fun setDataAuth(valueAmount: String, valueCard: String){
       val request = TransactionRequest(
            id              = UUID.randomUUID().toString(),
            commerceCode    = "000123",
            terminalCode    = "000ABC",
            amount          = valueAmount,
            card            = valueCard
       )

        // Consume api
        authorizationTransaction(request)
    }


    private suspend fun authorizationTransaction(request: TransactionRequest){
        try {
            val response = RetrofitInstance.apiService.authorizeTransaction(request)
            handleTransaction(response, request)
        }catch(e: Exception){
            Log.e("ERROR", e.toString())
        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleTransaction(response: Response<TransactionResponse>, request: TransactionRequest){
        runOnUiThread {
            if(response.isSuccessful){
                val transactionResponse = response.body()
                val receiptId = transactionResponse?.receiptId

                // Set recepit id to Text view
                tvReceiptId.text    = "Número de recibo: ${transactionResponse?.receiptId}"

                Toast.makeText(this@AuthorizationActivity, transactionResponse?.statusDescription, Toast.LENGTH_SHORT).show()

                Log.d("API SUCCESS", transactionResponse.toString())
                Log.d("RECEIPT ID", receiptId.toString())

                val transaction = Transaction(
                    receiptId           = transactionResponse?.receiptId?: "",
                    commerceCode        = request.commerceCode,
                    statusDescription   = transactionResponse?.statusDescription?: "",
                    amount              = request.amount,
                    card                = request.card,
                )

                val annulmentTransaction = AnnulmentTransaction(
                    receiptId           = transactionResponse?.receiptId?: "",
                    rrn                 = transactionResponse?.rrn?: "",
                    statusCode          = transactionResponse?.statusCode?: "",
                    statusDescription   = transactionResponse?.statusDescription?: ""
                )

                insertTransaction(transaction, annulmentTransaction)
            }else{
                val errorResponse = response.errorBody()?.string()
                val jsonResponse = JSONObject(errorResponse)
                val statusDescription = jsonResponse.getString("statusDescription")

                Toast.makeText(this@AuthorizationActivity, statusDescription, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun insertTransaction(transaction: Transaction, annulmentTransaction: AnnulmentTransaction){
        CoroutineScope(Dispatchers.IO).launch{
            val transactionDAO = TransactionDB.getDatabase(applicationContext).transactionDAO()
            transactionDAO.insertTransaction(transaction)
            transactionDAO.insertAnnulmentTransaction(annulmentTransaction)
        }
    }
}