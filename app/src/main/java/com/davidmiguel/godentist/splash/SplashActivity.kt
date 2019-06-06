package com.davidmiguel.godentist.splash

import androidx.core.app.ActivityOptionsCompat
import com.davidmiguel.godentist.core.base.BaseActivity
import com.davidmiguel.godentist.core.data.user.UserRepository
import com.davidmiguel.godentist.core.utils.Activities
import com.davidmiguel.godentist.core.utils.AddressableActivity
import com.davidmiguel.godentist.core.utils.showSnackbar
import com.davidmiguel.godentist.core.utils.startActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : BaseActivity() {

    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            launchActivity(Activities.Auth)
            return
        }
        UserRepository().exists(user.uid)
            .addOnSuccessListener { userExists ->
                launchActivity(if (userExists) Activities.Dashboard else Activities.Onboarding)
            }
            .addOnFailureListener {
                showSnackbar("Unknown error")
            }
    }

    private fun launchActivity(activity: AddressableActivity) {
        startActivity(
            act = activity,
            bundle = ActivityOptionsCompat.makeCustomAnimation(
                this,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            ).toBundle(),
            clearTask = true
        )
    }
}