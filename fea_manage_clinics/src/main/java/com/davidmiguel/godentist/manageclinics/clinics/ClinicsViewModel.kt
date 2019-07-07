package com.davidmiguel.godentist.manageclinics.clinics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.davidmiguel.godentist.core.data.clinics.ClinicsRepository
import com.davidmiguel.godentist.core.model.Clinic
import com.davidmiguel.godentist.core.utils.Event
import com.davidmiguel.godentist.core.utils.Resource
import com.davidmiguel.godentist.core.utils.ScreenState
import com.google.firebase.auth.FirebaseAuth

class ClinicsViewModel(
    private val firebaseAuth: FirebaseAuth,
    private val clinicsRepository: ClinicsRepository
) : ViewModel() {

    private val _screenState = MutableLiveData(ScreenState.INITIAL)
    val screenState: LiveData<ScreenState>
        get() = _screenState

    private val _addClinicEvent = MutableLiveData<Event<Unit>>()
    val addClinicEvent: LiveData<Event<Unit>>
        get() = _addClinicEvent

    private val loadClinicsEvent = MutableLiveData<Event<Any>>()

    private val _clinics: LiveData<List<Clinic>> = Transformations.switchMap(loadClinicsEvent) {
        Transformations.map(clinicsRepository.getAll(firebaseAuth.uid!!)) { res ->
            when (res.state) {
                Resource.State.LOADING -> {
                    _screenState.value = ScreenState.LOADING_DATA
                    listOf()
                }
                Resource.State.SUCCESS -> {
                    _screenState.value = ScreenState.DATA_LOADED
                    res.value
                }
                Resource.State.FAILURE -> {
                    _screenState.value = ScreenState.ERROR
                    listOf()
                }
            }
        }
    }
    val clinics: LiveData<List<Clinic>>
        get() = _clinics

    fun start() {
        if (_screenState.value != ScreenState.INITIAL) return
        loadClinics()
    }

    private fun loadClinics() {
        loadClinicsEvent.value = Event(Unit)
    }

    fun addNewClinic() {
        _addClinicEvent.value = Event(Unit)
    }
}