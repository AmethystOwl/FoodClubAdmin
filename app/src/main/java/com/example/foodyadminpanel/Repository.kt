package com.example.foodyadminpanel

import android.net.Uri
import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn

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

    fun updateMenuItem(newItem: MenuItem2, imageUri: Uri?) = callbackFlow<DataState<Int>> {
        auth.uid?.let {
            trySend(DataState.Loading)
            val doc = fireStore.collection("restaurants")
                .document(auth.uid!!)
                .collection("MenuCategories")
                .document(newItem.parentId!!)
                .collection("MenuItems")
                .document(newItem.id!!)
            if (imageUri == null) {
                // .set(newItem, SetOptions.merge())
                doc.update(
                    "name", newItem.name,
                    "description", newItem.description,
                    "price", newItem.price
                ).addOnCompleteListener {
                    when {
                        it.isSuccessful -> {
                            trySend(DataState.Success(0))
                            close()
                        }
                        it.isCanceled -> {
                            trySend(DataState.Canceled)
                            close(CancellationException())
                        }
                        it.exception != null -> {
                            trySend(DataState.Error(it.exception!!))
                            close(CancellationException("Error updating menuItem: ", it.exception))
                        }
                    }
                }
            } else {
                val file = storage.reference.child("Restaurants")
                    .child(auth.uid!!)
                    .child("menu")
                    .child(System.currentTimeMillis().toString())
                val uploadTask =
                    file.putFile(imageUri)
                uploadTask.continueWithTask {
                    it.addOnCompleteListener {
                        when {
                            it.isSuccessful -> {
                                file.downloadUrl.addOnCompleteListener {
                                    when {
                                        it.isSuccessful -> {
                                            newItem.imageUrl = it.result.toString()
                                            doc.update(
                                                "name", newItem.name,
                                                "description", newItem.description,
                                                "price", newItem.price,
                                                "imageUrl", newItem.imageUrl
                                            )
                                                .addOnCompleteListener {
                                                    when {
                                                        it.isSuccessful -> {
                                                            trySend(DataState.Success(0))
                                                            close()
                                                        }
                                                        it.isCanceled -> {
                                                            trySend(DataState.Canceled)
                                                            close(CancellationException())
                                                        }
                                                        it.exception != null -> {
                                                            trySend(DataState.Error(it.exception!!))
                                                            close(CancellationException("Error updating menuItem: ", it.exception))
                                                        }

                                                    }

                                                }

                                        }
                                        it.isCanceled -> {
                                            trySend(DataState.Canceled)
                                            close(CancellationException())
                                        }
                                        it.exception != null -> {
                                            trySend(DataState.Error(it.exception!!))
                                            close(CancellationException("Error updating menuItem: ", it.exception))
                                        }
                                    }
                                }

                            }
                            it.isCanceled -> {
                                trySend(DataState.Canceled)
                                close(CancellationException())
                            }
                            it.exception != null -> {
                                trySend(DataState.Error(it.exception!!))
                                close(CancellationException("Error uploading restaurant icon: ", it.exception))
                            }
                        }
                    }
                    file.downloadUrl
                }
            }
        }
        awaitClose()
    }.flowOn(Dispatchers.IO)


    fun addMenuItem(menuItem: MenuItem2, category: MenuCategory, imageUri: Uri) = callbackFlow<DataState<Int>> {
        auth.uid?.let {
            trySend(DataState.Loading)
            val file = storage.reference.child("Restaurants")
                .child(auth.uid!!)
                .child("menu")
                .child(menuItem.name!!)
            val uploadTask =
                file.putFile(imageUri)
            uploadTask.continueWithTask {
                it.addOnCompleteListener {
                    when {
                        it.isSuccessful -> {
                            file.downloadUrl.addOnCompleteListener {
                                when {
                                    it.isSuccessful -> {
                                        menuItem.imageUrl = it.result.toString()
                                        fireStore.collection("restaurants")
                                            .document(auth.uid!!)
                                            .collection("MenuCategories")
                                            .document(category.id!!)
                                            .collection("MenuItems")
                                            .add(menuItem).addOnCompleteListener {
                                                when {
                                                    it.isSuccessful -> {
                                                        trySend(DataState.Success(0))
                                                        close()
                                                    }
                                                    it.isCanceled -> {
                                                        trySend(DataState.Canceled)
                                                        close(CancellationException())
                                                    }
                                                    it.exception != null -> {
                                                        trySend(DataState.Error(it.exception!!))
                                                        close(CancellationException("Error adding item: ", it.exception))
                                                    }
                                                }
                                            }


                                    }
                                    it.isCanceled -> {
                                        trySend(DataState.Canceled)
                                        close(CancellationException())
                                    }
                                    it.exception != null -> {
                                        trySend(DataState.Error(it.exception!!))
                                        close(CancellationException("Error uploading image: ", it.exception))
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
        awaitClose()
    }.flowOn(Dispatchers.IO)

    fun addMenuItemWithCategory(menuItem: MenuItem2, imageUri: Uri, categoryName: String) = callbackFlow<DataState<Int>> {
        auth.uid?.let {
            trySend(DataState.Loading)
            val file = storage.reference.child("Restaurants")
                .child(auth.uid!!)
                .child("menu")
                .child(menuItem.name!!)
            val uploadTask =
                file.putFile(imageUri)
            uploadTask.continueWithTask {
                it.addOnCompleteListener {
                    when {
                        it.isSuccessful -> {
                            file.downloadUrl.addOnCompleteListener {
                                when {
                                    it.isSuccessful -> {
                                        menuItem.imageUrl = it.result.toString()
                                        val categoryDoc = fireStore.collection("restaurants")
                                            .document(auth.uid!!)
                                            .collection("MenuCategories")
                                            .document()

                                        categoryDoc.set(hashMapOf("categoryName" to categoryName)).addOnCompleteListener {
                                            when {
                                                it.isSuccessful -> {
                                                    categoryDoc.collection("MenuItems")
                                                        .add(menuItem).addOnCompleteListener {
                                                            when {
                                                                it.isSuccessful -> {
                                                                    trySend(DataState.Success(0))
                                                                    close()
                                                                }
                                                                it.isCanceled -> {
                                                                    trySend(DataState.Canceled)
                                                                    close(CancellationException())
                                                                }
                                                                it.exception != null -> {
                                                                    trySend(DataState.Error(it.exception!!))
                                                                    close(CancellationException("Error adding item: ", it.exception))
                                                                }
                                                            }
                                                        }

                                                }
                                            }
                                        }
                                    }
                                    it.isCanceled -> {
                                        trySend(DataState.Canceled)
                                        close(CancellationException())
                                    }
                                    it.exception != null -> {
                                        trySend(DataState.Error(it.exception!!))
                                        close(CancellationException("Error uploading image: ", it.exception))
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
        awaitClose()


        awaitClose()
    }.flowOn(Dispatchers.IO)

    fun updateInfo(name: String, loc: String, workingHours: String, imageUri: Uri?, cuisines: ArrayList<String>?, imagesToUpload: List<String>?) =
        callbackFlow<DataState<Int>> {
            var flag = 0
            auth.uid?.let {
                val doc = fireStore.collection("restaurants")
                    .document(auth.uid!!)
                fireStore.runBatch { batch ->
                    batch.update(doc, "name", name)
                    batch.update(doc, "workingHours", workingHours)
                    batch.update(doc, "location", loc)
                    cuisines?.let {
                        batch.update(doc, "cuisines", cuisines)
                    }
                    imageUri?.let {
                        val path = storage.reference.child("Restaurants")
                            .child(auth.uid!!)
                            .child("icon")
                            .child(System.currentTimeMillis().toString())
                        path.putFile(it).continueWithTask {
                            it.addOnCompleteListener {
                                when {
                                    it.isSuccessful -> {
                                        path.downloadUrl.addOnCompleteListener {
                                            when {
                                                it.isSuccessful -> {
                                                    batch.update(doc, "imageUrl", it.result.toString())
                                                }
                                                it.isCanceled -> {
                                                    flag = -1
                                                }
                                                it.exception != null -> {
                                                    flag = -1
                                                }
                                            }
                                        }
                                    }
                                    it.isCanceled -> {
                                        flag = -1

                                    }
                                    it.exception != null -> {
                                        flag = -1

                                    }
                                }
                            }
                        }

                    }

                    imagesToUpload?.let { files ->
                        val path = storage.reference.child("Restaurants")
                            .child(auth.uid!!)
                            .child("previewImages")
                            .child(System.currentTimeMillis().toString())
                        for (file in files) {
                            path.putFile(file.toUri()).continueWithTask {
                                it.addOnCompleteListener {
                                    when {
                                        it.isSuccessful -> {
                                            path.downloadUrl.addOnCompleteListener {
                                                when {
                                                    it.isSuccessful -> {
                                                        doc.update("foodPreviewList", FieldValue.arrayUnion(it.result.toString()))
                                                    }
                                                    it.isCanceled -> {
                                                        flag = -1
                                                    }
                                                    it.exception != null -> {
                                                        flag = -1
                                                    }
                                                }
                                            }
                                        }
                                        it.isCanceled -> {
                                            flag = -1
                                        }
                                        it.exception != null -> {
                                            flag = -1
                                        }
                                    }
                                }
                            }
                        }

                    }
                }.addOnCompleteListener {
                    when {
                        it.isSuccessful -> {
                            if (flag == 0) {
                                trySend(DataState.Success(0))
                                close()
                            } else {
                                trySend(DataState.Success(1))
                                close()

                            }
                        }
                        it.isCanceled -> {
                            trySend(DataState.Canceled)
                            close(CancellationException())
                        }
                        it.exception != null -> {
                            trySend(DataState.Error(it.exception!!))
                            close(CancellationException("Error while updating: ${it.exception?.message!!}"))
                        }
                    }
                }
            }
            awaitClose()
        }.flowOn(Dispatchers.IO)

    fun removePreviewImage(imageUrl: String) = callbackFlow<DataState<Int>> {
        auth.uid?.let {
            fireStore.collection("restaurants")
                .document(auth.uid!!)
                .update("foodPreviewList", FieldValue.arrayRemove(imageUrl))
                .addOnCompleteListener {
                    when{
                        it.isSuccessful -> {
                            trySend(DataState.Success(0))
                            close()
                        }
                        it.exception != null -> {
                            trySend(DataState.Error(it.exception!!))
                            close(CancellationException("Error removing preview image: ${it.exception?.message!!}"))
                        }
                    }
                }
        }
        awaitClose()
    }.flowOn(Dispatchers.IO)
}
