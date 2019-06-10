package com.davidmiguel.godentist.manageclinics.clinics

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.davidmiguel.godentist.core.base.BaseFragment

import com.davidmiguel.godentist.manageclinics.R
import com.davidmiguel.godentist.manageclinics.ViewModelFactory
import com.davidmiguel.godentist.manageclinics.add.AddClinicViewModel
import com.davidmiguel.godentist.manageclinics.databinding.FragmentClinicsBinding

class ClinicsFragment : BaseFragment() {

    private lateinit var binding: FragmentClinicsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_clinics, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        binding.clinicsList.layoutManager = layoutManager
        binding.clinicsList.hasFixedSize()
        binding.clinicsList.adapter = ClinicsAdapter()
        binding.clinicsList.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))
        val vm = ViewModelProviders.of(this, ViewModelFactory.getInstance()).get(ClinicsViewModel::class.java)
        binding.vm = vm
        vm.start()
    }

    companion object {
        fun newInstance() = ClinicsFragment()
    }
}
