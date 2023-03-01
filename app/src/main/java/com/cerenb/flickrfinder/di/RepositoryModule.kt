package com.cerenb.flickrfinder.di

import com.cerenb.flickrfinder.data.repository.PhotosSearchRepositoryImpl
import com.cerenb.flickrfinder.domain.repository.PhotosSearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindRepository(
        photosSearchRepositoryImpl: PhotosSearchRepositoryImpl
    ): PhotosSearchRepository

}