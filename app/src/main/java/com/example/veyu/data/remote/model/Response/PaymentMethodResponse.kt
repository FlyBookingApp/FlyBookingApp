package com.example.veyu.data.remote.model.Response

data class PaymentMethodResponse(
    val id: Long,
    val name: String,
    val type: String,
    val isActive: Boolean,
    val createdAt: String,
    val updatedAt: String
)