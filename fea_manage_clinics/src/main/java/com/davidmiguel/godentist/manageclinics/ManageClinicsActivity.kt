package com.davidmiguel.godentist.manageclinics

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.transaction
import com.davidmiguel.godentist.core.base.BaseActivity
import com.davidmiguel.godentist.manageclinics.clinics.ClinicsFragment

class ManageClinicsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_clinics)
        supportFragmentManager.findFragmentById(R.id.container)
            ?: replaceFragmentInActivity(ClinicsFragment.newInstance(), R.id.container)
    }

    /**
     * The `fragment` is added to the container view with id `frameId`. The operation is
     * performed by the `fragmentManager`.
     */
    fun AppCompatActivity.replaceFragmentInActivity(fragment: Fragment, frameId: Int) {
        supportFragmentManager.transaction {
            replace(frameId, fragment)
        }
    }
}
