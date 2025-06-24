package com.example.veyu.data.repository

import android.content.Context
import com.example.veyu.data.remote.NetworkModule
import com.example.veyu.data.remote.model.Response.UserResponse

class UserRepository(private val context: Context) {
    private val userApi = NetworkModule.provideUserApi(context)

    suspend fun getUserByUsername(username: String): Result<UserResponse> {
        return try {
            val user = userApi.getUserByUsername(username)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
