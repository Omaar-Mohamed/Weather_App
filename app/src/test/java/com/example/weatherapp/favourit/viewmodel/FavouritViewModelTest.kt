package com.example.weatherapp.favourit.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.model.AppRepo
import com.example.weatherapp.model.FakeRepo
import com.example.weatherapp.model.dto.AlertDto
import com.example.weatherapp.model.dto.FavLocations
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
class FavouritViewModelTest{
    lateinit var viewModel: FavouritViewModel
    lateinit var repo  : AppRepo
    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)
    val favLocation = FavLocations(1, 12.0, 13.0, "loc1")
    val favLocation2 = FavLocations(2, 12.0, 13.0, "loc2")
    val favLocation3 = FavLocations(3, 12.0, 13.0, "loc3")
    val favLocation4 = FavLocations(4, 12.0, 13.0, "loc4")
    private val favLocationsList = listOf(favLocation, favLocation2, favLocation3, favLocation4)

    private val alert1 = AlertDto(1, "loc1", "temp1", "desc1")
    private val alert2 = AlertDto(2, "loc2", "temp2", "desc2")
    private val alert3 = AlertDto(3, "loc3", "temp3", "desc3")
    private val alert4 = AlertDto(4, "loc4", "temp4", "desc4")
    private val localList = listOf(alert1, alert2, alert3, alert4).toMutableList()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup(){
        Dispatchers.setMain(testDispatcher)
//        repo = FakeRepo(favLocationsList , localList)
        viewModel = FavouritViewModel(repo)
    }
    @After
    fun tearDown(){
        testDispatcher.cleanupTestCoroutines()
    }
    @Test
    fun getAllLocations() = testScope.runBlockingTest{
        val favLocation = FavLocations(1, 12.0, 13.0, "loc1")
        viewModel.insertLocation(favLocation)
        viewModel.getAllLocations()
        val result = viewModel.allLocations.value
    assertEquals(notNullValue(), result)
    }


}