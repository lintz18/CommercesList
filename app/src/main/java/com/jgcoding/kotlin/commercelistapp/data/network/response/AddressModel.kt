package com.jgcoding.kotlin.commercelistapp.data.network.response

import com.google.gson.annotations.SerializedName

data class AddressModel(
    @SerializedName("street") val street: String,
    @SerializedName("city") val city: String,
    @SerializedName("state") val state: String,
    @SerializedName("zip") val zip: String,
    @SerializedName("country") val country: String
)