package com.example.foodyadminpanel

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuCategory(
    @Exclude var isSelected: Boolean?,
    @Exclude var id:String?,
    var categoryName: String?,
) : Parcelable{
    constructor() : this(null,null,null)
}
