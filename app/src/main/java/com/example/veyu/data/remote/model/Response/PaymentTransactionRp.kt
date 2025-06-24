package com.example.veyu.data.remote.model.Response

data class PaymentTransactionRp(
    val id: Long,
    val transactionReference: String,
    val booking: BookingResponse,
    val paymentMethod: PaymentTransactionRp,
    val amount: Double,
    val currency: String,
    val status: String,
    val paymentDate: String,
    val paymentDetails: String?,
    val createdAt: String,
    val updatedAt: String
)