package com.jgcoding.kotlin.commercelistapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jgcoding.kotlin.commercelistapp.data.database.entity.CommerceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CommerceDao {

    @Query("SELECT * FROM commerces_table")
    fun getAllCommerces(): Flow<List<CommerceEntity>>

    @Query("SELECT * FROM commerces_table WHERE id = :id")
    fun getCommerceById(id: Int): Flow<CommerceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommerces(commerces: List<CommerceEntity>)

    @Query("DELETE FROM commerces_table")
    suspend fun deleteAllCommerces()

}