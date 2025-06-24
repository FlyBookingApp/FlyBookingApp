package com.example.veyu.data.repository

import com.example.veyu.data.remote.api.PaymentMethodApi
import com.example.veyu.data.remote.api.SeatFlightApi
import com.example.veyu.data.remote.model.Request.PaymentMethodRequest
import com.example.veyu.data.remote.model.Response.PaymentMethodResponse

class PaymentMethodRepository(private val paymentMethodApi: PaymentMethodApi) {
    suspend fun createdPaymentMethod(request: PaymentMethodRequest): Result<PaymentMethodResponse> {
        return try {
            val result = paymentMethodApi.createdPaymentMethod(request)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPaymentMethodById(id: Long): Result<PaymentMethodResponse> {
        return try {
            val result = paymentMethodApi.getPaymentMethodById(id)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}