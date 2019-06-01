package com.davidmiguel.godentist.splash

import androidx.core.app.ActivityOptionsCompat
import com.davidmiguel.godentist.core.base.BaseActivity
import com.davidmiguel.godentist.core.utils.Activities
import com.davidmiguel.godentist.core.utils.startActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : BaseActivity() {

    override fun onStart() {
        super.onStart()
        startActivity(
            act = when {
                FirebaseAuth.getInstance().currentUser == null -> Activities.Auth
                else -> Activities.Dashboard
            },
            bundle = ActivityOptionsCompat.makeCustomAnimation(
                this,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            ).toBundle(),
            clearTask = true
        )
    }
}