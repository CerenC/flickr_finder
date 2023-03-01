package com.cerenb.flickrfinder.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cerenb.flickrfinder.databinding.ItemPhotoBinding
import com.cerenb.flickrfinder.ui.extensions.loadImage
import com.cerenb.flickrfinder.ui.model.PhotoListItem

class PhotosAdapter(
    private val onItemClicked: (String) -> Unit
) : PagingDataAdapter<PhotoListItem, PhotosAdapter.ProductViewHolder>(PhotoDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onItemClicked
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val photo = getItem(position)
        holder.bind(photo)
    }

    class ProductViewHolder constructor(
        private val binding: ItemPhotoBinding,
        val onItemClicked: (id: String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(photoListItem: PhotoListItem?) {
            with(binding) {
                photoListItem?.let { item ->
                    binding.thumbnailImage.loadImage(
                        url = item.thumbnailImageUrl,
                        context = binding.root.context
                    )
                    binding.title.text = item.title
                    root.setOnClickListener {
                        onItemClicked(item.fullPhotoImageUrl)
                    }
                }
            }
        }
    }
}

object PhotoDiffCallback : DiffUtil.ItemCallback<PhotoListItem>() {
    override fun areItemsTheSame(oldItem: PhotoListItem, newItem: PhotoListItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PhotoListItem, newItem: PhotoListItem): Boolean {
        return oldItem.id == newItem.id
    }
}