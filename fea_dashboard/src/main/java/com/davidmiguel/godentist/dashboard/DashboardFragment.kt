package com.davidmiguel.godentist.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.davidmiguel.godentist.core.base.AuthenticatedFragment
import com.davidmiguel.godentist.dashboard.databinding.DashboardFragmentBinding
import com.davidmiguel.godentist.requireMainActivity
import com.davidmiguel.godentist.core.R as RC

class DashboardFragment : AuthenticatedFragment() {

    private lateinit var binding: DashboardFragmentBinding
    private val dashboardViewModel: DashboardViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dashboard_fragment, container, false)
        return binding.root
    }

    override fun onResumeAuthenticated() {
        requireMainActivity().showExtendedFAB(RC.drawable.ic_add_black_24dp, "Add workday") {
            Toast.makeText(context, "Clicked FAB", Toast.LENGTH_SHORT).show()
        }
    }
}
