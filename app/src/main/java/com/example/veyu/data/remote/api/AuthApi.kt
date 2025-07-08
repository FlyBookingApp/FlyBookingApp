package com.example.veyu.data.remote.api

import com.example.veyu.data.remote.model.Request.LoginRequest
import com.example.veyu.data.remote.model.Response.LoginResponse
import com.example.veyu.data.remote.model.Request.RegisterRequest
import com.example.veyu.data.remote.model.Request.ResetPasswordRequest
import com.example.veyu.data.remote.model.Request.SendOtpRequest
import com.example.veyu.data.remote.model.Request.VerifyOPTRequest
import com.example.veyu.data.remote.model.Response.RegisterResponse
import okhttp3.Request
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("api/v1/auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("api/v1/password-reset/send-otp")
    suspend fun sendOtp(@Body request: SendOtpRequest): String

    @POST("api/v1/password-reset/verify-otp")
    suspend fun verifyOtp(@Body request: VerifyOPTRequest): String

    @POST("api/v1/password-reset/reset")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): String

    @POST("api/v1/auth/refresh")
    suspend fun refresh(@Body refreshToken: String): String
}