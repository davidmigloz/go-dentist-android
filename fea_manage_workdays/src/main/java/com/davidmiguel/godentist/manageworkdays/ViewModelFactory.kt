package com.davidmiguel.godentist.manageworkdays

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.davidmiguel.godentist.core.data.workdays.WorkDaysRepository
import com.davidmiguel.godentist.manageworkdays.add.AddWorkDayViewModel
import com.google.firebase.auth.FirebaseAuth

class ViewModelFactory private constructor(
    private val firebaseAuth: FirebaseAuth,
    private val workDaysRepository: WorkDaysRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(AddWorkDayViewModel::class.java) ->
                    AddWorkDayViewModel(firebaseAuth, workDaysRepository)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T

    companion object {

        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance() =
            INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                INSTANCE ?: ViewModelFactory(FirebaseAuth.getInstance(), WorkDaysRepository())
                    .also { INSTANCE = it }
            }

        @VisibleForTesting
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
