package com.example.retailcloudassessmentproject.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable

open class CartItem(
    @PrimaryKey var itemID: String? = null,
    var itemName: String? = null,
    var sellingPrice: Double? = null,
    var taxPercentage: Double? = null,
    var quantity: Int? = 0
) : RealmObject()