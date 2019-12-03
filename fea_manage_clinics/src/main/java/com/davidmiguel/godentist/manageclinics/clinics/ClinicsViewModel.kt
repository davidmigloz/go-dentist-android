package com.davidmiguel.godentist.manageclinics.clinics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidmiguel.godentist.core.data.clinics.ClinicsRepository
import com.davidmiguel.godentist.core.model.Clinic
import com.davidmiguel.godentist.core.utils.Event
import com.davidmiguel.godentist.core.utils.Failure
import com.davidmiguel.godentist.core.utils.ScreenState
import com.davidmiguel.godentist.core.utils.Success
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ClinicsViewModel(
    private val firebaseAuth: FirebaseAuth,
    private val clinicsRepository: ClinicsRepository
) : ViewModel() {

    private val _screenState = MutableLiveData<ScreenState>(ScreenState.INITIAL)
    val screenState: LiveData<ScreenState>
        get() = _screenState

    private val _addClinicEvent = MutableLiveData<Event<Unit>>()
    val addClinicEvent: LiveData<Event<Unit>>
        get() = _addClinicEvent

    private val _clinics: MutableLiveData<List<Clinic>> = MutableLiveData(listOf())
    val clinics: LiveData<List<Clinic>>
        get() = _clinics

    fun start() {
        if (_screenState.value != ScreenState.INITIAL) return
        loadClinics()
    }

    private fun loadClinics() {
        _screenState.value = ScreenState.LOADING_DATA
        clinicsRepository.getAll(firebaseAuth.uid!!)
            .onEach { res ->
                when (res) {
                    is Success -> onClinicsLoaded(res.value)
                    is Failure -> onErrorLoadingClinics()
                }
            }
            .launchIn(viewModelScope)
    }

    private fun onClinicsLoaded(clinics: List<Clinic>) {
        viewModelScope.launch {
            _clinics.postValue(clinics.sorted())
            _screenState.postValue(ScreenState.DATA_LOADED)
        }
    }

    private fun onErrorLoadingClinics() {
        _screenState.value = ScreenState.ERROR
    }

    fun addNewClinic() {
        _addClinicEvent.value = Event(Unit)
    }
}
