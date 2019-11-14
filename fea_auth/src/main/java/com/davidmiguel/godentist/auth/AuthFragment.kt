package com.davidmiguel.godentist.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.davidmiguel.godentist.core.auth.AuthViewModel
import com.davidmiguel.godentist.core.base.BaseFragment
import com.davidmiguel.godentist.core.data.user.UserRepository
import com.davidmiguel.godentist.core.utils.showSnackbar
import com.davidmiguel.godentist.login.R
import com.davidmiguel.godentist.login.databinding.FragmentAuthBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.davidmiguel.godentist.core.R as RC

private const val RC_SIGN_IN = 1

class AuthFragment : BaseFragment() {

    private lateinit var binding: FragmentAuthBinding
    private val authViewModel: AuthViewModel by activityViewModels()

    private val providers by lazy {
        listOf(AuthUI.IdpConfig.EmailBuilder().setRequireName(false).build())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_auth, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            authViewModel.refuseAuthentication()
            // TODO in the future move to intro screen
            findNavController().popBackStack(RC.id.work_days_fragment, false)
        }

        authViewModel.authState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when (authenticationState) {
                AuthViewModel.AuthState.AUTHENTICATED -> {
                    val user = FirebaseAuth.getInstance().currentUser
                    user?.let {
                        checkUserState(it)
                    } ?: showSnackbar("No account found")
                }
            }
        })

        if (authViewModel.authState.value != AuthViewModel.AuthState.AUTHENTICATED) {
            startSignIn()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        onRedirectAfterSignIn(requestCode, resultCode, data)
    }

    /**
     * Launch the Firebase authentication activity.
     */
    private fun startSignIn() {
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )
    }

    /**
     * Method called when firebase authentication is done.
     * If result is OK, we create (if it doesn't exist) the user account in the Firestore cloud.
     * Then we redirect the user to our home activity.
     * If result is CANCELED, we show an error message to the user according to its error type.
     */
    private fun onRedirectAfterSignIn(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN && resultCode != Activity.RESULT_OK) {
            val response = IdpResponse.fromResultIntent(data)
            when {
                // If response is null the user canceled the sign-in flow using the back button
                response == null -> showSnackbar("Sign-in cancelled")
                response.error?.errorCode == ErrorCodes.NO_NETWORK -> showSnackbar("No network")
                else -> showSnackbar("Unknown error")
            }
        }
    }

    private fun checkUserState(user: FirebaseUser) {
        UserRepository().exists(user.uid)
            .addOnSuccessListener(requireActivity()) { userExists ->
                if (userExists) {
                    findNavController().popBackStack()
                } else {
                    // TODO go to onboarding
                }
            }
            .addOnFailureListener(requireActivity()) {
                showSnackbar("Unknown error")
            }
    }
}
