package com.davidmiguel.godentist.core.data.clinics

import com.davidmiguel.godentist.core.data.COLLECTION_CLINICS
import com.davidmiguel.godentist.core.data.COLLECTION_USERS
import com.davidmiguel.godentist.core.model.Clinic
import com.davidmiguel.godentist.core.utils.Result
import com.davidmiguel.godentist.core.utils.asFlow
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

@Suppress("RedundantVisibilityModifier", "unused")
class ClinicsRepository {

    private val db by lazy { Firebase.firestore }

    public suspend fun exists(uid: String, clinicId: String): Result<Boolean> {
        return try {
            getClinicDocumentRef(uid, clinicId).get().continueWith {
                it.result?.exists() == true
            }.await().run { Result.forSuccess(this) }
        } catch (e: Exception) {
            Result.forFailure(e)
        }
    }

    public suspend fun get(uid: String, clinicId: String): Result<Clinic> {
        return try {
            getClinicDocumentRef(uid, clinicId).get().continueWith {
                it.result?.toObject<Clinic>()
            }.await()?.run {
                Result.forSuccess(this)
            } ?: Result.forFailureNotFound()
        } catch (e: Exception) {
            Result.forFailure(e)
        }
    }

    public fun getAll(uid: String): Flow<Result<List<Clinic>>> {
        return getClinicsCollectionRef(uid).asFlow(Clinic::class.java)
    }

    /**
     * Updates (or creates if it doesn't exist) a clinic.
     * To create a clinic the id must be empty.
     */
    public suspend fun put(uid: String, clinic: Clinic): Result<Unit> {
        if (clinic.id.isEmpty()) { // Create document if it doesn't exists
            clinic.id = getClinicsCollectionRef(uid).document().id
        }
        return try {
            getClinicDocumentRef(uid, clinic.id).set(clinic).await()
            Result.forSuccess(Unit)
        } catch (e: Exception) {
            Result.forFailure(e)
        }
    }

    private fun getClinicsCollectionRef(uid: String): CollectionReference {
        return db.collection(COLLECTION_USERS).document(uid).collection(COLLECTION_CLINICS)
    }

    private fun getClinicDocumentRef(uid: String, clinicId: String): DocumentReference {
        return getClinicsCollectionRef(uid).document(clinicId)
    }
}
