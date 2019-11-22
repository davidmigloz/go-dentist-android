package com.davidmiguel.godentist.manageworkdays.workdays

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidmiguel.godentist.core.data.workdays.WorkDaysRepository
import com.davidmiguel.godentist.core.model.WorkDay
import com.davidmiguel.godentist.core.utils.Event
import com.davidmiguel.godentist.core.utils.Failure
import com.davidmiguel.godentist.core.utils.ScreenState
import com.davidmiguel.godentist.core.utils.Success
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class WorkDaysViewModel(
    private val firebaseAuth: FirebaseAuth,
    private val workDaysRepository: WorkDaysRepository
) : ViewModel() {

    private val _screenState = MutableLiveData<ScreenState>(ScreenState.INITIAL)
    val screenState: LiveData<ScreenState>
        get() = _screenState

    private val _addEditWorkDayEvent = MutableLiveData<Event<String>>()
    val addEditWorkDayEvent: LiveData<Event<String>>
        get() = _addEditWorkDayEvent

    private val _workDays: MutableLiveData<List<WorkDay>> = MutableLiveData(listOf())
    val workDays: LiveData<List<WorkDay>>
        get() = _workDays

    fun start() {
        if (_screenState.value != ScreenState.INITIAL) return
        loadWorkDays()
    }

    private fun loadWorkDays() {
        _screenState.value = ScreenState.LOADING_DATA
        workDaysRepository.getAll(firebaseAuth.uid!!)
            .onEach { res ->
                when (res) {
                    is Success -> onWorkDaysLoaded(res.value)
                    is Failure -> onErrorLoadingWorkDays()
                }
            }
            .launchIn(viewModelScope)
    }

    private fun onWorkDaysLoaded(workDays: List<WorkDay>) {
        viewModelScope.launch {
            _workDays.postValue(workDays.sortedDescending())
            _screenState.postValue(ScreenState.DATA_LOADED)
        }
    }

    private fun onErrorLoadingWorkDays() {
        _screenState.value = ScreenState.ERROR
    }

    fun addNewWorkDay() {
        _addEditWorkDayEvent.value = Event("")
    }

    fun editWorkDay(workDay: WorkDay) {
        _addEditWorkDayEvent.value = Event(workDay.id)
    }
}
