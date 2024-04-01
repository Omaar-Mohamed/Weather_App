package com.example.weatherapp.model.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import org.junit.Before


class AppLocalDataSourseImpLTest {

    @Before
    fun setUp() {
        val database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            AppDatabase::class.java
        ).build()
//        val localDataSourse = AppLocalDataSourseImpL(database.alartDao())
    }


}