package com.example.retailcloudassessmentproject

import com.example.retailcloudassessmentproject.model.Item
import retrofit2.Call
import retrofit2.http.GET

interface APIService {
    @GET("/")
    fun getItems(): Call<List<Item>>
}