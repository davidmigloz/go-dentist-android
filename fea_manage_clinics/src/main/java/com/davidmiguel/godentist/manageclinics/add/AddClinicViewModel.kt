package com.davidmiguel.godentist.manageclinics.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidmiguel.godentist.core.R
import com.davidmiguel.godentist.core.data.clinics.ClinicsRepository
import com.davidmiguel.godentist.core.model.Clinic
import com.davidmiguel.godentist.core.utils.Event
import com.davidmiguel.godentist.core.utils.Failure
import com.davidmiguel.godentist.core.utils.ScreenState
import com.davidmiguel.godentist.core.utils.Success
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class AddClinicViewModel(
    private val firebaseAuth: FirebaseAuth,
    private val clinicsRepository: ClinicsRepository
) : ViewModel() {

    private val _screenState = MutableLiveData(ScreenState.INITIAL)
    val screenState: LiveData<ScreenState>
        get() = _screenState

    private val _snackbarEvent = MutableLiveData<Event<Int>>()
    val snackbarEvent: LiveData<Event<Int>>
        get() = _snackbarEvent

    private val _clinicUpdatedEvent = MutableLiveData<Event<Unit>>()
    val clinicUpdatedEvent: LiveData<Event<Unit>>
        get() = _clinicUpdatedEvent

    private var clinicId: String? = null

    val name = MutableLiveData<String>()
    private val _nameError = MutableLiveData(false)
    val nameError: LiveData<Boolean>
        get() = _nameError

    val percentage = MutableLiveData<String>()
    private val _percentageError = MutableLiveData(false)
    val percentageError: LiveData<Boolean>
        get() = _percentageError

    fun start(clinicId: String?) {
        if (_screenState.value != ScreenState.INITIAL) return
        if (clinicId != null) {
            this.clinicId = clinicId
            loadClinic(clinicId)
        } else {
            _screenState.value = ScreenState.DATA_LOADED
        }
    }

    private fun loadClinic(clinicId: String) {
        _screenState.value = ScreenState.LOADING_DATA
        viewModelScope.launch {
            clinicsRepository.get(firebaseAuth.uid!!, clinicId).let { res ->
                when (res) {
                    is Success -> onClinicLoaded(res.value)
                    is Failure -> onErrorLoadingClinic()
                }
            }
        }
    }

    private fun onClinicLoaded(clinic: Clinic) {
        name.value = clinic.name ?: ""
        percentage.value = clinic.percentage?.toString() ?: ""
        _screenState.value = ScreenState.DATA_LOADED
    }

    private fun onErrorLoadingClinic() {
        _screenState.value = ScreenState.ERROR
    }

    fun saveClinic() {
        viewModelScope.launch {
            _nameError.postValue(false)
            _percentageError.postValue(false)

            val currentId = clinicId ?: ""

            val currentName = name.value?.trim()
            if (currentName.isNullOrBlank()) {
                _nameError.postValue(true)
                return@launch
            }

            val currentPercentage = percentage.value?.toIntOrNull()
            if (currentPercentage == null) {
                _percentageError.postValue(true)
                return@launch
            }
            // Save clinic (optimistic/offline first)
            onClinicSaved()
            clinicsRepository.put(
                firebaseAuth.uid!!, Clinic(currentId, currentName, currentPercentage)
            )
        }
    }

    private fun onClinicSaved() {
        _clinicUpdatedEvent.postValue(Event(Unit))
        _snackbarEvent.postValue(Event(R.string.all_dataSaved))
    }
}
