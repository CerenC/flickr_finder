package com.cerenb.flickrfinder.domain.model

import com.cerenb.flickrfinder.ui.model.PhotoListItem

data class Photo(
    val id: String,
    val title: String,
    val owner: String,
    val secret: String,
    val server: String
) {

    /**
     * image url format is: https://live.staticflickr.com/{server-id}/{id}_{secret}_{size-suffix}.jpg
     * @see <a href="https://www.flickr.com/services/api/misc.urls.html">Photo Images URLs documentation</a>
     */
    fun getThumbnailImageUrl(): String {
        return "$IMAGE_BASE_URL$server/${id}_${secret}_t.jpg"
    }

    fun getFullPhotoImageUrl(): String {
        return "$IMAGE_BASE_URL$server/${id}_${secret}_b.jpg"
    }

    companion object {
        const val IMAGE_BASE_URL = "https://live.staticflickr.com/"
    }
}

fun Photo.toListItem() = PhotoListItem(
    id = id,
    title = title,
    thumbnailImageUrl = this.getThumbnailImageUrl(),
    fullPhotoImageUrl = this.getFullPhotoImageUrl()
)
