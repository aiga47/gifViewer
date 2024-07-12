package com.aigak.gifviewer

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.aigak.gifviewer.networking.NetworkFactory
import com.aigak.gifviewer.networking.api.response.GifSearchResponse
import com.aigak.gifviewer.repository.GifSearchRepository
import com.aigak.gifviewer.viewmodel.GifSearchViewModel
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
class GifViewModelUnitTest {


    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainDispatcherRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var gifVm: GifSearchViewModel
    private lateinit var repository: GifSearchRepository

    @Before
    fun setUp() {
        repository = GifSearchRepository(Dispatchers.IO, NetworkFactory(mockk<Context>(relaxed = true), false).giphyApi())
        gifVm = GifSearchViewModel(repository)
    }

    @Test
    fun checkIfRequestCreatedCorrectly() = runTest {
        val response = repository.searchGifs("dog", 50)
        advanceUntilIdle()
        assertEquals(response.second, null) // no error
        assertEquals((response.first?.data?.size ?: 0) > 1, true) // dog contains dog gifs
        assertEquals(response.first?.pagination?.offset, 50)
    }

    @Test
    fun checkIfRequestKeyChanges() = runTest {
        val key = gifVm.searchKeyword
        gifVm.search("dog")
        advanceUntilIdle()
        assertEquals(key.value, "dog")
    }

    @Test
    fun checkIfRequestOffsetChanges() = runTest {
        val offset = gifVm.offset
        assertEquals(offset.value, 0)
        gifVm.search("dog")
        advanceUntilIdle()
        assertEquals(offset.value, 50)
        gifVm.search("dog")
        advanceUntilIdle()
        assertEquals(offset.value, 100)
    }

    @Test
    fun checkDropOffset() = runTest {
        val offset = gifVm.offset
        gifVm.search("dog")
        advanceUntilIdle()
        gifVm.search("dog")
        advanceUntilIdle()
        gifVm.search("cat")
        advanceUntilIdle()
        assertEquals(offset.value, 50)
    }

}