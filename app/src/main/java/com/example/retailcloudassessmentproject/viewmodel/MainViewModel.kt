package com.example.retailcloudassessmentproject.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retailcloudassessmentproject.Dao.CartDao
import com.example.retailcloudassessmentproject.helpers.isNetworkAvailable
import com.example.retailcloudassessmentproject.manager.DataManager
import com.example.retailcloudassessmentproject.model.CartItem
import com.example.retailcloudassessmentproject.repository.ItemRepository
import com.example.retailcloudassessmentproject.model.Item
import kotlinx.coroutines.launch

class MainViewModel(private val context: Context) : ViewModel() {

    private val repository = ItemRepository()

    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>> get() = _items

    private val _cartItems = MutableLiveData<List<CartItem>>()
    val cartItems: LiveData<List<CartItem>> get() = _cartItems

    private val _cartItemCount = MutableLiveData<Int>()
    val cartItemCount: LiveData<Int> get() = _cartItemCount

    private val _totalProductCount = MutableLiveData<Int>()
    val totalProductCount: LiveData<Int> get() = _totalProductCount

    private val _totalAmount = MutableLiveData<Double>()
    val totalAmount: LiveData<Double> get() = _totalAmount

    private val _totalTax = MutableLiveData<Double>()
    val totalTax: LiveData<Double> get() = _totalTax

    private val _totalAmountWithTax = MutableLiveData<Double>()
    val totalAmountWithTax: LiveData<Double> get() = _totalAmountWithTax

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    init {
        fetchItems()
        fetchCartItems()
    }

    /**
     * Fetches the items from the repository. If the network is not available,
     * fetches the items from the local database.
     */
    fun fetchItems() {
        _isLoading.value = true
        if (context.isNetworkAvailable()) {
        repository.getItems(object : DataManager.APICallback<List<Item>> {
            override fun onSuccess(response: List<Item>) {
                _isLoading.value = false
                _items.value = response
                _items.postValue(response)
                calculateTotals()
            }

            override fun onFailure(message: String) {
                _isLoading.value = false
                getItemsFromDB()
            }
        })
        } else {
            getItemsFromDB()
        }
    }

    /**
     * Fetches the items from the local database.
     */
    private fun getItemsFromDB() {
        _items.value = repository.fetchItemsFromDB()
        calculateTotals()
    }

    /**
     * Fetches the cart items from the CartDao.
     */
    fun fetchCartItems() {
        _isLoading.value = true
        _cartItemCount.value = CartDao.getCartItems().size
        _cartItems.value = CartDao.getCartItems()
        _isLoading.value = false
    }

    /**
     * Calculates the total counts, amounts, and taxes for the cart items.
     */
    fun calculateTotals() {
        viewModelScope.launch {
            val cartItems = _cartItems.value ?: emptyList()

            var totalCount = 0
            cartItems.forEach { item ->
                totalCount += item.quantity ?: 0
            }
            _totalProductCount.value = totalCount

            var totalAmount = 0.0
            cartItems.forEach { item ->
                totalAmount += (item.sellingPrice ?: 0.0) * (item.quantity ?: 0)
            }
            _totalAmount.value = totalAmount

            var totalTax = 0.0
            cartItems.forEach { item ->
                totalTax += ((item.sellingPrice ?: 0.0) * (item.taxPercentage ?: 0.0) / 100) * (item.quantity ?: 0)
            }
            _totalTax.value = totalTax

            var totalAmountWithTax = 0.0
            cartItems.forEach { item ->
                val itemTotal = (item.sellingPrice ?: 0.0) * (item.quantity ?: 0)
                val taxAmount = itemTotal * ((item.taxPercentage ?: 0.0) / 100)
                totalAmountWithTax += itemTotal + taxAmount
            }
            _totalAmountWithTax.value = totalAmountWithTax
        }
    }
}





