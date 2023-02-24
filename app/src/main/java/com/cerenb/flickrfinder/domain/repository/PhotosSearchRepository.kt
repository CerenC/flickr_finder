package com.cerenb.flickrfinder.domain.repository

import com.cerenb.flickrfinder.domain.domain.Photo

interface PhotosSearchRepository {
    fun getPhotos(query : String) : Photo
}