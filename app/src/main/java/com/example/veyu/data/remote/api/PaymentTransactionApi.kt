package com.example.veyu.data.remote.api

import com.example.veyu.data.remote.model.Request.PaymentTransactionRq
import com.example.veyu.data.remote.model.Response.PaymentTransactionRp
import retrofit2.http.Body
import retrofit2.http.POST

interface PaymentTransactionApi {
    @POST("api/v1/payment-transactions/")
    suspend fun createPaymentTransaction(@Body request: PaymentTransactionRq): PaymentTransactionRp
}