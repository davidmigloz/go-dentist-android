package com.davidmiguel.godentist.manageclinics.clinics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.davidmiguel.godentist.core.data.clinics.ClinicsRepository
import com.davidmiguel.godentist.core.model.Clinic
import com.davidmiguel.godentist.core.utils.Event
import com.google.firebase.auth.FirebaseAuth

class ClinicsViewModel(
    private val firebaseAuth: FirebaseAuth,
    private val clinicsRepository: ClinicsRepository
) : ViewModel() {

    private var isDataLoaded = false
    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean>
        get() = _dataLoading

    private val _newClinicEvent = MutableLiveData<Event<Unit>>()
    val newClinicEvent: LiveData<Event<Unit>>
        get() = _newClinicEvent

    private val _clinics = MutableLiveData<List<Clinic>>().apply { value = emptyList() }
    val clinics: LiveData<List<Clinic>>
        get() = _clinics

    fun start() {
        if (isDataLoaded) return
        if (_dataLoading.value == true) return
        loadClinics()
    }

    private fun loadClinics() {
        _dataLoading.value = true
        clinicsRepository.getAll(firebaseAuth.uid!!)
            .addOnCompleteListener { task ->
                task.result?.run { onClinicsLoaded(this) } ?: onErrorLoadingClinic()
            }
    }

    private fun onClinicsLoaded(clinics: List<Clinic>) {
        _clinics.value = clinics
        _dataLoading.value = false
        isDataLoaded = true
    }

    private fun onErrorLoadingClinic() {
        _dataLoading.value = false
    }

    fun addNewClinic() {
        _newClinicEvent.value = Event(Unit)
    }
}