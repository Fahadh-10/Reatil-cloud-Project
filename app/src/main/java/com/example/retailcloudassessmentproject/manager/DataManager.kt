package com.example.retailcloudassessmentproject.manager

import com.example.retailcloudassessmentproject.APIService
import com.example.retailcloudassessmentproject.helpers.API_URL
import com.example.retailcloudassessmentproject.model.Item
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DataManager {

    private val retrofit = Retrofit.Builder()
        .baseUrl(API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(APIService::class.java)

    fun getItems(callback: APICallback<List<Item>>) {
        val call = apiService.getItems()
        call.enqueue(object : Callback<List<Item>> {
            override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
                if (response.isSuccessful) {
                    val items = response.body()
                    if (items != null) {
                        callback.onSuccess(items)
                    } else {
                        callback.onFailure("Response null")
                    }
                } else {
                    callback.onFailure("Request failed: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                callback.onFailure("Request failed: ${t.message}")
            }
        })
    }

    interface APICallback<T> {
        fun onSuccess(response: T)
        fun onFailure(message: String)
    }
}
