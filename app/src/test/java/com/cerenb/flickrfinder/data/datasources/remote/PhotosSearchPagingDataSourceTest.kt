package com.cerenb.flickrfinder.data.datasources.remote

import androidx.paging.PagingSource
import com.cerenb.flickrfinder.data.datasources.remote.model.toDomainModel
import com.cerenb.flickrfinder.data.datasources.remote.service.PhotoSearchService
import com.cerenb.flickrfinder.domain.model.Photo
import com.cerenb.flickrfinder.test.FakeData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class PhotosSearchPagingDataSourceTest {
    private lateinit var photoSearchService: PhotoSearchService
    private lateinit var pagingSource: PhotosSearchPagingDataSource

    @Before
    fun setUp() {
        photoSearchService = mock()
        pagingSource = PhotosSearchPagingDataSource(photoSearchService, TEST_PAGE_SIZE, "test")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `load photos when successful load`() = runTest {
        whenever(
            photoSearchService.getPhotos(
                any(),
                any(),
                any()
            )
        ).thenReturn(FakeData.photoSearchResponse)

        val photos = FakeData.photoSearchResponse.photos.photoResult.map { it.toDomainModel() }
        val expectedPage: PagingSource.LoadResult<Int, Photo> = PagingSource.LoadResult.Page(
            data = listOf(photos[0], photos[1]),
            prevKey = null,
            nextKey = 2
        )

        val actual: PagingSource.LoadResult<Int, Photo> = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 2,
                placeholdersEnabled = false
            )
        )

        assertEquals(expectedPage, actual)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when error occurred while loading photos then throw exception`() = runTest {
        val error = RuntimeException("404", Throwable())
        whenever(photoSearchService.getPhotos(any(), any(), any())).thenThrow(error)
        val expectedResult = PagingSource.LoadResult.Error<Int, Photo>(error)

        assertEquals(
            expectedResult, pagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = 0,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            )
        )
    }

    companion object {
        const val TEST_PAGE_SIZE = 2
    }
}