package com.example.foodyadminpanel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Restaurant(
    // editable
    var name: String?,
    var cuisines: ArrayList<String>?,
    var iconImageUrl: String?,
    var location: String?,
    var foodPreviewList: ArrayList<String>?,
    var openNow: Boolean?,

    // read-only
    val rating: Double? = 0.0,
    val noOfReviews: Long? =0,
) : Parcelable {
    constructor() : this(
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    )
}
