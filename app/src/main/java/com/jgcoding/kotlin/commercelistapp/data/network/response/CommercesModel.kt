package com.jgcoding.kotlin.commercelistapp.data.network.response

import com.google.gson.annotations.SerializedName
import com.jgcoding.kotlin.commercelistapp.domain.model.Commerce

//Aunque los nombres sean iguales prefiero usar siempre @SerializedName por si he de cambiar algún nombre que no sea adecuado o no se entienda
data class CommercesModel(
    @SerializedName("photo") val photo: String,
    @SerializedName("name") val name: String,
    @SerializedName("category") val category: String,
    @SerializedName("cashback") val cashback: Double,
    @SerializedName("location") val location: List<Double>,
    @SerializedName("openingHours") val openingHours: String,
    @SerializedName("address") val address: AddressModel,
    @SerializedName("_id") val id: Int
){
    fun toDomain() = Commerce(
        photo = photo,
        name = name,
        category = category,
        cashback = cashback,
        location = Pair(location[0], location[1]),
        openingHours = openingHours,
        address = "${address.street}, ${address.city}, ${address.state}, ${address.zip}, ${address.country}",
        id = id,
        distance = 0
    )
}

