package com.jgcoding.kotlin.commercelistapp.domain.usecase

import com.jgcoding.kotlin.commercelistapp.domain.Repository
import com.jgcoding.kotlin.commercelistapp.domain.model.Commerce
import javax.inject.Inject

class GetCommercesUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke() = repository.getCommercesApi()

    /**
     * Mi intención aquí era comprobar si la lista de comercios no era vacía.
     * Si era vacía recuperaría del repositorio la base de datos almacenada si existiera
     */

//    {
//        val commerces = repository.getCommercesApi()

//        return if (commerces.isNotEmpty()) {
//            repository.deleteAllCommercesDatabase()
////            repository.insertAllCommercesInDataBase(commerces)
//            commerces
//        }
//        else {
////            repository.getCommercesDatabase()
//            repository.getCommercesApi()
//        }
//    }
}