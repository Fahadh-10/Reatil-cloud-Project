package com.example.retailcloudassessmentproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.retailcloudassessmentproject.manager.DataManager
import com.example.retailcloudassessmentproject.model.CartItem
import com.example.retailcloudassessmentproject.repository.ItemRepository

class CartViewModel : ViewModel() {

    private val repository = ItemRepository()

    private val _cartItems = MutableLiveData<List<CartItem>>()
    val cartItems: LiveData<List<CartItem>> get() = _cartItems

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    /**
     * Fetches the cart items from the repository and updates the LiveData.
     */
    fun fetchCartItems() {
        _isLoading.value = true
        repository.getCartItems(object : DataManager.APICallback<List<CartItem>> {
            override fun onSuccess(response: List<CartItem>) {
                _isLoading.value = false
                _cartItems.value = response
            }

            override fun onFailure(message: String) {
                _isLoading.value = false
            }
        })
    }

    /**
     * Calculates the total amount including tax for the given list of cart items.
     * @return The total amount including tax.
     */
    fun calculateTotalWithTax(cartItems: List<CartItem>?): Double {
        cartItems ?: return 0.0 // Handle null or empty case gracefully
        var totalAmount = 0.0
        for (item in cartItems) {
            val itemTotal = item.sellingPrice?.times(item.quantity ?: 1) ?: 0.0
            val taxAmount = itemTotal * ((item.taxPercentage ?: 0.0) / 100)
            totalAmount += itemTotal + taxAmount
        }
        return totalAmount
    }

    /**
     * Calculates the total quantity of items in the given list of cart items.
     * @param cartItems cart items list
     * @return The total quantity of items.
     */
    fun calculateTotalQuantity(cartItems: List<CartItem>?): Int {
        cartItems ?: return 0
        var totalQuantity = 0
        for (item in cartItems) {
            totalQuantity += item.quantity ?: 0
        }
        return totalQuantity
    }


    /**
     * Calculates the total amount for the given list of cart items.
     * @param cartItems The list of cart items.
     * @return The total amount.
     */
    fun calculateTotalAmount(cartItems: List<CartItem>?): Double {
        cartItems ?: return 0.0
        var totalAmount = 0.0
        for (item in cartItems) {
            val itemTotal = item.sellingPrice?.times(item.quantity ?: 1) ?: 0.0
            totalAmount += itemTotal
        }
        return totalAmount
    }

    /**
     * Calculates the total tax amount for the given list of cart items.
     * @param cartItems The list of cart items.
     * @return The total tax amount.
     */
    fun calculateTotalTaxAmount(cartItems: List<CartItem>?): Double {
        cartItems ?: return 0.0
        var totalTaxAmount = 0.0
        for (item in cartItems) {
            val itemTotal = item.sellingPrice?.times(item.quantity ?: 1) ?: 0.0
            val taxAmount = itemTotal * ((item.taxPercentage ?: 0.0) / 100)
            totalTaxAmount += taxAmount
        }
        return totalTaxAmount
    }
}





