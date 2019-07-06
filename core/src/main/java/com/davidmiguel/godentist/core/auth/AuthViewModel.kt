package com.davidmiguel.godentist.core.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {

    enum class AuthState {
        /** Initial state, the user needs to authenticate */
        AUTHENTICATED,
        /** The user has authenticated successfully */
        UNAUTHENTICATED
    }

    val authState = MutableLiveData<AuthState>()

    init {
        updateAuthState()
        FirebaseAuth.AuthStateListener { updateAuthState() }
    }

    private fun updateAuthState() {
        authState.value = if (isUserAuthenticated()) {
            AuthState.AUTHENTICATED
        } else {
            AuthState.UNAUTHENTICATED
        }
    }

    fun refuseAuthentication() {
        authState.value = AuthState.UNAUTHENTICATED
    }

    private fun isUserAuthenticated(): Boolean = FirebaseAuth.getInstance().currentUser != null
}