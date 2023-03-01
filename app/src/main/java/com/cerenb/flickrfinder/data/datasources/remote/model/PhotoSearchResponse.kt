package com.cerenb.flickrfinder.data.datasources.remote.model

import com.cerenb.flickrfinder.domain.model.Photo
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhotoSearchResponse(
    val stat: String,
    val photos: PhotosResponse
)

@JsonClass(generateAdapter = true)
data class PhotosResponse(
    val total: Int,
    @Json(name = "photo") val photoResult: List<PhotoResponse>
)

@JsonClass(generateAdapter = true)
data class PhotoResponse(
    val id: String,
    val title: String,
    val owner: String,
    val secret: String,
    val server: String
)

fun PhotoResponse.toDomainModel() = Photo(
    id = id,
    title = title,
    owner = owner,
    secret = secret,
    server = server
)