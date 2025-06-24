package com.example.veyu.data.remote.model.Request

data class RegisterRequest(
    val username: String,
    val password: String,
    val confirmPassword: String,
    val email: String,
    val phone: String,
    val firstName: String = "string",
    val lastName: String = "string",
    val facebookAccountId: Long? = 1073741824,
    val googleAccountId: Long? = 1073741824
)