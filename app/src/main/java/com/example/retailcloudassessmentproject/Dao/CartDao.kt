package com.example.retailcloudassessmentproject.Dao

import android.util.Log
import com.example.retailcloudassessmentproject.model.CartItem
import com.example.retailcloudassessmentproject.model.Item
import io.realm.Realm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object CartDao {

    fun addItemToCart(item: Item) {
        CoroutineScope(Dispatchers.IO).launch {
            val realm = Realm.getDefaultInstance()
            realm.use { realm ->
                realm.executeTransaction { transactionRealm ->
                    val itemId = item.itemID
                    Log.d("CartDao", "Adding item with ID: $itemId")
                    val cartItem = transactionRealm.where(CartItem::class.java).equalTo("itemID", itemId).findFirst()
                    if (cartItem != null) {
                        val newQuantity = (cartItem.quantity ?: 0) + 1
                        cartItem.quantity = newQuantity
                    } else {
                        val newCartItem = transactionRealm.createObject(CartItem::class.java, itemId)
                        newCartItem.itemName = item.itemName
                        newCartItem.sellingPrice = item.sellingPrice
                        newCartItem.taxPercentage = item.taxPercentage
                        newCartItem.quantity = 1
                    }
                }
            }
        }
    }


    fun getCartItems(): List<CartItem> {
        val realm = Realm.getDefaultInstance()
        val cartItems: List<CartItem>
        realm.use { realm ->
            cartItems = realm.copyFromRealm(realm.where(CartItem::class.java).findAll())
        }
        return cartItems
    }
}

