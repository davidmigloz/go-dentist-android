package com.davidmiguel.godentist.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.davidmiguel.godentist.core.R
import com.davidmiguel.godentist.core.auth.AuthViewModel

abstract class AuthenticatedFragment : BaseFragment() {

    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        authViewModel.authState.observe(viewLifecycleOwner, Observer { authenticationState ->
            if (authenticationState != AuthViewModel.AuthState.AUTHENTICATED) {
                findNavController().navigate(R.id.auth_fragment)
            }
        })
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        if(authViewModel.authState.value == AuthViewModel.AuthState.AUTHENTICATED) {
            onResumeAuthenticated()
        }
    }

    abstract fun onResumeAuthenticated()
}
