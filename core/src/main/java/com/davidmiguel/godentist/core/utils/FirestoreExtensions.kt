@file:Suppress("unused", "EXPERIMENTAL_API_USAGE")

package com.davidmiguel.godentist.core.utils

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// LiveData
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

fun <T> CollectionReference.liveData(clazz: Class<T>): LiveData<Result<List<T>>> {
    return CollectionLiveData(this, clazz)
}

fun <T> DocumentReference.liveData(clazz: Class<T>): LiveData<Result<T>> {
    return DocumentLiveData(this, clazz)
}

fun <T> Query.liveData(clazz: Class<T>): LiveData<Result<List<T>>> {
    return QueryLiveData(this, clazz)
}

// https://github.com/brotoo25/firestore-livedata/blob/master/firestore-livedata/src/main/java/com/kiwimob/firestore/livedata/CollectionLiveData.kt
private class CollectionLiveData<T>(
    private val collectionReference: CollectionReference,
    private val clazz: Class<T>
) : LiveData<Result<List<T>>>() {

    private var listener: ListenerRegistration? = null

    override fun onActive() {
        super.onActive()
        listener = collectionReference.addSnapshotListener { querySnapshot, exception ->
            value = if (exception == null) {
                querySnapshot?.toObjects(clazz)?.run {
                    Result.forSuccess(this)
                } ?: Result.forFailureNotFound()
            } else {
                Result.forFailure(exception)
            }
        }
    }

    override fun onInactive() {
        super.onInactive()
        listener?.remove()
        listener = null
    }
}

// https://github.com/brotoo25/firestore-livedata/blob/master/firestore-livedata/src/main/java/com/kiwimob/firestore/livedata/DocumentLiveData.kt
private class DocumentLiveData<T>(
    private val documentReference: DocumentReference,
    private val clazz: Class<T>
) : LiveData<Result<T>>() {

    private var listener: ListenerRegistration? = null

    override fun onActive() {
        super.onActive()
        listener = documentReference.addSnapshotListener { documentSnapshot, exception ->
            value = if (exception == null) {
                documentSnapshot?.toObject(clazz)?.run {
                    Result.forSuccess(this)
                } ?: Result.forFailureNotFound()
            } else {
                Result.forFailure(exception)
            }
        }
    }

    override fun onInactive() {
        super.onInactive()
        listener?.remove()
        listener = null
    }
}

// https://github.com/brotoo25/firestore-livedata/blob/master/firestore-livedata/src/main/java/com/kiwimob/firestore/livedata/QueryLiveData.kt
private class QueryLiveData<T>(
    private val query: Query,
    private val clazz: Class<T>
) : LiveData<Result<List<T>>>() {

    private var listener: ListenerRegistration? = null

    override fun onActive() {
        super.onActive()
        listener = query.addSnapshotListener { querySnapshot, exception ->
            value = if (exception == null) {
                querySnapshot?.toObjects(clazz)?.run {
                    Result.forSuccess(this)
                } ?: Result.forFailureNotFound()
            } else {
                Result.forFailure(exception)
            }
        }
    }

    override fun onInactive() {
        super.onInactive()
        listener?.remove()
        listener = null
    }
}

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Coroutines Flow
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

fun <T> CollectionReference.asFlow(clazz: Class<T>): Flow<Result<List<T>>> {
    return callbackFlow {
        val listener = addSnapshotListener { querySnapshot, exception ->
            if (exception == null) {
                val value = querySnapshot?.toObjects(clazz)?.run {
                    Result.forSuccess(this)
                } ?: Result.forFailureNotFound()
                offer(value)
            } else {
                offer(Result.forFailure(exception))
                close()
            }
        }
        awaitClose { listener.remove() }
    }
}

fun <T> DocumentReference.asFlow(clazz: Class<T>): Flow<Result<T>> {
    return callbackFlow {
        val listener = addSnapshotListener { documentSnapshot, exception ->
            if (exception == null) {
                val value = documentSnapshot?.toObject(clazz)?.run {
                    Result.forSuccess(this)
                } ?: Result.forFailureNotFound()
                offer(value)
            } else {
                offer(Result.forFailure(exception))
                close()
            }
        }
        awaitClose { listener.remove() }
    }
}

fun <T> Query.asFlow(clazz: Class<T>): Flow<Result<List<T>>> {
    return callbackFlow {
        val listener = addSnapshotListener { querySnapshot, exception ->
            if (exception == null) {
                val value = querySnapshot?.toObjects(clazz)?.run {
                    Result.forSuccess(this)
                } ?: Result.forFailureNotFound()
                offer(value)
            } else {
                offer(Result.forFailure(exception))
                close()
            }
        }
        awaitClose { listener.remove() }
    }
}
