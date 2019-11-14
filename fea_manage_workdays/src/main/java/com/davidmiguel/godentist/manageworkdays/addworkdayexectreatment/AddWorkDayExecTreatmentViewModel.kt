package com.davidmiguel.godentist.manageworkdays.addworkdayexectreatment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidmiguel.godentist.core.R
import com.davidmiguel.godentist.core.data.clinics.ClinicsRepository
import com.davidmiguel.godentist.core.data.treatments.TreatmentsRepository
import com.davidmiguel.godentist.core.data.workdays.WorkDaysRepository
import com.davidmiguel.godentist.core.model.Treatment
import com.davidmiguel.godentist.core.model.WorkDay
import com.davidmiguel.godentist.core.model.WorkDay.ExecutedTreatment
import com.davidmiguel.godentist.core.utils.Event
import com.davidmiguel.godentist.core.utils.Failure
import com.davidmiguel.godentist.core.utils.ScreenState
import com.davidmiguel.godentist.core.utils.Success
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AddWorkDayExecTreatmentViewModel(
    private val firebaseAuth: FirebaseAuth,
    private val workDaysRepository: WorkDaysRepository,
    private val clinicsRepository: ClinicsRepository,
    private val treatmentsRepository: TreatmentsRepository
) : ViewModel() {

    private val _screenState = MutableLiveData(ScreenState.INITIAL)
    val screenState: LiveData<ScreenState>
        get() = _screenState

    private val _snackbarEvent = MutableLiveData<Event<Int>>()
    val snackbarEvent: LiveData<Event<Int>>
        get() = _snackbarEvent

    private val _workDayExecTreatmentUpdatedEvent = MutableLiveData<Event<Unit>>()
    val workDayExecTreatmentUpdatedEvent: LiveData<Event<Unit>>
        get() = _workDayExecTreatmentUpdatedEvent

    private var workDayId: String? = null
    private var executedTreatmentId: String? = null

    private var workDay: WorkDay? = null

    // Treatments
    private val _treatments: MutableLiveData<List<Treatment>> = MutableLiveData(listOf())
    val treatments: LiveData<List<Treatment>>
        get() = _treatments
    // Treatment
    val treatment = MutableLiveData<Treatment>()
    private val _treatmentsError = MutableLiveData(false)
    val treatmentsError: LiveData<Boolean>
        get() = _treatmentsError
    // Price
    val price = MutableLiveData<String>()
    private val _priceError = MutableLiveData(false)
    val priceError: LiveData<Boolean>
        get() = _priceError

    fun start(workDayId: String?, executedTreatmentId: String?) {
        if (_screenState.value != ScreenState.INITIAL) return
        loadData(workDayId, executedTreatmentId)
    }

    private fun loadData(workDayId: String?, executedTreatmentId: String?) {
        _screenState.value = ScreenState.LOADING_DATA
        workDayId?.run { loadWorkDay(this) }
        loadTreatments()
        _screenState.value = ScreenState.DATA_LOADED
    }

    private fun loadWorkDay(workDayId: String) {
        workDaysRepository.get(firebaseAuth.uid!!, workDayId)
            .onEach { res ->
                when (res) {
                    is Success -> workDay = res.value
                    is Failure -> onErrorLoadingData()
                }
            }
            .catch {
                onErrorLoadingData()
            }
            .launchIn(viewModelScope)
    }

    private fun loadTreatments() {
        treatmentsRepository.getAll(firebaseAuth.uid!!)
            .onEach { res ->
                when (res) {
                    is Success -> _treatments.value = res.value
                    is Failure -> onErrorLoadingData()
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

    fun saveTreatment() {
        _treatmentsError.value = false
        _priceError.value = false

        val currentWorkDay = workDay ?: return
        val currentId = executedTreatmentId ?: ""
        // Treatment
        val currentTreatment = treatment.value
        if (currentTreatment == null) {
            _treatmentsError.value = true
            return
        }
        // Price
        val currentPrice = price.value?.toDoubleOrNull()
        if (currentPrice == null) {
            _priceError.value = true
            return
        }

        currentWorkDay.executedTreatments = (workDay?.executedTreatments ?: mutableListOf()).apply {
            add(
                ExecutedTreatment(
                    id = currentId,
                    treatment = currentTreatment,
                    price = currentPrice
                )
            )
        }

        viewModelScope.launch {
            workDaysRepository.put(
                firebaseAuth.uid!!, currentWorkDay
            ).let { res ->
                when (res) {
                    is Success -> onWorkDayExecTreatmentSaved()
                    is Failure -> onErrorSavingWorkDay()
                }
            }
        }
    }

    private fun onWorkDayExecTreatmentSaved() {
        _workDayExecTreatmentUpdatedEvent.value = Event(Unit)
        _snackbarEvent.value = Event(R.string.all_dataSaved)
    }

    private fun onErrorSavingWorkDay() {
        _snackbarEvent.value = Event(R.string.all_errSavingData)
    }
}
