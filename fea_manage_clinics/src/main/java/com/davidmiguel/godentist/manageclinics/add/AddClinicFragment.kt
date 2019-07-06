package com.davidmiguel.godentist.manageclinics.add

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.davidmiguel.godentist.core.base.AuthenticatedFragment
import com.davidmiguel.godentist.manageclinics.R
import com.davidmiguel.godentist.core.R as RC
import com.davidmiguel.godentist.manageclinics.ViewModelFactory
import com.davidmiguel.godentist.manageclinics.databinding.FragmentAddClinicBinding
import com.davidmiguel.godentist.requireMainActivity
import com.google.android.material.bottomappbar.BottomAppBar

class AddClinicFragment : AuthenticatedFragment() {

    private lateinit var binding: FragmentAddClinicBinding
    private val addClinicViewModel: AddClinicViewModel by viewModels { ViewModelFactory.getInstance() }
    private var clinicId: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        DataBindingUtil.inflate<FragmentAddClinicBinding>(
            inflater, R.layout.fragment_add_clinic, container, false
        ).apply {
            binding = this
            lifecycleOwner = viewLifecycleOwner
            vm = addClinicViewModel
            return root
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModelListeners()
    }

    private fun setupViewModelListeners() {
        requireMainActivity().showFAB(RC.drawable.ic_done_black_24dp, BottomAppBar.FAB_ALIGNMENT_MODE_END) {
            addClinicViewModel.saveClinic()
        }
        binding.percentage.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addClinicViewModel.saveClinic()
            }
            false
        }
        addClinicViewModel.nameError.observe(viewLifecycleOwner, Observer { error ->
            binding.nameContainer.error = if (error) "Invalid name!" else null
        })
        addClinicViewModel.percentageError.observe(viewLifecycleOwner, Observer { error ->
            binding.percentageContainer.error = if (error) "Invalid percentage!" else null
        })
        addClinicViewModel.clinicUpdatedEvent.observe(viewLifecycleOwner, Observer {
            findNavController().popBackStack()
        })
        addClinicViewModel.snackbarEvent.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.run { requireMainActivity().showSnackbar(this) }
        })
    }

    override fun onResumeAuthenticated() {
        addClinicViewModel.start(clinicId)
    }
}
