package com.example.health.ui.viewmodels

import app.cash.turbine.test
import com.example.health.MainDispatcherRule
import com.example.health.data.apiHelper.Result
import com.example.health.data.remote.models.MedicineDose
import com.example.health.data.remote.models.MedicineResponse
import com.example.health.repositories.AppRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class)
class AppVMTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: AppVM
    private lateinit var mockRepository: AppRepository

    @Before
    fun setUp() {
        mockRepository = mock(AppRepository::class.java)
        runBlocking {
            `when`(mockRepository.getData()).thenReturn(flowOf(Result.Loading()))
        }
        viewModel = AppVM(mockRepository)
    }

    @Test
    fun `initial state should be Loading`() = runTest {
        // Arrange
        runBlocking {
            `when`(mockRepository.getData()).thenReturn(flowOf(Result.Loading()))
        }
        viewModel = AppVM(mockRepository)
        // Act
        val initialState = viewModel.appDataState.first()
        // Assert
        assertEquals(Result.Loading<String>(), initialState)
    }


    @Test
    fun `getData should emit Success when repository returns Success`() = runTest {
        // Arrange
        val mockResponse = MedicineResponse(
            medicines = listOf(MedicineDose(1, "Paracetamol", "500mg", "Moderate"))
        )
        `when`(mockRepository.getData()).thenReturn(flowOf(Result.Success(mockResponse, 200)))
        // Act
        viewModel.getData()
        // Assert
        viewModel.appDataState.test {
            assertEquals(Result.Success(mockResponse, 200), awaitItem()) // Success state
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getData should emit Failure when repository returns Failure`() = runTest {
        // Arrange
        val mockError = Exception("Network Error")
        `when`(mockRepository.getData()).thenReturn(
            flowOf(Result.Failure("Network Error", mockError, 500, null))
        )

        // Act
        viewModel.getData()

        // Assert
        viewModel.appDataState.test {
            assertEquals(
                Result.Failure<MedicineResponse>("Network Error", mockError, 500, null), awaitItem()
            ) // Failure state
            cancelAndIgnoreRemainingEvents()
        }
    }
}