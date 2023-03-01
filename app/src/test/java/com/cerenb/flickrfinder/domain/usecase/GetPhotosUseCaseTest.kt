package com.cerenb.flickrfinder.domain.usecase

import androidx.paging.PagingSource
import com.cerenb.flickrfinder.domain.model.Photo
import com.cerenb.flickrfinder.domain.repository.PhotosSearchRepository
import com.cerenb.flickrfinder.test.FakeData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever


class GetPhotosUseCaseTest {
    private lateinit var repository: PhotosSearchRepository
    private lateinit var useCase: GetPhotosUseCase

    @Before
    fun setUp() {
        repository = mock()
        useCase = GetPhotosUseCase(repository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when use case executed then should return photo paging data back`() = runTest {
        val query = "test"
        val photoSearchPagingData: PagingSource.LoadResult<Int, Photo> =
            PagingSource.LoadResult.Page(
                data = FakeData.photos,
                prevKey = null,
                nextKey = null
            )

        whenever(repository.getPhotos(query)).thenReturn(flow { photoSearchPagingData })
        useCase.execute(query).collectLatest {
            assertEquals(FakeData.photos, it)
        }
        verify(repository).getPhotos(query)
    }
}