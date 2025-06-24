package com.example.veyu.data.remote.api

import com.example.veyu.data.remote.model.Request.PaymentMethodRequest
import com.example.veyu.data.remote.model.Response.PaymentMethodResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PaymentMethodApi {
    @POST("api/v1/payment-methods/")
    suspend fun createdPaymentMethod(@Body request: PaymentMethodRequest): PaymentMethodResponse

    @GET("api/v1/payment-methods/{id}")
    suspend fun getPaymentMethodById(@Path("id") id: Long): PaymentMethodResponse
}