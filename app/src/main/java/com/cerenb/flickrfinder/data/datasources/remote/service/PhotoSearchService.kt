package com.cerenb.flickrfinder.data.datasources.remote.service

import com.cerenb.flickrfinder.data.datasources.remote.model.PhotoSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotoSearchService {

    @GET("?format=json&nojsoncallback=1&method=flickr.photos.search")
    suspend fun getPhotos(
        @Query("text") searchTerm: String,
        @Query("per_page") pageSize: Int,
        @Query("page") page: Int
    ): PhotoSearchResponse

}