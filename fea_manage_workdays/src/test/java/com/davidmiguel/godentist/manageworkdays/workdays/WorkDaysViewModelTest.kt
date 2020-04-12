package com.davidmiguel.godentist.manageworkdays.workdays

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.davidmiguel.godentist.core.model.WorkDay
import com.davidmiguel.godentist.core.utils.ScreenState.*
import com.davidmiguel.godentist.testutils.MainCoroutineRule
import com.davidmiguel.godentist.testutils.assertEventTriggered
import com.davidmiguel.godentist.testutils.data.workdays.WorkDaysFakeRepository
import com.davidmiguel.godentist.testutils.getOrAwaitValue
import com.davidmiguel.godentist.testutils.observeForTesting
import com.google.common.truth.Truth.assertThat
import com.google.firebase.auth.FirebaseAuth
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class WorkDaysViewModelTest {

    // Subject under test
    private lateinit var workDaysViewModel: WorkDaysViewModel

    // Set the main coroutines dispatcher for unit testing.
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // Dependencies
    private lateinit var workDaysRepository: WorkDaysFakeRepository
    private val firebaseAuth = mockk<FirebaseAuth>().apply {
        every { uid } returns "fake"
    }

    @Before
    fun setupViewModel() {
        workDaysRepository = WorkDaysFakeRepository().apply {
            addWorkDays(
                WorkDay(id = "1"),
                WorkDay(id = "2")
            )
        }
        workDaysViewModel = WorkDaysViewModel(firebaseAuth, workDaysRepository)
    }

    @Test
    fun `Given 2 workdays in DB When start is called Then 2 workdays are shown`() {
        mainCoroutineRule.pauseDispatcher()

        workDaysViewModel.start()
        workDaysViewModel.workDays.observeForTesting {
            assertThat(workDaysViewModel.screenState.getOrAwaitValue()).isEqualTo(LOADING_DATA)

            mainCoroutineRule.resumeDispatcher()

            assertThat(workDaysViewModel.screenState.getOrAwaitValue()).isEqualTo(DATA_LOADED)
            assertThat(workDaysViewModel.workDays.getOrAwaitValue()).hasSize(2)
        }
    }

    @Test
    fun `Given no network When start is called Then error screen is shown`() {
        mainCoroutineRule.pauseDispatcher()
        workDaysRepository.setReturnError(true)

        workDaysViewModel.start()
        workDaysViewModel.workDays.observeForTesting {
            assertThat(workDaysViewModel.screenState.getOrAwaitValue()).isEqualTo(LOADING_DATA)

            mainCoroutineRule.resumeDispatcher()

            assertThat(workDaysViewModel.screenState.getOrAwaitValue()).isEqualTo(ERROR)
            assertThat(workDaysViewModel.workDays.getOrAwaitValue()).hasSize(0)
        }
    }

    @Test
    fun `When addNewWorkDay is called Then add work day screen is open`() {
        workDaysViewModel.addNewWorkDay()
        workDaysViewModel.addEditWorkDayEvent.assertEventTriggered("")
    }

    @Test
    fun `When editWorkDay is called Then edit work day screen is open`() {
        workDaysViewModel.editWorkDay(WorkDay(id = "1"))
        workDaysViewModel.addEditWorkDayEvent.assertEventTriggered("1")
    }
}
