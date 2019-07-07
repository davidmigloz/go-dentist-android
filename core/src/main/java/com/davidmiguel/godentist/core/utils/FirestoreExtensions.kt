@file:Suppress("unused")

package com.davidmiguel.godentist.core.utils

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

fun <T> CollectionReference.liveData(clazz: Class<T>): LiveData<Resource<List<T>>> {
    return CollectionLiveData(this, clazz)
}

fun <T> DocumentReference.liveData(clazz: Class<T>): LiveData<Resource<T>> {
    return DocumentLiveData(this, clazz)
}

fun <T> Query.liveData(clazz: Class<T>): LiveData<Resource<List<T>>> {
    return QueryLiveData(this, clazz)
}

// https://github.com/brotoo25/firestore-livedata/blob/master/firestore-livedata/src/main/java/com/kiwimob/firestore/livedata/CollectionLiveData.kt
private class CollectionLiveData<T>(
    private val collectionReference: CollectionReference,
    private val clazz: Class<T>
) : LiveData<Resource<List<T>>>(Resource.forLoading()) {

    private var listener: ListenerRegistration? = null

    override fun onActive() {
        super.onActive()
        listener = collectionReference.addSnapshotListener { querySnapshot, exception ->
            value = if (exception == null) {
                querySnapshot?.toObjects(clazz)?.run {
                    Resource.forSuccess(this)
                } ?: Resource.forLoading()
            } else {
                Resource.forFailure(exception)
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
) : LiveData<Resource<T>>(Resource.forLoading()) {

    private var listener: ListenerRegistration? = null

    override fun onActive() {
        super.onActive()
        listener = documentReference.addSnapshotListener { documentSnapshot, exception ->
            value = if (exception == null) {
                documentSnapshot?.toObject(clazz)?.run {
                    Resource.forSuccess(this)
                } ?: Resource.forLoading()
            } else {
                Resource.forFailure(exception)
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
) : LiveData<Resource<List<T>>>(Resource.forLoading()) {

    private var listener: ListenerRegistration? = null

    override fun onActive() {
        super.onActive()
        listener = query.addSnapshotListener { querySnapshot, exception ->
            value = if (exception == null) {
                querySnapshot?.toObjects(clazz)?.run {
                    Resource.forSuccess(this)
                } ?: Resource.forLoading()
            } else {
                Resource.forFailure(exception)
            }
        }
    }

    override fun onInactive() {
        super.onInactive()
        listener?.remove()
        listener = null
    }
}
