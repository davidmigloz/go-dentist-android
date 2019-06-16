package com.davidmiguel.godentist.dashboard

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.davidmiguel.godentist.core.R as RC
import com.davidmiguel.godentist.core.base.BaseFragment
import com.davidmiguel.godentist.dashboard.databinding.DashboardFragmentBinding
import com.davidmiguel.godentist.requireMainActivity
import com.google.android.material.bottomappbar.BottomAppBar


class DashboardFragment : BaseFragment() {

    private lateinit var binding: DashboardFragmentBinding
    private lateinit var viewModel: DashboardViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dashboard_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        requireMainActivity().showExtendedFAB(RC.drawable.ic_add_black_24dp, "Add workday") {
            Toast.makeText(context, "Clicked FAB", Toast.LENGTH_SHORT).show()
        }
    }

}
