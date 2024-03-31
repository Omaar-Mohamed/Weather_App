package com.example.weatherapp.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp.model.db.AppLocalDataSourse
import com.example.weatherapp.model.db.FakeLocalDataSourse
import com.example.weatherapp.model.dto.AlertDto
import com.example.weatherapp.model.dto.FavLocations
import com.example.weatherapp.model.network.AppRemoteDataSourse
import com.example.weatherapp.model.network.FakeRemoteDataSourse
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotSame
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class AppRepoImplTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val alert1 = AlertDto(1, "loc1", "temp1", "desc1")
    private val alert2 = AlertDto(2, "loc2", "temp2", "desc2")
    private val alert3 = AlertDto(3, "loc3", "temp3", "desc3")
    private val alert4 = AlertDto(4, "loc4", "temp4", "desc4")
    private val localList = listOf(alert1, alert2, alert3, alert4)

    private val Location1 = FavLocations(1, 12.0,13.0,"lon1")
    private val Location2 = FavLocations(2, 12.0,13.0,"lon2")
    private val favLocationsList = listOf(Location1, Location2)

    private lateinit var fakeRemoteDataSourse: AppRemoteDataSourse
    private lateinit var fakeLocalDataSourse: AppLocalDataSourse
    private lateinit var appRepoImpl: AppRepoImpl

    @Before
    fun setup() {
        fakeRemoteDataSourse = FakeRemoteDataSourse()
        fakeLocalDataSourse = FakeLocalDataSourse(localList.toMutableList() , favLocationsList.toMutableList())
        appRepoImpl = AppRepoImpl.getInstance(fakeRemoteDataSourse, fakeLocalDataSourse)
    }

    @Test
    fun testGetAllAlerts() = runBlockingTest {
        val resultFlow = appRepoImpl.getAllAlarts()
        val result = resultFlow.first()
        assertEquals(localList, result)
    }
    @Test
    fun testInsertAlert_getLastInsertedRow() {
        runBlockingTest {
            val newAlert = AlertDto(5, "loc5", "temp5", "desc5")
            val insertedId = appRepoImpl.insertAlart(newAlert)
            val resultGetById = appRepoImpl.getLastInsertedRow().firstOrNull()
            assertEquals(insertedId, resultGetById?.id)
//            assertEquals(5L, insertedId)
        }
    }

    @Test
    fun testinsertNewAlert_DeleteAlert() {
        runBlockingTest {
            val newAlert = AlertDto(5, "loc5", "temp5", "desc5")
            val insertedId = appRepoImpl.insertAlart(newAlert)
            appRepoImpl.deleteAlart(newAlert)
            val resultGetById = appRepoImpl.getLastInsertedRow().firstOrNull()
            assertNotSame(insertedId, resultGetById?.id)

        }
    }

    @Test
    fun testinsertNewAlert_DeleteAlertById() {
        runBlockingTest {
            val newAlert = AlertDto(5, "loc5", "temp5", "desc5")
            val insertedId = appRepoImpl.insertAlart(newAlert)
            appRepoImpl.deleteAlartById(newAlert.id)
            val resultGetById = appRepoImpl.getLastInsertedRow().firstOrNull()
            assertNotSame(insertedId, resultGetById?.id)

        }
    }

    @Test
    fun testInsertLocation() {
        runBlockingTest {
            var newLocation = FavLocations(4, 12.0,13.0,"lon3")
            val insertedId = appRepoImpl.insertLocation(newLocation)
            assertEquals(newLocation.id.toLong(), insertedId)

        }
    }


}
