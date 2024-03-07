package com.jgcoding.kotlin.commercelistapp.domain

import com.jgcoding.kotlin.commercelistapp.domain.model.Commerce
import com.jgcoding.kotlin.commercelistapp.domain.usecase.GetCommercesUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetCommercesUseCaseTest {

    @RelaxedMockK
    private lateinit var repository: Repository

    lateinit var getCommercesUseCase: GetCommercesUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        getCommercesUseCase = GetCommercesUseCase(repository)
    }

    @Test
    fun `when the api doesn't return anything then get values from database`() = runBlocking {
        //Given
       coEvery { repository.getCommercesApi() } returns emptyList()

        //When
        getCommercesUseCase()

        //Then
        coVerify(exactly = 1) { repository.getCommercesDatabase() }
    }

    @Test
    fun `when the api return something then get values from api`() = runBlocking {
        //Given
        val myList = listOf(
            Commerce(
                name = "test",
                photo = "",
                category = "test",
                cashback = 0.0,
                id = 1,
                address = "test",
                distance = 0,
                openingHours = "test",
                location = Pair(0.0, 0.0)
                )
        )
        coEvery { repository.getCommercesApi() } returns myList

        //When
        val response = getCommercesUseCase()

        //Then
        coVerify(exactly = 1) { repository.deleteAllCommercesDatabase() }
        coVerify(exactly = 1) { repository.insertAllCommercesInDataBase(any()) }
        coVerify(exactly = 0) { repository.getCommercesDatabase() }
        assert(response == myList)
    }
}