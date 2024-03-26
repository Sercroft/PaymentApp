package com.example.paymentappnexos.`interface`

import com.example.paymentappnexos.models.AnnulmentTransactionRequest
import com.example.paymentappnexos.models.AnnulmentTransactionResponse
import com.example.paymentappnexos.models.TransactionRequest
import com.example.paymentappnexos.models.TransactionResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("authorization")
    suspend fun authorizeTransaction(@Body request: TransactionRequest): Response<TransactionResponse>

    @POST("annulment")
    suspend fun annulmentTransaction(@Body request: AnnulmentTransactionRequest): Response<AnnulmentTransactionResponse>
}