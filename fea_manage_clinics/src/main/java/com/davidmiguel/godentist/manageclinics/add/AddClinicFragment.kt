package com.davidmiguel.godentist.manageclinics.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.davidmiguel.godentist.manageclinics.R
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.davidmiguel.godentist.manageclinics.ViewModelFactory
import com.davidmiguel.godentist.manageclinics.clinics.ClinicsViewModel
import com.davidmiguel.godentist.manageclinics.databinding.FragmentAddClinicBinding


class AddClinicFragment : Fragment() {

    private lateinit var binding: FragmentAddClinicBinding

    private var clinicId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            clinicId = it.getString(ARG_CLINIC_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_clinic, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val vm = ViewModelProviders.of(this, ViewModelFactory.getInstance()).get(AddClinicViewModel::class.java)
        binding.vm = vm
        vm.start(clinicId)
        vm.clinicUpdatedEvent.observe(viewLifecycleOwner, Observer {

        })
    }

    companion object {

        private const val ARG_CLINIC_ID = "com.davidmiguel.godentist.manageclinics.add.ARG_CLINIC_ID"

        fun newInstance(clinicId: String? = null) =
            AddClinicFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CLINIC_ID, clinicId)
                }
            }
    }
}
