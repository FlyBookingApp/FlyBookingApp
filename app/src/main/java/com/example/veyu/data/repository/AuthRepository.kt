package com.example.veyu.data.repository

import com.example.veyu.data.remote.api.AuthApi
import com.example.veyu.data.remote.model.Request.LoginRequest
import com.example.veyu.data.remote.model.Response.LoginResponse
import com.example.veyu.data.remote.model.Request.RegisterRequest
import com.example.veyu.data.remote.model.Response.RegisterResponse

class AuthRepository(private val api: AuthApi) {

    suspend fun login(request: LoginRequest): Result<LoginResponse> {
        return try {
            val response = api.login(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(request: RegisterRequest) : Result<RegisterResponse> {
        return try {
            val response = api.register(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
