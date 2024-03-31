package com.example.weatherapp.alert.viewModel


import androidx.arch.core.executor.testing.InstantTaskExecutorRule

import com.example.weatherapp.favourit.viewmodel.FavouritViewModel
import com.example.weatherapp.model.AppRepo
import com.example.weatherapp.model.FakeRepo
import com.example.weatherapp.model.db.AlertDbState
import com.example.weatherapp.model.db.DbState
import com.example.weatherapp.model.db.FakeLocalDataSourse
import com.example.weatherapp.model.dto.AlertDto
import com.example.weatherapp.model.dto.FavLocations
import com.example.weatherapp.model.network.FakeRemoteDataSourse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class AlertViewModelTest {

    lateinit var viewModel: AlertViewModel
    lateinit var repo: AppRepo
    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    private val favLocation = FavLocations(1, 12.0, 13.0, "loc1")
    private val favLocation2 = FavLocations(2, 12.0, 13.0, "loc2")
    private val favLocation3 = FavLocations(3, 12.0, 13.0, "loc3")
    private val favLocation4 = FavLocations(4, 12.0, 13.0, "loc4")
    private val favLocationsList = listOf(favLocation, favLocation2, favLocation3, favLocation4)

    private val alert1 = AlertDto(1, "loc1", "temp1", "desc1")
    private val alert2 = AlertDto(2, "loc2", "temp2", "desc2")
    private val alert3 = AlertDto(3, "loc3", "temp3", "desc3")
    private val alert4 = AlertDto(4, "loc4", "temp4", "desc4")
    private val localList = listOf(alert1, alert2, alert3, alert4)

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repo = FakeRepo(
            FakeLocalDataSourse(localList.toMutableList(), favLocationsList.toMutableList()),
            FakeRemoteDataSourse()
        )
        viewModel = AlertViewModel(repo)
    }

    @After
    fun tearDown() {
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun testInsertAlert_getLastInsertedRow_returnId() {
        testScope.runBlockingTest {
            val newAlertDto = AlertDto(5, "Location", "Temperature", "Description")
            val lastInsertedId = viewModel.insertAlert(newAlertDto)
            viewModel.getLastInsertedRow()
            val resultState = viewModel.lastAlertInserted.first()
            val lastInsertedRowId = resultState.let {
                when (it) {
                    is AlertDbState.LastInsertedRow -> it.data?.id
                    else -> null
                }
            }

            assertEquals(lastInsertedId, lastInsertedRowId)
        }
    }

    @Test
    fun testDeleteAlert_insertAlert_returnId() {
            testScope.runBlockingTest {
            val newAlertDto = AlertDto(5, "Location", "Temperature", "Description")
            val lastInsertedId = viewModel.insertAlert(newAlertDto)
                viewModel.deleteAlert(newAlertDto)

                val resultState = viewModel.lastAlertInserted.first()

            val lastInsertedRowId = resultState.let {
                when (it) {
                    is AlertDbState.LastInsertedRow -> it.data?.id
                    else -> null
                }
            }

            assertNotEquals(lastInsertedId, lastInsertedRowId)
        }
    }

    @Test
    fun testGetAllAlerts() {
        testScope.runBlockingTest {
            val expected = localList
            viewModel.getAllAlerts()
            val resultState = viewModel.alert.first()
            val result = when (resultState) {
                is AlertDbState.Success -> resultState.data
                else -> null
            }
            assertEquals(expected, result)
        }    }

    @Test
    fun testGetLastInsertedRow() {
        // Write your test for getLastInsertedRow() function here
        runBlockingTest {
            val newAlert = AlertDto(5, "loc5", "temp5", "desc5")
            val insertedId = viewModel.insertAlert(newAlert)
            viewModel.getLastInsertedRow()
            val resultState = viewModel.lastAlertInserted.first()
            val result = when (resultState) {
                is AlertDbState.LastInsertedRow -> resultState.data?.id
                else -> null
            }
            assertEquals(insertedId, result)
        }
    }

    @Test
    fun testDeleteAlertById() {
        // Write your test for deleteAlertById() function here
        runBlockingTest {
            val newAlert = AlertDto(5, "loc5", "temp5", "desc5")
            val insertedId = viewModel.insertAlert(newAlert)
            viewModel.deleteAlertById(insertedId)
            viewModel.getLastInsertedRow()
            val resultState = viewModel.lastAlertInserted.first()
            val result = when (resultState) {
                is AlertDbState.LastInsertedRow -> resultState.data?.id
                else -> null
            }
            assertNotEquals(insertedId, result)
        }
    }


}
