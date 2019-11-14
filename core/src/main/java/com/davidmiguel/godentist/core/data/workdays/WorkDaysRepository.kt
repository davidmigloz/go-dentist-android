package com.davidmiguel.godentist.core.data.workdays

import com.davidmiguel.godentist.core.data.COLLECTION_USERS
import com.davidmiguel.godentist.core.data.COLLECTION_WORKDAYS
import com.davidmiguel.godentist.core.model.WorkDay
import com.davidmiguel.godentist.core.utils.Result
import com.davidmiguel.godentist.core.utils.asFlow
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

@Suppress("RedundantVisibilityModifier", "unused")
class WorkDaysRepository {

    private val db by lazy { Firebase.firestore }

    public suspend fun exists(uid: String, workDayId: String): Result<Boolean> {
        return try {
            getWorkDayDocumentRef(uid, workDayId).get().continueWith {
                it.result?.exists() == true
            }.await().run { Result.forSuccess(this) }
        } catch (e: Exception) {
            Result.forFailure(e)
        }
    }

    public fun get(uid: String, workDayId: String): Flow<Result<WorkDay>> {
        return getWorkDayDocumentRef(uid, workDayId).asFlow(WorkDay::class.java)
    }

    public fun getAll(uid: String): Flow<Result<List<WorkDay>>> {
        return getWorkDaysCollectionRef(uid).asFlow(WorkDay::class.java)
    }

    /**
     * Updates (or creates if it doesn't exist) a work day.
     * To create a work day the id must be empty.
     */
    public suspend fun put(uid: String, workDay: WorkDay): Result<WorkDay> {
        if (workDay.id.isEmpty()) { // Create document if it doesn't exists
            workDay.id = getWorkDaysCollectionRef(uid).document().id
        }
        return try {
            getWorkDayDocumentRef(uid, workDay.id).set(workDay).await()
            Result.forSuccess(workDay)
        } catch (e: Exception) {
            Result.forFailure(e)
        }
    }

    private fun getWorkDaysCollectionRef(uid: String): CollectionReference {
        return db.collection(COLLECTION_USERS).document(uid).collection(COLLECTION_WORKDAYS)
    }

    private fun getWorkDayDocumentRef(uid: String, workdayId: String): DocumentReference {
        return getWorkDaysCollectionRef(uid).document(workdayId)
    }
}