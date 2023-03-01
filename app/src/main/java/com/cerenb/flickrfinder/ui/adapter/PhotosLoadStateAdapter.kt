package com.cerenb.flickrfinder.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cerenb.flickrfinder.databinding.ItemLoadStateBinding
import com.cerenb.flickrfinder.ui.UiErrorsMapper

class PhotosLoadStateAdapter(
    private val adapter: PhotosAdapter
) : LoadStateAdapter<PhotosLoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) =
        LoadStateViewHolder(
            ItemLoadStateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) =
        holder.bind(loadState)

    inner class LoadStateViewHolder(
        binding: ItemLoadStateBinding,
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {
        private val progressBar = binding.progressBar
        private val errorMsg = binding.errorMsg
        private val retry = binding.retryButton
            .also {
                it.setOnClickListener { adapter.retry() }
            }

        fun bind(loadState: LoadState) {
            progressBar.isVisible = loadState is LoadState.Loading
            retry.isVisible = loadState is LoadState.Error
            errorMsg.isVisible = !(loadState as? LoadState.Error)?.error?.message.isNullOrBlank()
            errorMsg.text =
                UiErrorsMapper(itemView.resources).map((loadState as? LoadState.Error)?.error)
        }
    }
}