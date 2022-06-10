package com.example.foodyadminpanel

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuItem(
    @Exclude var id: String?,
    @Exclude var parentId: String?,
    var name: String?,
    var price: Double?,
    var quantity: Long?,
    var total: Double?,
    var imageUrl: String?,
) : Parcelable {
    constructor() : this(null, null, null, null, null, null,null)
}
