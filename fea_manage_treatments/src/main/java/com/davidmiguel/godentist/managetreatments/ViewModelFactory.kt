package com.davidmiguel.godentist.managetreatments

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.davidmiguel.godentist.core.data.treatments.TreatmentsRepository
import com.davidmiguel.godentist.managetreatments.add.AddTreatmentViewModel
import com.davidmiguel.godentist.managetreatments.treatments.TreatmentsViewModel
import com.google.firebase.auth.FirebaseAuth

class ViewModelFactory private constructor(
    private val firebaseAuth: FirebaseAuth,
    private val treatmentsRepository: TreatmentsRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(AddTreatmentViewModel::class.java) ->
                    AddTreatmentViewModel(firebaseAuth, treatmentsRepository)
                isAssignableFrom(TreatmentsViewModel::class.java) ->
                    TreatmentsViewModel(firebaseAuth, treatmentsRepository)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T

    companion object {

        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance() =
            INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                INSTANCE ?: ViewModelFactory(FirebaseAuth.getInstance(), TreatmentsRepository())
                    .also { INSTANCE = it }
            }

        @VisibleForTesting
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
