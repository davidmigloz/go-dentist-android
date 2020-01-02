package com.davidmiguel.godentist.manageclinics.clinics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.davidmiguel.godentist.core.base.AuthenticatedFragment
import com.davidmiguel.godentist.core.utils.observeEvent
import com.davidmiguel.godentist.manageclinics.R
import com.davidmiguel.godentist.manageclinics.ViewModelFactory
import com.davidmiguel.godentist.manageclinics.databinding.FragmentClinicsBinding
import com.davidmiguel.godentist.requireMainActivity
import com.google.android.material.bottomappbar.BottomAppBar
import com.davidmiguel.godentist.core.R as RC

class ClinicsFragment : AuthenticatedFragment() {

    private lateinit var binding: FragmentClinicsBinding
    private val clinicsViewModel: ClinicsViewModel by viewModels { ViewModelFactory.getInstance() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        DataBindingUtil.inflate<FragmentClinicsBinding>(
            inflater, R.layout.fragment_clinics, container, false
        ).apply {
            binding = this
            lifecycleOwner = viewLifecycleOwner
            vm = clinicsViewModel
            initContent()
            return root
        }
    }

    private fun initContent() {
        binding.clinicsList.layoutManager = LinearLayoutManager(context)
        binding.clinicsList.adapter = ClinicsAdapter()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModelListeners()
    }

    private fun setupViewModelListeners() {
        requireMainActivity().showFAB(
            RC.drawable.ic_add_black_24dp,
            BottomAppBar.FAB_ALIGNMENT_MODE_END
        ) {
            clinicsViewModel.addNewClinic()
        }
        clinicsViewModel.addClinicEvent.observeEvent(viewLifecycleOwner) {
            findNavController().navigate(RC.id.add_clinic_fragment)
        }
    }

    override fun onResumeAuthenticated() {
        clinicsViewModel.start()
    }
}
