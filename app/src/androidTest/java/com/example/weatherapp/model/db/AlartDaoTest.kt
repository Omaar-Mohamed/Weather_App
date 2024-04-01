package com.example.weatherapp.model.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.weatherapp.model.dto.AlertDto
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.equalTo

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
class AlartDaoTest {
    lateinit var database: AppDatabase
    lateinit var dao: AlartDao

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    @Before
    fun setUp() {
         database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            AppDatabase::class.java
        ).build()
        dao = database.alartDao()
    }
    @After
    fun tearDown() {
            database.close()
    }

    @Test
    fun testInsertAlart_getAlertById() = runBlockingTest {


        val newAlertDto = AlertDto(5, "Location", "Temperature", "Description")
        val insertingResult = dao.insertAlart(newAlertDto)
        var lastInserted = dao.getLastInsertedRow().first()
        assertEquals(insertingResult, lastInserted?.id)
    }

    @Test
    fun testDeleteAlertById_getAlertById() = runBlockingTest {
        val newAlertDto = AlertDto(5, "Location", "Temperature", "Description")
        dao.insertAlart(newAlertDto)
        dao.deleteAlartById(5)
        var lastInserted = dao.getLastInsertedRow().first()
        assertEquals(null, lastInserted)
    }

    @Test
    fun testGetAllAlarts() = runBlockingTest {
        val newAlertDto = AlertDto(5, "Location", "Temperature", "Description")
        dao.insertAlart(newAlertDto)
        val allAlarts = dao.getAllAlarts().first()
        assertThat(allAlarts.size, equalTo(1))
    }

    //testLastInsertedRow
    @Test
    fun testLastInsertedRow() = runBlockingTest {
        val newAlertDto = AlertDto(5, "Location", "Temperature", "Description")
        dao.insertAlart(newAlertDto)
        val lastInsertedRow = dao.getLastInsertedRow().first()
        assertThat(lastInsertedRow?.id, equalTo(5))
    }




}