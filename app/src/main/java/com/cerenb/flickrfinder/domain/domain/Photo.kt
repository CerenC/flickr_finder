package com.cerenb.flickrfinder.domain.domain

data class Photo(
    val id: String,
    val title: String,
    val owner: String,
    val secret: String,
    val server: String
)