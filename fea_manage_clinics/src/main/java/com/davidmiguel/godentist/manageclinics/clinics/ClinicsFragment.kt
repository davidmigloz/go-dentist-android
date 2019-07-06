package com.davidmiguel.godentist.manageclinics.clinics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.davidmiguel.godentist.core.auth.AuthViewModel
import com.davidmiguel.godentist.core.base.AuthenticatedFragment
import com.davidmiguel.godentist.core.base.BaseFragment
import com.davidmiguel.godentist.manageclinics.R
import com.davidmiguel.godentist.core.R as RC
import com.davidmiguel.godentist.manageclinics.ViewModelFactory
import com.davidmiguel.godentist.manageclinics.databinding.FragmentClinicsBinding
import com.davidmiguel.godentist.requireMainActivity
import com.google.android.material.bottomappbar.BottomAppBar

class ClinicsFragment : AuthenticatedFragment() {

    private lateinit var binding: FragmentClinicsBinding
    private val clinicsViewModel: ClinicsViewModel by viewModels { ViewModelFactory.getInstance() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_clinics, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        initContent()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.vm = clinicsViewModel
    }

    override fun onResumeAuthenticated() {
        clinicsViewModel.start()
        requireMainActivity().showFAB(RC.drawable.ic_add_black_24dp, BottomAppBar.FAB_ALIGNMENT_MODE_END) {
            findNavController().navigate(RC.id.add_clinic_fragment)
        }
    }

    private fun initContent() {
        val layoutManager = LinearLayoutManager(context)
        binding.clinicsList.layoutManager = layoutManager
        binding.clinicsList.hasFixedSize()
        binding.clinicsList.adapter = ClinicsAdapter()
        binding.clinicsList.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))
    }
}
