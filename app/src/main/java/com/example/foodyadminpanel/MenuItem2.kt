package com.example.foodyadminpanel


import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuItem2(
    @get:Exclude var id: String?,
    @get:Exclude var parentId: String?,
    var name: String?,
    var description: String?,
    var imageUrl: String?,
    var price: Double?,
) : Parcelable {
    constructor() : this(null, null, null, null, null, null)
}
