package com.jgcoding.kotlin.commercelistapp.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.jgcoding.kotlin.commercelistapp.data.database.entity.Location

class Converter {

    @TypeConverter
    fun locationToString(location: Location): String = Gson().toJson(location)

    @TypeConverter
    fun stringToLocation(string: String): Location = Gson().fromJson(string, Location::class.java)

}