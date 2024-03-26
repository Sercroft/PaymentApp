package com.example.paymentappnexos.network

import com.example.paymentappnexos.`interface`.ApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object {
        private const val BASE_URL = "http://192.168.1.14:8080/api/payments/" // localhost

        private fun getRetrofitInstance(): Retrofit {
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Basic MDAwMTIzMDAwQUJD")
                    .build()
                chain.proceed(request)
            }

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
        }

        val apiService: ApiService by lazy {
            getRetrofitInstance().create(ApiService::class.java)
        }
    }
}
