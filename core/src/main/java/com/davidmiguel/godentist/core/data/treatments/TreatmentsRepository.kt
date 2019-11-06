package com.davidmiguel.godentist.core.data.treatments

import com.davidmiguel.godentist.core.data.COLLECTION_TREATMENTS
import com.davidmiguel.godentist.core.data.COLLECTION_USERS
import com.davidmiguel.godentist.core.model.Treatment
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
class TreatmentsRepository {

    private val db by lazy { Firebase.firestore }

    public suspend fun exists(uid: String, treatmentId: String): Result<Boolean> {
        return try {
            getTreatmentDocumentRef(uid, treatmentId).get().continueWith {
                it.result?.exists() == true
            }.await().run { Result.forSuccess(this) }
        } catch (e: Exception) {
            Result.forFailure(e)
        }
    }

    public suspend fun get(uid: String, treatmentId: String): Result<Treatment> {
        return try {
            getTreatmentDocumentRef(uid, treatmentId).get().continueWith {
                it.result?.toObject<Treatment>()
            }.await()?.run {
                Result.forSuccess(this)
            } ?: Result.forFailureNotFound()
        } catch (e: Exception) {
            Result.forFailure(e)
        }
    }

    public fun getAll(uid: String): Flow<Result<List<Treatment>>> {
        return getTreatmentsCollectionRef(uid).asFlow(Treatment::class.java)
    }

    /**
     * Updates (or creates if it doesn't exist) a treatment.
     * To create a treatment the id must be empty.
     */
    public suspend fun put(uid: String, treatment: Treatment): Result<Unit> {
        if (treatment.id.isEmpty()) { // Create document if it doesn't exists
            treatment.id = getTreatmentsCollectionRef(uid).document().id
        }
        return try {
            getTreatmentDocumentRef(uid, treatment.id).set(treatment).await()
            Result.forSuccess(Unit)
        } catch (e: Exception) {
            Result.forFailure(e)
        }
    }

    private fun getTreatmentsCollectionRef(uid: String): CollectionReference {
        return db.collection(COLLECTION_USERS).document(uid).collection(COLLECTION_TREATMENTS)
    }

    private fun getTreatmentDocumentRef(uid: String, treatmentId: String): DocumentReference {
        return getTreatmentsCollectionRef(uid).document(treatmentId)
    }
}