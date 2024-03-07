package com.jgcoding.kotlin.commercelistapp.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jgcoding.kotlin.commercelistapp.domain.model.Commerce

@Entity(tableName = "commerces_table")
data class CommerceEntity(
    val name: String,
    val photo: String,
    val cashback: Double,
    val address: String,
    val openingHours: String,
    val category: String,
    val location: Location,
    @PrimaryKey val id: Int
){
    fun toDomain() = Commerce(
        name = name,
        photo = photo,
        cashback = cashback,
        address = address,
        openingHours = openingHours,
        category = category,
        location = Pair(location.lat, location.lng),
        id = id
    )
}
