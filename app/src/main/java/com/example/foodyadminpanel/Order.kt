package com.example.foodyadminpanel

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.util.Date
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@Parcelize
data class Order(
    @Exclude var orderId: String?,
    var userId: String?,
    var categoryMealPair: @RawValue ArrayList<HashMap<String, Any>>?,
    var total: Double?,
    var restaurantId: String?,
    var restaurantName: String?,
    var paymentMethod: String?,
    val timeStamp: Timestamp?,
    var orderState: String?,
    var deliveredDate: Timestamp?,
    var shippingAddress: String?
) : Parcelable {
    constructor() : this(
        null,
        null,
        null,
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
