package com.example.retailcloudassessmentproject.Dao

import android.util.Log
import com.example.retailcloudassessmentproject.model.Item
import io.realm.Realm
import io.realm.exceptions.RealmException
import java.io.IOException

object ItemDao {

    fun saveOrUpdateItems(items: List<Item>) {
        val realm = Realm.getDefaultInstance()
        try {
            realm.beginTransaction()
            realm.insertOrUpdate(items)
            realm.commitTransaction()
        } catch (e: RealmException) {
            realm.cancelTransaction()
            Log.e("ItemDao", "Error saving items to Realm", e)
        } finally {
            realm.close()
        }
    }

    fun fetchItems(): List<Item> {
        val realm = Realm.getDefaultInstance()
        val results = realm.where(Item::class.java).findAll()
        val items = realm.copyFromRealm(results)
        realm.close()
        return items
    }
}
