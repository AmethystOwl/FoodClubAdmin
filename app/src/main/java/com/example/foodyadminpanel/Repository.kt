package com.example.foodyadminpanel

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

@OptIn(ExperimentalCoroutinesApi::class)
class Repository {
    val fireStore = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance()
    val auth = FirebaseAuth.getInstance()

    fun login(email: String, password: String) = callbackFlow<DataState<Int>> {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            when {
                it.isSuccessful -> {
                    trySend(DataState.Success(0))
                    close()
                }
                it.exception != null -> {
                    trySend(DataState.Error(it.exception!!))
                    cancel(CancellationException())
                }
            }
        }

        awaitClose()
    }

    fun getRestaurantInfo() = callbackFlow<DataState<Restaurant>> {
        if (auth.uid != null) {
            fireStore.collection("restaurants")
                .document(auth.uid!!)
                .get()
                .addOnCompleteListener {
                    when {
                        it.isSuccessful -> {
                            val restaurant = it.result.toObject(Restaurant::class.java)
                            trySend(DataState.Success(restaurant!!))
                            close()
                        }
                        it.exception != null -> {
                            trySend(DataState.Error(it.exception!!))
                            cancel(CancellationException())
                        }
                    }
                }
        }

        awaitClose()
    }

}
