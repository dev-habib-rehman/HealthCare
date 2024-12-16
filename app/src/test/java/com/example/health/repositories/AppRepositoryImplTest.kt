package com.example.health.repositories

import android.database.sqlite.SQLiteException
import com.example.health.MainDispatcherRule
import com.example.health.data.apiHelper.Result
import com.example.health.data.local.dao.AppDao
import com.example.health.data.local.entities.MedicineEntity
import com.example.health.data.local.entities.toDomain
import com.example.health.data.remote.apiService.NetworkService
import com.example.health.data.remote.models.MedicineDose
import com.example.health.data.remote.models.MedicineResponse
import com.example.health.data.remote.models.toEntities
import com.example.health.utils.Constants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class AppRepositoryImplTest {
    private lateinit var repository: AppRepositoryImpl
    private lateinit var mockDao: AppDao
    private lateinit var mockApiHelper: NetworkService

    // Coroutine Test Rule
    @get:Rule
    val coroutineTestRule = MainDispatcherRule()

    @Before
    fun setup() {
        mockDao = mock(AppDao::class.java)
        mockApiHelper = mock(NetworkService::class.java)
        repository = AppRepositoryImpl(mockDao, mockApiHelper)
    }

    @After
    fun tearDown() {
        Mockito.reset(mockDao, mockApiHelper)
    }

    @Test
    fun `getDataFromDatabase should return data from local database`() = runTest {
        // Arrange
        val mockData = listOf(
            MedicineEntity(
                id = 1, name = "Paracetamol", dose = "500mg", strength = "Moderate"
            )
        )
        `when`(mockDao.getAllMedicine()).thenReturn(mockData)
        // Act
        val result = repository.getDataFromDatabase()
        val expectedResult =
            Result.Success(MedicineResponse(medicines = mockData.map { it.toDomain() }), 200)
        // Assert
        assertEquals(expectedResult, result)
        verify(mockDao).getAllMedicine()
    }

    @Test
    fun `getDataApiCall should return data from API`() = runTest {
        // Arrange
        val mockApiResponse = Response.success(
            MedicineResponse(
                medicines = listOf(
                    MedicineDose(
                        id = 1, name = "Paracetamol", dose = "500mg", strength = "Moderate"
                    )
                )
            )
        )
        `when`(mockApiHelper.getMedicineDose()).thenReturn(mockApiResponse)
        // Act
        val result = repository.getDataApiCall()
        val expectedResult = Result.Success(mockApiResponse.body(), mockApiResponse.code())
        // Assert
        assertEquals(expectedResult, result)
        verify(mockApiHelper).getMedicineDose()
    }

    @Test
    fun `saveDataToDatabase should insert data into local database`() = runTest {
        // Arrange
        val mockData = MedicineResponse(
            medicines = listOf(
                MedicineDose(
                    id = 1, name = "Paracetamol", dose = "500mg", strength = "Moderate"
                )
            )
        )
        // Act
        repository.insertData(mockData)

        // Assert
        verify(mockDao).insertMedicine(mockData.toEntities())
    }

    @Test
    fun `getData should fetch data from API and save to database when database is empty`() =
        runTest {
            // Arrange
            `when`(mockDao.getAllMedicine()).thenReturn(emptyList()) // Database is empty
            val mockApiResponse = Response.success(
                MedicineResponse(
                    medicines = listOf(
                        MedicineDose(
                            id = 1, name = "Paracetamol", dose = "500mg", strength = "Moderate"
                        )
                    )
                )
            )
            `when`(mockApiHelper.getMedicineDose()).thenReturn(mockApiResponse)

            // Act
            val result = repository.getData()
            val expectedResult = Result.Success(mockApiResponse.body(), mockApiResponse.code())

            // Assert
            assertEquals(expectedResult, result.first()) // Verify data is fetched from API)
            mockApiResponse.body()?.toEntities()
                ?.let { verify(mockDao).insertMedicine(it) } // Verify data is saved to DB
            verify(mockApiHelper).getMedicineDose() // Verify API call
        }

    @Test
    fun `getDataApiCall should return failure when API call fails`() = runTest {
        // Arrange
        val errorMessage = "Server error"
        val errorResponse = Response.error<MedicineResponse>(
            500, ResponseBody.create(MediaType.parse("application/json"), errorMessage)
        )
        `when`(mockApiHelper.getMedicineDose()).thenReturn(errorResponse)

        // Act
        val result = repository.getDataApiCall()

        // Assert
        assertTrue(result is Result.Failure)
        assertEquals(500, (result as Result.Failure).errorCode)
        verify(mockApiHelper).getMedicineDose()
    }

    @Test
    fun `getDataFromDatabase should return failure when database operation fails`() = runTest {
        // Arrange
        `when`(mockDao.getAllMedicine()).thenThrow(SQLiteException(Constants.Database.DB_ERROR))

        // Act
        val result = repository.getDataFromDatabase()

        // Assert
        assertTrue(result is Result.Failure)
        assertEquals(Constants.Database.DB_ERROR, (result as Result.Failure).message)
        verify(mockDao).getAllMedicine()
    }

    @Test
    fun `getDataApiCall should return failure when API response body is null`() = runTest {
        // Arrange
        val mockApiResponse = Response.success<MedicineResponse>(null)
        `when`(mockApiHelper.getMedicineDose()).thenReturn(mockApiResponse)

        // Act
        val result = repository.getDataApiCall()

        // Assert
        assertTrue(result is Result.Failure)
        assertEquals(Constants.ApiError.API_NO_BODY, (result as Result.Failure).message)
        verify(mockApiHelper).getMedicineDose()
    }

    @Test
    fun `getDataFromDatabase should handle corrupted data gracefully`() = runTest {
        // Arrange
        val corruptedData = listOf(
            MedicineEntity(id = 0, name = "", dose = "500mg", strength = "Moderate")
        )
        `when`(mockDao.getAllMedicine()).thenReturn(corruptedData)

        // Act
        val result = repository.getDataFromDatabase()

        // Assert
        assertTrue(result is Result.Failure)
        assertEquals(Constants.Database.DB_CORRUPTED_DATA, (result as Result.Failure).message)
        verify(mockDao).getAllMedicine()
    }
}