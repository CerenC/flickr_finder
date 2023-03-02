package com.cerenb.flickrfinder.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.cerenb.flickrfinder.data.datasources.remote.model.toDomainModel
import com.cerenb.flickrfinder.domain.model.toListItem
import com.cerenb.flickrfinder.domain.repository.PhotosSearchRepository
import com.cerenb.flickrfinder.domain.usecase.GetPhotosUseCase
import com.cerenb.flickrfinder.test.FakeData
import com.cerenb.flickrfinder.test.MainCoroutineRule
import com.cerenb.flickrfinder.ui.adapter.PhotoDiffCallback
import com.cerenb.flickrfinder.ui.model.PhotoListItem
import com.cerenb.flickrfinder.ui.model.UiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
internal class PhotoSearchViewModelTest {

    @get:Rule
    var coroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var photosSearchRepository: PhotosSearchRepository
    private lateinit var getPhotosUseCase: GetPhotosUseCase
    private lateinit var viewModel: PhotoSearchViewModel

    private val photos = FakeData.photos
    private val testDispatcher = coroutineRule.testDispatcher

    @Before
    fun setUp() {
        photosSearchRepository = mock()
        getPhotosUseCase = GetPhotosUseCase(photosSearchRepository)
        viewModel = PhotoSearchViewModel(getPhotosUseCase, testDispatcher)
    }

    @Test
    fun `expose IDLE ui state when search term is empty`() {
        runTest(coroutineRule.testDispatcher) {

            whenever(photosSearchRepository.getPhotos(any())).thenReturn(
                flow {
                    emit(PagingData.from(photos))
                }
            )

            val results = mutableListOf<UiState<PagingData<PhotoListItem>>>()

            val job = launch {
                viewModel.onSearchQueryChanged("")
                viewModel.state.toList(results)
            }

            assertEquals(UiState.Idle, results.first())

            assertEquals(1, results.size)

            verify(photosSearchRepository, times(0)).getPhotos("")

            job.cancel()
        }
    }

    @Test
    fun `expose correct ui states in correct when photos are loaded successfully`() {
        runTest(coroutineRule.testDispatcher) {

            val differ = AsyncPagingDataDiffer(
                diffCallback = PhotoDiffCallback,
                updateCallback = fakeListUpdateCallback,
                mainDispatcher = testDispatcher,
                workerDispatcher = testDispatcher,
            )

            val queryText = "query"
            val pagingData = flow { emit(PagingData.from(photos)) }
            val results = mutableListOf<UiState<PagingData<PhotoListItem>>>()

            whenever(photosSearchRepository.getPhotos(queryText)).thenReturn(pagingData)

            val job = launch {
                viewModel.onSearchQueryChanged(queryText)
                viewModel.state.collectLatest {
                    when (it) {
                        is UiState.Success -> differ.submitData(it.data)
                        else -> results.add(it)
                    }
                }
            }

            advanceUntilIdle()

            assertEquals(UiState.Idle, results.first())

            assertEquals(UiState.Loading, results[1])

            val actualList = differ.snapshot()
            val expectedList = listOf(
                FakeData.photoResponse1.toDomainModel().toListItem(),
                FakeData.photoResponse2.toDomainModel().toListItem()
            )

            assertEquals(expectedList, actualList)

            verify(photosSearchRepository, times(1)).getPhotos(queryText)

            job.cancel()
        }
    }

    @Test
    fun `expose correct ui states in correct when error occurs at photo loading`() {
        runTest(coroutineRule.testDispatcher) {
            val queryText = "query"
            val error = RuntimeException("404 occur", Throwable())

            whenever(photosSearchRepository.getPhotos(any())).thenThrow(error)

            val results = mutableListOf<UiState<PagingData<PhotoListItem>>>()


            val job = launch {
                viewModel.onSearchQueryChanged(queryText)

                viewModel.state.toList(results)
            }

            advanceUntilIdle()

            assertEquals(UiState.Idle, results.first())
            assertEquals(UiState.Loading, results[1])

            val errorUiState = UiState.Error<Exception>(error = error)
            assertEquals(
                errorUiState.error.message,
                (results.last() as UiState.Error<*>).error.message
            )

            viewModel.onRetryClicked()
            advanceUntilIdle()

            verify(photosSearchRepository, times(2)).getPhotos(queryText)

            job.cancel()
        }
    }


    private val fakeListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}