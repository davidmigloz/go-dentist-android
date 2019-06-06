package com.davidmiguel.godentist.onboarding

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.davidmiguel.godentist.core.base.BaseActivity
import com.davidmiguel.godentist.core.data.user.UserRepository
import com.davidmiguel.godentist.core.model.User
import com.davidmiguel.godentist.core.utils.Activities
import com.davidmiguel.godentist.core.utils.showSnackbar
import com.davidmiguel.godentist.core.utils.startActivity
import com.davidmiguel.godentist.onboarding.databinding.ActivityOnboardingBinding
import com.google.firebase.auth.FirebaseAuth

class OnboardingActivity : BaseActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_onboarding)

        val authUser = FirebaseAuth.getInstance().currentUser
        if(authUser == null) {
            finish()
            return
        }

        binding.btnNext.setOnClickListener {
            val user = User().apply {
                uid = authUser.uid
                name = binding.name.text.toString()
            }
            UserRepository().update(user)
                .addOnSuccessListener {
                    startActivity(Activities.Dashboard, clearTask = true)
                    finish()
                }
                .addOnFailureListener {
                    showSnackbar("Unknown error!")
                }
        }
    }
}
