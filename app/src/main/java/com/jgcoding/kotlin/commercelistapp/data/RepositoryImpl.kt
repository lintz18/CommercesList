package com.jgcoding.kotlin.commercelistapp.data

import android.util.Log
import com.jgcoding.kotlin.commercelistapp.data.database.dao.CommerceDao
import com.jgcoding.kotlin.commercelistapp.data.database.entity.CommerceEntity
import com.jgcoding.kotlin.commercelistapp.data.network.ApiService
import com.jgcoding.kotlin.commercelistapp.domain.Repository
import com.jgcoding.kotlin.commercelistapp.domain.model.Commerce
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val commerceDao: CommerceDao
) : Repository {

    companion object {
        private const val TAG = "RepositoryImpl"
    }

    //region API
    override suspend fun getCommercesApi(): List<Commerce> {
        runCatching { apiService.getCommerces() }
            .onSuccess { commerceModel ->
                for (commerce in commerceModel){
                    var cat =""
                    if(commerce.category != cat)
                        cat = commerce.category
                    Log.i(TAG, "getCommerces: CATEGORY: $cat")
                }
                return commerceModel.map { it.toDomain() }
            }
            .onFailure {
                Log.e(TAG, "Ha ocurrido un error ${it.message}")
            }
        return emptyList()
    }
    //endregion

    //region BBDD
    override suspend fun insertAllCommercesInDataBase(commerces: List<Commerce>) {
        commerceDao.insertCommerces(commerces.map {
            it.toEntity()
        })
    }

    override fun getCommerceById(id: Int): Flow<Commerce> {
        return commerceDao.getCommerceById(id).map {
            it.toDomain()
        }
    }

    override fun getCommercesDatabase(): Flow<List<Commerce>> {
        return  commerceDao.getAllCommerces().map { commerces->
            commerces.map { it.toDomain() }
        }
    }

    override suspend fun deleteAllCommercesDatabase() {
        commerceDao.deleteAllCommerces()
    }
    //endregion
}

