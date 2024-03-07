package com.jgcoding.kotlin.commercelistapp.domain.usecase

import com.jgcoding.kotlin.commercelistapp.domain.Repository
import javax.inject.Inject

class GetCommerceIdUseCase @Inject constructor(private val repository: Repository) {
    operator fun invoke(id: Int) = repository.getCommerceById(id)
}