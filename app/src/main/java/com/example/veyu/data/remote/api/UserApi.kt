package com.example.veyu.data.remote.api

import com.example.veyu.data.remote.model.Response.UserResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {
    @GET("api/v1/users/username/{username}")
    suspend fun getUserByUsername(@Path("username") username: String): UserResponse
}
