package com.cerenb.flickrfinder.domain.usecase

import androidx.paging.PagingData
import com.cerenb.flickrfinder.domain.model.Photo
import com.cerenb.flickrfinder.domain.repository.PhotosSearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPhotosUseCase @Inject constructor(
    private val photosSearchRepository: PhotosSearchRepository
) {

    fun execute(query: String): Flow<PagingData<Photo>> =
        photosSearchRepository.getPhotos(query)

}