package com.davidmiguel.godentist.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.davidmiguel.godentist.core.base.BaseActivity
import com.davidmiguel.godentist.core.utils.*
import com.davidmiguel.godentist.login.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth

private const val RC_SIGN_IN = 1

class AuthActivity : BaseActivity() {

    private val providers = listOf(
        AuthUI.IdpConfig.EmailBuilder().build()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        if (FirebaseAuth.getInstance().currentUser == null) {
            startSignIn()
        } else {
            launchDashboardActivity()
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
        if (requestCode == RC_SIGN_IN) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val user = FirebaseAuth.getInstance().currentUser
                    user?.let {
                        launchDashboardActivity()
                    } ?: showSnackbar("No account found")
                }
                else -> {
                    val response = IdpResponse.fromResultIntent(data)
                    when {
                        // If response is null the user canceled the sign-in flow using the back button
                        response == null -> showSnackbar("Sign-in cancelled")
                        response.error?.errorCode == ErrorCodes.NO_NETWORK -> showSnackbar("No network")
                        else -> showSnackbar("Unknown error")
                    }
                }
            }
        }
    }

    private fun launchDashboardActivity() {
        startActivity(Activities.Dashboard, clearTask = true)
        finish()
    }
}
