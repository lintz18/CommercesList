package com.jgcoding.kotlin.commercelistapp.domain.usecase

import com.jgcoding.kotlin.commercelistapp.domain.Repository
import javax.inject.Inject

class GetCommercesDatabaseUseCase @Inject constructor(private val repository: Repository) {
    operator fun invoke() = repository.getCommercesDatabase()
}