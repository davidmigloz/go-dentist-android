package com.davidmiguel.godentist.core.base

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.davidmiguel.godentist.core.R
import com.davidmiguel.godentist.core.auth.AuthViewModel

abstract class AuthenticatedFragment : BaseFragment() {

    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onResume() {
        super.onResume()
        authViewModel.authState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when (authenticationState) {
                AuthViewModel.AuthState.AUTHENTICATED -> onResumeAuthenticated()
                else -> findNavController().navigate(R.id.auth_fragment)
            }
        })
    }

    abstract fun onResumeAuthenticated()
}
