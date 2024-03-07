package com.jgcoding.kotlin.commercelistapp.domain.usecase

import com.jgcoding.kotlin.commercelistapp.domain.Repository
import com.jgcoding.kotlin.commercelistapp.domain.model.Commerce
import javax.inject.Inject

class SaveCommerceUseCase@Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(list: List<Commerce>) = repository.insertAllCommercesInDataBase(list)
}