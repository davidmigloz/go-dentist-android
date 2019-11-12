package com.davidmiguel.godentist.manageworkdays.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidmiguel.godentist.core.R
import com.davidmiguel.godentist.core.data.clinics.ClinicsRepository
import com.davidmiguel.godentist.core.data.workdays.WorkDaysRepository
import com.davidmiguel.godentist.core.model.Clinic
import com.davidmiguel.godentist.core.model.WorkDay
import com.davidmiguel.godentist.core.utils.*
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*

class AddWorkDayViewModel(
    private val firebaseAuth: FirebaseAuth,
    private val workDaysRepository: WorkDaysRepository,
    private val clinicsRepository: ClinicsRepository
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

    private val _clinics: MutableLiveData<List<Clinic>> = MutableLiveData(listOf())
    val clinics: LiveData<List<Clinic>>
        get() = _clinics

    private var workDayId: String? = null

    // Date
    val date = MutableLiveData<Long>(nowToEpochMilliUtc())
    private val _dateError = MutableLiveData(false)
    val dateError: LiveData<Boolean>
        get() = _dateError
    // Clinic
    val clinic = MutableLiveData<Clinic>()
    private val _clinicError = MutableLiveData(false)
    val clinicError: LiveData<Boolean>
        get() = _clinicError
    // Duration
    val duration = MutableLiveData<String>()
    private val _durationError = MutableLiveData(false)
    val durationError: LiveData<Boolean>
        get() = _durationError

    fun start(workDayId: String?) {
        if (_screenState.value != ScreenState.INITIAL) return
        loadData(workDayId)
    }

    private fun loadData(workDayId: String?) {
        viewModelScope.launch {
            _screenState.value = ScreenState.LOADING_DATA
            loadClinics()
            workDayId?.run {
                this@AddWorkDayViewModel.workDayId = this
                loadWorkDay(this)
            }
            _screenState.value = ScreenState.DATA_LOADED
        }
    }

    private suspend fun loadWorkDay(workDayId: String) {
        workDaysRepository.get(firebaseAuth.uid!!, workDayId).let { res ->
            when (res) {
                is Success -> {
                    val workDay = res.value
                    duration.value = (workDay.duration?.run { this / 60F } ?: 0).toString()
                }
                is Failure -> onErrorLoadingData()
            }
        }
    }

    private fun loadClinics() {
        clinicsRepository.getAll(firebaseAuth.uid!!)
            .onEach { res ->
                when (res) {
                    is Success -> {
                        _screenState.value = ScreenState.DATA_LOADED
                        _clinics.value = res.value
                        if (res.value.isNotEmpty() && clinic.value == null) {
                            clinic.value = res.value.first()
                        }
                    }
                    is Failure -> {
                        onErrorLoadingData()
                    }
                }
            }
            .catch {
                onErrorLoadingData()
            }
            .launchIn(viewModelScope)
    }

    private fun onErrorLoadingData() {
        _screenState.value = ScreenState.ERROR
    }

    fun saveWorkDay() {
        _durationError.value = false

        val currentId = workDayId ?: ""
        // Date
        val currentDate = date.value?.run { Timestamp(Date(this)) }
        if(currentDate == null) {
            _dateError.value = true
            return
        }
        // Clinic
        val currentClinic = clinic.value
        if(currentClinic == null) {
            _clinicError.value = true
            return
        }
        // Duration
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
                    date = currentDate,
                    clinic = currentClinic,
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
