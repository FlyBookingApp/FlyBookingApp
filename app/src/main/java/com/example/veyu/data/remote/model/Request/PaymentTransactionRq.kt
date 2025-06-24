package com.example.veyu.data.remote.model.Request

data class PaymentTransactionRq(
    val bookingId: Long,
    val paymentMethodId: Long,
    val amount: Double,
    val transactionDate: String = "2025-06-23T05:53:40.052Z",
    val status: String = "string"
)