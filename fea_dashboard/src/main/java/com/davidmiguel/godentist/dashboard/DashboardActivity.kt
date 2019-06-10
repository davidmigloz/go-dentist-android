package com.davidmiguel.godentist.dashboard

import android.os.Bundle
import com.davidmiguel.godentist.core.base.BaseActivity
import com.davidmiguel.godentist.core.utils.Activities
import com.davidmiguel.godentist.core.utils.startActivity

class DashboardActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        startActivity(Activities.ManageClinics)
    }
}
