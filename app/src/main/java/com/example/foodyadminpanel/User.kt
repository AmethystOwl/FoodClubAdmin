package com.example.foodyadminpanel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val isUser: Boolean = true,
    var name: String,
    val email: String,
    var dateOfBirth: String,
    var phoneNumber: String,
    var isEmailVerified: Boolean = false,
    var isPhoneVerified: Boolean = false,
    var profilePictureUrl: String? = null,
    var favorites: ArrayList<String>? = null,
) : Parcelable {
    constructor() : this(
        true,
        "",
        "",
        "",
        "",
        false,
        false,
        null,
        null
    )
}
