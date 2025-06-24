package com.example.veyu.ui.screen.account

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.veyu.data.local.UserPreferences
import com.example.veyu.data.remote.model.Response.UserResponse
import com.example.veyu.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = UserRepository(application)
    private val userPreferences = UserPreferences(application)

    private val _userState = MutableStateFlow<UserResponse?>(null)
    val userState: StateFlow<UserResponse?> = _userState

    init {
        fetchUserFromLocalStore()
    }

    fun fetchUserFromLocalStore() {
        viewModelScope.launch {
            val name = userPreferences.userName.first()
            Log.d("ProfileViewModel", "fetchUserFromLocalStore: $name")
            if (!name.isNullOrEmpty()) {
                fetchUser(name)
            } else {
                // handle empty name, maybe navigate to login
            }
        }
    }

    private suspend fun fetchUser(username: String) {
        repository.getUserByUsername(username).onSuccess {
            _userState.value = it
        }.onFailure {
            // handle error
        }
    }

    suspend fun onLogoutClick() {
        userPreferences.clearToken()
    }
}
