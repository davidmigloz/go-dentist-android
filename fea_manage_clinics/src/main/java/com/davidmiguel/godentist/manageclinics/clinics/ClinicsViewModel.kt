package com.davidmiguel.godentist.manageclinics.clinics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidmiguel.godentist.core.data.clinics.ClinicsRepository
import com.davidmiguel.godentist.core.model.Clinic
import com.davidmiguel.godentist.core.utils.Event
import com.davidmiguel.godentist.core.utils.Resource
import com.davidmiguel.godentist.core.utils.ScreenState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

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

    private val _clinics: MutableLiveData<List<Clinic>> = MutableLiveData(listOf())

    val clinics: LiveData<List<Clinic>>
        get() = _clinics

    fun start() {
        if (_screenState.value != ScreenState.INITIAL) return
        loadClinics()
    }

    private fun loadClinics() {
        clinicsRepository.getAll(firebaseAuth.uid!!)
            .onEach { res ->
                when (res.state) {
                    Resource.State.LOADING -> {
                        _screenState.value = ScreenState.LOADING_DATA
                        _clinics.value = listOf()
                    }
                    Resource.State.SUCCESS -> {
                        _screenState.value = ScreenState.DATA_LOADED
                        _clinics.value = res.value
                    }
                    Resource.State.FAILURE -> {
                        _screenState.value = ScreenState.ERROR
                        _clinics.value = listOf()
                    }
                }
            }
            .catch {
                _screenState.value = ScreenState.ERROR
                _clinics.value = listOf()
            }
            .launchIn(viewModelScope)
    }

    fun addNewClinic() {
        _addClinicEvent.value = Event(Unit)
    }
}