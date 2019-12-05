package com.davidmiguel.godentist.managetreatments.treatments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidmiguel.godentist.core.data.treatments.TreatmentsRepository
import com.davidmiguel.godentist.core.model.Treatment
import com.davidmiguel.godentist.core.utils.Event
import com.davidmiguel.godentist.core.utils.Failure
import com.davidmiguel.godentist.core.utils.ScreenState
import com.davidmiguel.godentist.core.utils.Success
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class TreatmentsViewModel(
    private val firebaseAuth: FirebaseAuth,
    private val treatmentsRepository: TreatmentsRepository
) : ViewModel() {

    private val _screenState = MutableLiveData<ScreenState>(ScreenState.INITIAL)
    val screenState: LiveData<ScreenState>
        get() = _screenState

    private val _addTreatmentEvent = MutableLiveData<Event<Unit>>()
    val addTreatmentEvent: LiveData<Event<Unit>>
        get() = _addTreatmentEvent

    private val _treatments: MutableLiveData<List<Treatment>> = MutableLiveData(listOf())
    val treatments: LiveData<List<Treatment>>
        get() = _treatments

    fun start() {
        if (_screenState.value != ScreenState.INITIAL) return
        loadTreatments()
    }

    private fun loadTreatments() {
        _screenState.value = ScreenState.LOADING_DATA
        treatmentsRepository.getAll(firebaseAuth.uid!!)
            .onEach { res ->
                when (res) {
                    is Success -> onTreatmentsLoaded(res.value)
                    is Failure -> onErrorLoadingTreatments()
                }
            }.launchIn(viewModelScope)
    }

    private fun onTreatmentsLoaded(treatments: List<Treatment>) {
        viewModelScope.launch {
            _treatments.postValue(treatments.sorted())
            _screenState.value = ScreenState.DATA_LOADED
        }
    }

    private fun onErrorLoadingTreatments() {
        _screenState.value = ScreenState.ERROR
    }

    fun addNewTreatment() {
        _addTreatmentEvent.value = Event(Unit)
    }
}
