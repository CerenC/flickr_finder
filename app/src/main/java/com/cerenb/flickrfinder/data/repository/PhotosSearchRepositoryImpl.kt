package com.cerenb.flickrfinder.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.cerenb.flickrfinder.data.datasources.remote.PhotosSearchPagingDataSource
import com.cerenb.flickrfinder.data.datasources.remote.service.PhotoSearchService
import com.cerenb.flickrfinder.domain.model.Photo
import com.cerenb.flickrfinder.domain.repository.PhotosSearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PhotosSearchRepositoryImpl @Inject constructor(
    private val photoSearchService: PhotoSearchService
) : PhotosSearchRepository {
    override fun getPhotos(query: String): Flow<PagingData<Photo>> = Pager(
        config = PagingConfig(enablePlaceholders = false, pageSize = PAGE_SIZE),
        pagingSourceFactory = {
            PhotosSearchPagingDataSource(
                service = photoSearchService,
                pageSize = PAGE_SIZE,
                query = query
            )
        }
    ).flow

    companion object {
        private const val PAGE_SIZE = 25
    }

}