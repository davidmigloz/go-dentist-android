package com.davidmiguel.godentist.manageworkdays.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidmiguel.godentist.core.R
import com.davidmiguel.godentist.core.data.workdays.WorkDaysRepository
import com.davidmiguel.godentist.core.model.WorkDay
import com.davidmiguel.godentist.core.utils.Event
import com.davidmiguel.godentist.core.utils.Failure
import com.davidmiguel.godentist.core.utils.ScreenState
import com.davidmiguel.godentist.core.utils.Success
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class AddWorkDayViewModel(
    private val firebaseAuth: FirebaseAuth,
    private val workDaysRepository: WorkDaysRepository
) : ViewModel() {

    private val _screenState = MutableLiveData(ScreenState.INITIAL)
    val screenState: LiveData<ScreenState>
        get() = _screenState

    private val _snackbarEvent = MutableLiveData<Event<Int>>()
    val snackbarEvent: LiveData<Event<Int>>
        get() = _snackbarEvent

    private val _workDayUpdatedEvent = MutableLiveData<Event<Unit>>()
    val workDayUpdatedEvent: LiveData<Event<Unit>>
        get() = _workDayUpdatedEvent

    private var workDayId: String? = null

    val duration = MutableLiveData<String>()
    private val _durationError = MutableLiveData(false)
    val durationError: LiveData<Boolean>
        get() = _durationError

    fun start(workDayId: String?) {
        if (_screenState.value != ScreenState.INITIAL) return
        if (workDayId != null) {
            this.workDayId = workDayId
            loadWorkDay(workDayId)
        } else {
            _screenState.value = ScreenState.DATA_LOADED
        }
    }

    private fun loadWorkDay(workDayId: String) {
        _screenState.value = ScreenState.LOADING_DATA
        viewModelScope.launch {
            workDaysRepository.get(firebaseAuth.uid!!, workDayId).let { res ->
                when (res) {
                    is Success -> onWorkDayLoaded(res.value)
                    is Failure -> onErrorLoadingWorkDay()
                }
            }
        }
    }

    private fun onWorkDayLoaded(workDay: WorkDay) {
        duration.value = (workDay.duration?.run { this / 60F } ?: 0).toString()
        _screenState.value = ScreenState.DATA_LOADED
    }

    private fun onErrorLoadingWorkDay() {
        _screenState.value = ScreenState.ERROR
    }

    fun saveWorkDay() {
        _durationError.value = false

        val currentId = workDayId ?: ""

        val currentDuration = duration.value?.toFloatOrNull()?.run { (this * 60).toInt() }
        if (currentDuration == null) {
            _durationError.value = true
            return
        }

        viewModelScope.launch {
            workDaysRepository.put(
                firebaseAuth.uid!!,
                WorkDay(
                    currentId,
                    duration = currentDuration
                )
            ).let { res ->
                when (res) {
                    is Success -> onWorkDaySaved()
                    is Failure -> onErrorSavingWorkDay()
                }
            }
        }
    }

    private fun onWorkDaySaved() {
        _workDayUpdatedEvent.value = Event(Unit)
        _snackbarEvent.value = Event(R.string.all_dataSaved)
    }

    private fun onErrorSavingWorkDay() {
        _snackbarEvent.value = Event(R.string.all_errSavingData)
    }
}
