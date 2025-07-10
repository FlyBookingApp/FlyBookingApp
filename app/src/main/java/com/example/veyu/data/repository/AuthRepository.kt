package com.example.veyu.data.repository

import com.example.veyu.data.remote.api.AuthApi
import com.example.veyu.data.remote.model.Request.LoginRequest
import com.example.veyu.data.remote.model.Response.LoginResponse
import com.example.veyu.data.remote.model.Request.RegisterRequest
import com.example.veyu.data.remote.model.Request.ResetPasswordRequest
import com.example.veyu.data.remote.model.Request.SendOtpRequest
import com.example.veyu.data.remote.model.Request.VerifyOPTRequest
import com.example.veyu.data.remote.model.Response.RefreshTokenRequest
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

    suspend fun sendOtp(email: String): Result<String> {
        return try {
            val response = api.sendOtp(
                SendOtpRequest(
                    email = email
                )
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun verifyOtp(request: VerifyOPTRequest): Result<String> {
        return try {
            val response = api.verifyOtp(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun resetPassword(request: ResetPasswordRequest): Result<String> {
        return try {
            val response = api.resetPassword(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun refresh(refreshToken: String): Result<String> {
        return try {
            val response = api.refresh(RefreshTokenRequest(refreshToken))
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
