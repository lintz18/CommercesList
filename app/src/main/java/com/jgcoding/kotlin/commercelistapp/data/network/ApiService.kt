package com.jgcoding.kotlin.commercelistapp.data.network

import com.jgcoding.kotlin.commercelistapp.data.network.response.CommercesModel
import com.jgcoding.kotlin.commercelistapp.data.network.response.CommercesResponse
import retrofit2.http.GET

interface ApiService {
    @GET("/commerces.json")
    suspend fun getCommerces(): List<CommercesModel>
}