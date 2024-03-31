import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp.favourit.viewmodel.FavouritViewModel
import com.example.weatherapp.model.AppRepo
import com.example.weatherapp.model.FakeRepo
import com.example.weatherapp.model.db.DbState
import com.example.weatherapp.model.db.FakeLocalDataSourse
import com.example.weatherapp.model.dto.AlertDto
import com.example.weatherapp.model.dto.FavLocations
import com.example.weatherapp.model.network.FakeRemoteDataSourse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FavouritViewModelTest {

    lateinit var viewModel: FavouritViewModel
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
        viewModel = FavouritViewModel(repo)
    }

    @After
    fun tearDown() {
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun testGetAllLocations_returnSize() {
        testScope.runBlockingTest {
            val expected = favLocationsList
            viewModel.getAllLocations()
            val resultState = viewModel.allLocations.first()
            val result = when (resultState) {
                is DbState.Success -> resultState.data
                else -> null
            }
            assertEquals(expected, result)
        }
    }

    @Test
    fun testInsertLocation_returnsize() {
        testScope.runBlockingTest {
            val expectedSize = favLocationsList.size + 1
            viewModel.insertLocation(FavLocations(5, 12.0, 13.0, "loc5"))

            // Collect the StateFlow to get its current value
            viewModel.getAllLocations()
            val currentState = viewModel.allLocations.first()

            val resultSize = when (currentState) {
                is DbState.Success -> currentState.data.size
                else -> 0 // Handle failure or loading state
            }

            assertEquals(expectedSize, resultSize)
        }
    }



    @Test
    fun testDeleteLocation_returnsize() {
        testScope.runBlockingTest {
            val locationToDelete = favLocationsList.first()
            val expectedSize = favLocationsList.size - 1
            viewModel.deleteLocation(locationToDelete)

            viewModel.getAllLocations()
            val currentState = viewModel.allLocations.first()

            val resultSize = when (currentState) {
                is DbState.Success -> currentState.data.size
                else -> 0
            }

            assertEquals(expectedSize, resultSize)
        }
    }


}
