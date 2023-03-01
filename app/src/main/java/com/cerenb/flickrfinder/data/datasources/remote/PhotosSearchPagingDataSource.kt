package com.cerenb.flickrfinder.data.datasources.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.cerenb.flickrfinder.data.datasources.remote.model.toDomainModel
import com.cerenb.flickrfinder.data.datasources.remote.service.PhotoSearchService
import com.cerenb.flickrfinder.domain.model.Photo

private const val STARTING_PAGE_INDEX = 1

class PhotosSearchPagingDataSource(
    private val service: PhotoSearchService,
    private val pageSize: Int,
    private val query: String
) : PagingSource<Int, Photo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val pageIndex = params.key ?: STARTING_PAGE_INDEX

        return try {
            val response = service.getPhotos(query, pageSize, pageIndex)
            val results = response.photos.photoResult.map { it.toDomainModel() }
            val isEndOfList = results.isEmpty()
            LoadResult.Page(
                data = results,
                prevKey = if (pageIndex == STARTING_PAGE_INDEX) null else pageIndex - 1,
                nextKey = if (isEndOfList) null else pageIndex + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}