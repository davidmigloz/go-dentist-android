package com.davidmiguel.godentist.manageclinics.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.davidmiguel.godentist.core.data.clinics.ClinicsRepository
import com.davidmiguel.godentist.core.model.Clinic
import com.davidmiguel.godentist.core.utils.Event
import com.google.firebase.auth.FirebaseAuth

class AddClinicViewModel(
    private val firebaseAuth: FirebaseAuth,
    private val clinicsRepository: ClinicsRepository
) : ViewModel() {

    private var isDataLoaded = false
    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean>
        get() = _dataLoading

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarMessage: LiveData<Event<Int>>
        get() = _snackbarText

    private val _clinicUpdatedEvent = MutableLiveData<Event<Unit>>()
    val clinicUpdatedEvent: LiveData<Event<Unit>>
        get() = _clinicUpdatedEvent

    private var clinicId: String? = null
    val name = MutableLiveData<String>()


    fun start(clinicId: String?) {
        if (isDataLoaded) return
        if (_dataLoading.value == true) return
        clinicId?.run {
            this@AddClinicViewModel.clinicId = this
            loadClinic(this)
        }
    }

    private fun loadClinic(clinicId: String) {
        _dataLoading.value = true
        clinicsRepository.get(firebaseAuth.uid!!, clinicId)
            .addOnCompleteListener { task ->
                task.result?.run { onClinicLoaded(this) } ?: onErrorLoadingClinic()
            }
    }

    private fun onClinicLoaded(clinic: Clinic) {
        name.value = clinic.name

        _dataLoading.value = false
        isDataLoaded = true
    }

    private fun onErrorLoadingClinic() {
        _dataLoading.value = false
    }

    fun saveClinic() {
        val currentId = clinicId ?: ""
        val currentName = name.value
        if (currentName.isNullOrBlank()) {
            // Show error
            return
        }
        clinicsRepository.update(firebaseAuth.uid!!, Clinic(currentId, currentName))
            .addOnSuccessListener {
                _clinicUpdatedEvent.value = Event(Unit)
            }
            .addOnFailureListener {
                // TODO show error
            }
    }
}