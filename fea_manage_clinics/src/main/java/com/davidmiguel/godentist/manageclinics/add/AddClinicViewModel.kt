package com.davidmiguel.godentist.manageclinics.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.davidmiguel.godentist.core.utils.Event
import com.davidmiguel.godentist.core.utils.ScreenState

interface AddClinicViewModel {

    val screenState: LiveData<ScreenState>

    val snackbarEvent: LiveData<Event<Int>>
    val clinicUpdatedEvent: LiveData<Event<Unit>>

    val name : MutableLiveData<String>
    val nameError: LiveData<Boolean>

    val percentage : MutableLiveData<String>
    val percentageError: LiveData<Boolean>

    fun start(clinicId: String?)

    fun saveClinic()
}