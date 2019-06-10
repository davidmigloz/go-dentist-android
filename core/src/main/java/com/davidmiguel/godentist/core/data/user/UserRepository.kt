package com.davidmiguel.godentist.core.data.user

import com.davidmiguel.godentist.core.data.COLLECTION_USERS
import com.davidmiguel.godentist.core.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class UserRepository() {

    private val db by lazy { Firebase.firestore }

    public fun exists(uid: String): Task<Boolean> {
        return getUserDocumentRef(uid).get().continueWith {
            it.result?.exists() == true
        }
    }

    public fun get(uid: String): Task<User?> {
        return getUserDocumentRef(uid).get().continueWith {
            it.result?.toObject<User>()
        }
    }

    public fun update(user: User): Task<Void> {
        return getUserDocumentRef(user.uid).set(user)
    }

    private fun getUserDocumentRef(uid: String): DocumentReference {
        return db.collection(COLLECTION_USERS).document(uid)
    }
}