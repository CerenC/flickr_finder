package com.cerenb.flickrfinder.domain.repository

import androidx.paging.PagingData
import com.cerenb.flickrfinder.domain.model.Photo
import kotlinx.coroutines.flow.Flow

interface PhotosSearchRepository {
    fun getPhotos(query: String): Flow<PagingData<Photo>>
}