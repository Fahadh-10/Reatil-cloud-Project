package com.example.retailcloudassessmentproject.repository

import com.example.retailcloudassessmentproject.Dao.CartDao
import com.example.retailcloudassessmentproject.Dao.ItemDao
import com.example.retailcloudassessmentproject.manager.DataManager
import com.example.retailcloudassessmentproject.model.CartItem
import com.example.retailcloudassessmentproject.model.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ItemRepository {

    /**
     * Fetches items from the remote data source.
     * @param callback The callback to be invoked with the result of the API call.
     */
    fun getItems(callback: DataManager.APICallback<List<Item>>) {
        DataManager.getItems(object : DataManager.APICallback<List<Item>> {
            override fun onSuccess(response: List<Item>) {
                saveItemsToRealm(response)
                callback.onSuccess(response)
            }

            override fun onFailure(message: String) {
                callback.onFailure(message)
            }
        })
    }

    /**
     * Saves the fetched items to the local Realm database.
     * @param items The list of items to be saved.
     */
    private fun saveItemsToRealm(items: List<Item>) {
        ItemDao.saveOrUpdateItems(items)
    }

    /**
     * Fetches items from the local Realm database.
     * @return A list of items from the local database.
     */
    fun fetchItemsFromDB(): List<Item> {
        return ItemDao.fetchItems()
    }

    /**
     * Fetches cart items from the local data source asynchronously.
     * @param callback The callback to be invoked with the result of the cart items fetch.
     */
    fun getCartItems(callback: DataManager.APICallback<List<CartItem>>) {
        CoroutineScope(Dispatchers.IO).launch {
            val cartItems = CartDao.getCartItems()
            withContext(Dispatchers.Main) {
                callback.onSuccess(cartItems)
            }
        }
    }
}

