package com.cerenb.flickrfinder.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.cerenb.flickrfinder.di.IoDispatcher
import com.cerenb.flickrfinder.domain.model.toListItem
import com.cerenb.flickrfinder.domain.usecase.GetPhotosUseCase
import com.cerenb.flickrfinder.ui.model.PhotoListItem
import com.cerenb.flickrfinder.ui.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class PhotoSearchViewModel @Inject constructor(
    private val getPhotosUseCase: GetPhotosUseCase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private var searchJob: Job? = null

    private val _state = MutableStateFlow<UiState<PagingData<PhotoListItem>>>(UiState.Idle)
    val state: StateFlow<UiState<PagingData<PhotoListItem>>> = _state
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(SHARE_STOP_MILLIS),
            UiState.Idle
        )

    private val queryFlow = MutableStateFlow("")

    fun onSearchQueryChanged(query: String) {
        if (queryFlow.value != query) {
            queryFlow.value = query
            getPhotos()
        }
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun getPhotos() {
        searchJob?.cancel()

        searchJob = queryFlow.debounce(DEBOUNCE_MILLIS)
            .filter { searchQuery ->
                if (searchQuery.isEmpty()) {
                    _state.value = UiState.Idle
                    return@filter false
                } else {
                    return@filter true
                }
            }
            .distinctUntilChanged()
            .flatMapLatest { searchQuery ->
                _state.value = UiState.Loading

                getPhotosUseCase.execute(searchQuery)
                    .cachedIn(viewModelScope)
                    .map { pagingData ->
                        pagingData.map { photo ->
                            photo.toListItem()
                        }
                    }
            }
            .catch { _state.value = UiState.Error(it as Exception) }
            .flowOn(dispatcher)
            .onEach { _state.value = UiState.Success(it) }
            .launchIn(viewModelScope)
    }

    fun onRetryClicked() {
        getPhotos()
    }

    private companion object {
        private const val SHARE_STOP_MILLIS: Long = 5000
        private const val DEBOUNCE_MILLIS: Long = 400
    }

}