package com.example.veyu.data.remote.api

import com.example.veyu.data.remote.model.Request.LoginRequest
import com.example.veyu.data.remote.model.Response.LoginResponse
import com.example.veyu.data.remote.model.Request.RegisterRequest
import com.example.veyu.data.remote.model.Response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("api/v1/auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse
}