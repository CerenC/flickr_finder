package com.cerenb.flickrfinder.test

import com.cerenb.flickrfinder.data.datasources.remote.model.PhotoResponse
import com.cerenb.flickrfinder.data.datasources.remote.model.PhotoSearchResponse
import com.cerenb.flickrfinder.data.datasources.remote.model.PhotosResponse
import com.cerenb.flickrfinder.data.datasources.remote.model.toDomainModel

object FakeData {

    val photoResponse1 = PhotoResponse(
        id = "id1",
        title = "title 1",
        owner = "owner1",
        secret = "secret1",
        server = "server1"
    )

    val photoResponse2 = PhotoResponse(
        id = "id2",
        title = "title 2",
        owner = "owner2",
        secret = "secret2",
        server = "server2"
    )

    val photosResponse = PhotosResponse(
        total = 100,
        photoResult = listOf(photoResponse1, photoResponse2)
    )

    val photoSearchResponse = PhotoSearchResponse(
        stat = "OK",
        photos = photosResponse
    )

    val photos = photoSearchResponse.photos.photoResult.map { it.toDomainModel() }

}