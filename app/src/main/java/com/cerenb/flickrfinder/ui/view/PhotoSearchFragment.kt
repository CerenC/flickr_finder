package com.cerenb.flickrfinder.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import com.cerenb.flickrfinder.R
import com.cerenb.flickrfinder.databinding.FragmentPhotoSearchBinding
import com.cerenb.flickrfinder.ui.UiErrorsMapper
import com.cerenb.flickrfinder.ui.adapter.PhotosAdapter
import com.cerenb.flickrfinder.ui.adapter.PhotosLoadStateAdapter
import com.cerenb.flickrfinder.ui.extensions.withCustomDrawable
import com.cerenb.flickrfinder.ui.model.UiState
import com.cerenb.flickrfinder.ui.model.UiViewIndex
import com.cerenb.flickrfinder.ui.viewmodel.PhotoSearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PhotoSearchFragment : Fragment(R.layout.fragment_photo_search) {
    private var _binding: FragmentPhotoSearchBinding? = null
    private val binding: FragmentPhotoSearchBinding
        get() = requireNotNull(_binding)

    private val viewModel: PhotoSearchViewModel by viewModels()

    private val photosAdapter by lazy { PhotosAdapter { url -> onListItemClick(url) } }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoSearchBinding.inflate(inflater, container, false)

        binding.photoList.adapter = photosAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
        setupListener()
    }

    private fun setupListener() {
        binding.searchText.addTextChangedListener {

                viewModel.onSearchQueryChanged(query = it.toString())

        }
    }

    private fun setupUi() {
        with(binding.photoList) {
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
                .withCustomDrawable(requireContext(), R.drawable.divider)
                .also {
                    addItemDecoration(it)
                }
        }

        binding.photoList.adapter = photosAdapter.withLoadStateHeaderAndFooter(
            header = PhotosLoadStateAdapter(photosAdapter),
            footer = PhotosLoadStateAdapter(photosAdapter)
        )

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest {
                    when (it) {
                        UiState.Idle -> {
                            binding.viewStateOptions.displayedChild = UiViewIndex.CHILD_IDLE
                        }
                        UiState.Loading -> {
                            binding.viewStateOptions.displayedChild = UiViewIndex.CHILD_LOADING
                        }
                        is UiState.Success -> {
                            binding.viewStateOptions.displayedChild = UiViewIndex.CHILD_CONTENT
                            photosAdapter.submitData(it.data)
                        }
                        is UiState.Error -> {
                            setupErrorUi(it.error)
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            photosAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.Error }
                .collectLatest { loadStates ->
                    binding.photoList.scrollToPosition(0)
                    if (loadStates.refresh is LoadState.Error) {
                        setupErrorUi((loadStates.refresh as LoadState.Error).error)
                    }
                }
        }
    }

    private fun onListItemClick(fullPhotoImageUrl: String) {
        findNavController().navigate(
            PhotoSearchFragmentDirections.actionPhotoSearchToFullPhotoFragment(
                fullPhotoImageUrl
            )
        )
    }

    private fun setupErrorUi(error: Throwable?) {
        binding.viewStateOptions.displayedChild = UiViewIndex.CHILD_ERROR
        binding.errorView.showError(
            UiErrorsMapper(resources).map(error)
        ) { onRetryClicked() }
    }

    private fun onRetryClicked() {
        viewModel.onRetryClicked()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}