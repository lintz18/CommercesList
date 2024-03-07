package com.jgcoding.kotlin.commercelistapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jgcoding.kotlin.commercelistapp.data.database.dao.CommerceDao
import com.jgcoding.kotlin.commercelistapp.data.database.entity.CommerceEntity

@Database(
    entities = [CommerceEntity::class], //Location?
    version = 1
)
@TypeConverters(Converter::class)
abstract class CommerceDatabase : RoomDatabase() {
    abstract fun getCommerceDao(): CommerceDao
}