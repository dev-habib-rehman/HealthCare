package com.example.health

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.health.data.local.dao.AppDao
import com.example.health.data.local.database.AppDatabase
import com.example.health.data.local.entities.MedicineEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class AppDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var medicineDao: AppDao

    @Before
    fun setup() {
        // Create an in-memory database for testing
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), AppDatabase::class.java
        ).allowMainThreadQueries() // Allow main thread queries for testing
            .build()

        // Get the DAO from the database
        medicineDao = db.medicineDao()
    }

    @After
    fun tearDown() {
        // Close the database after tests are done
        db.close()
        Dispatchers.resetMain()
    }

    @Test
    fun insertShouldAddDataToDatabase() = runTest {
        // Arrange: Create a sample MedicineEntity
        val medicine =
            listOf(MedicineEntity(id = 1, name = "Aspirin", dose = "500mg", strength = "Strong"),
                MedicineEntity(id = 2, name = "Panadol", dose = "500mg", strength = "Strong"))

        // Act: Insert the data into the database
        medicineDao.insertMedicine(medicine)

        // Assert: Verify the data is inserted correctly
        val result = medicineDao.getAllMedicine()
        assertTrue(result.containsAll(medicine))
    }

    @Test
    fun getAllMedicineShouldReturnAllMedicinesFromTheDatabase() = runTest {
        // Arrange: Insert sample data
        val medicine1 = MedicineEntity(id = 1, name = "Aspirin", dose = "500mg", strength = "Strong")
        val medicine2 = MedicineEntity(id = 2, name = "Paracetamol", dose = "250mg", strength = "Medium")

        medicineDao.insertMedicine(listOf(medicine1, medicine2))

        // Act: Retrieve all medicines
        val result = medicineDao.getAllMedicine()

        // Assert: Verify the data retrieved is correct
        assertEquals(2, result.size)
        assertTrue(result.contains(medicine1))
        assertTrue(result.contains(medicine2))
    }
}