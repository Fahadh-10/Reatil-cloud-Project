package com.example.retailcloudassessmentproject.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable

open class Item(
    @PrimaryKey var itemID: String? = null,
    @SerializedName("itemName") var itemName: String? = null,
    @SerializedName("sellingPrice") var sellingPrice: Double? = null,
    @SerializedName("taxPercentage") var taxPercentage: Double? = null,
    var quantity: Int? = 0
) : Serializable, RealmObject()