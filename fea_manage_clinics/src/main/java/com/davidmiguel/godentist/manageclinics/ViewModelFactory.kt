package com.davidmiguel.godentist.manageclinics

import android.annotation.SuppressLint
import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.davidmiguel.godentist.core.data.clinics.ClinicsRepository
import com.davidmiguel.godentist.manageclinics.add.AddClinicViewModel
import com.davidmiguel.godentist.manageclinics.clinics.ClinicsViewModel
import com.google.firebase.auth.FirebaseAuth

class ViewModelFactory private constructor(
    private val firebaseAuth: FirebaseAuth,
    private val clinicsRepository: ClinicsRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(ClinicsViewModel::class.java) ->
                    ClinicsViewModel(firebaseAuth, clinicsRepository)
                isAssignableFrom(AddClinicViewModel::class.java) ->
                    AddClinicViewModel(firebaseAuth, clinicsRepository)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T

    companion object {

        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance() =
            INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                INSTANCE ?: ViewModelFactory(FirebaseAuth.getInstance(), ClinicsRepository())
                    .also { INSTANCE = it }
            }

        @VisibleForTesting
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
