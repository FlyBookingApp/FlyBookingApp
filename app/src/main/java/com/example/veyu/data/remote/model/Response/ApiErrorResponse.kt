package com.example.veyu.data.remote.model.Response

data class ApiErrorResponse(
    val path: String,
    val error: String,
    val message: String,
    val errors: Map<String, String>?, // Cho phép null nếu không có
    val timestamp: String,
    val status: Int
)