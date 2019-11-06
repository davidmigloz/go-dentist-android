package com.davidmiguel.godentist.managetreatments.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidmiguel.godentist.core.R
import com.davidmiguel.godentist.core.data.treatments.TreatmentsRepository
import com.davidmiguel.godentist.core.model.Treatment
import com.davidmiguel.godentist.core.utils.Event
import com.davidmiguel.godentist.core.utils.Failure
import com.davidmiguel.godentist.core.utils.ScreenState
import com.davidmiguel.godentist.core.utils.Success
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class AddTreatmentViewModel(
    private val firebaseAuth: FirebaseAuth,
    private val treatmentsRepository: TreatmentsRepository
) : ViewModel() {

    private val _screenState = MutableLiveData(ScreenState.INITIAL)
    val screenState: LiveData<ScreenState>
        get() = _screenState

    private val _snackbarEvent = MutableLiveData<Event<Int>>()
    val snackbarEvent: LiveData<Event<Int>>
        get() = _snackbarEvent

    private val _treatmentUpdatedEvent = MutableLiveData<Event<Unit>>()
    val treatmentUpdatedEvent: LiveData<Event<Unit>>
        get() = _treatmentUpdatedEvent

    private var treatmentId: String? = null

    val name = MutableLiveData<String>()
    private val _nameError = MutableLiveData(false)
    val nameError: LiveData<Boolean>
        get() = _nameError

    fun start(treatmentId: String?) {
        if (_screenState.value != ScreenState.INITIAL) return
        if (treatmentId != null) {
            this.treatmentId = treatmentId
            loadTreatment(treatmentId)
        } else {
            _screenState.value = ScreenState.DATA_LOADED
        }
    }

    private fun loadTreatment(treatmentId: String) {
        _screenState.value = ScreenState.LOADING_DATA
        viewModelScope.launch {
            treatmentsRepository.get(firebaseAuth.uid!!, treatmentId).let { res ->
                when (res) {
                    is Success -> onTreatmentLoaded(res.value)
                    is Failure -> onErrorLoadingTreatment()
                }
            }
        }
    }

    private fun onTreatmentLoaded(treatment: Treatment) {
        name.value = treatment.name ?: ""
        _screenState.value = ScreenState.DATA_LOADED
    }

    private fun onErrorLoadingTreatment() {
        _screenState.value = ScreenState.ERROR
    }

    fun saveTreatment() {
        _nameError.value = false

        val currentId = treatmentId ?: ""

        val currentName = name.value
        if (currentName == null) {
            _nameError.value = true
            return
        }

        viewModelScope.launch {
            treatmentsRepository.put(
                firebaseAuth.uid!!,
                Treatment(
                    id = currentId,
                    name = currentName
                )
            ).let { res ->
                when (res) {
                    is Success -> onTreatmentSaved()
                    is Failure -> onErrorSavingTreatment()
                }
            }
        }
    }

    private fun onTreatmentSaved() {
        _treatmentUpdatedEvent.value = Event(Unit)
        _snackbarEvent.value = Event(R.string.all_dataSaved)
    }

    private fun onErrorSavingTreatment() {
        _snackbarEvent.value = Event(R.string.all_errSavingData)
    }
}
