package com.example.veyu.data.repository

import com.example.veyu.data.remote.api.PaymentTransactionApi
import com.example.veyu.data.remote.model.Request.PaymentTransactionRq
import com.example.veyu.data.remote.model.Response.PaymentTransactionRp

class PaymentTrasactionRepository(private val paymentTrasactionApi: PaymentTransactionApi) {
    suspend fun createPaymentTransaction(request: PaymentTransactionRq): Result<PaymentTransactionRp> {
        return try {
            val result = paymentTrasactionApi.createPaymentTransaction(request)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}