package com.example.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.data.response.UnsplashPhoto
import com.example.databinding.ListItemUnsplashPhotoBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PhotoClickListener(val clickListener: (unsplashPhoto: UnsplashPhoto) -> Unit) {
    fun onClick(unsplashPhoto: UnsplashPhoto) = clickListener(unsplashPhoto)
}

data class PhotoListData(
    val isLoading: Boolean?,
    val unsplashPhotos: List<UnsplashPhoto>?
)

class PhotoListAdapter(private val clickListener: PhotoClickListener) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(UnsplashPhotoDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun submitPhotoList(unsplashPhotos: List<UnsplashPhoto>?) {
        adapterScope.launch {

            var items: List<DataItem> = emptyList()
            if (unsplashPhotos != null) {
                items = items + unsplashPhotos.map { DataItem.UnsplashPhotoItem(it) }
            }

            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is ItemViewHolder -> {
                val unsplashPhotoItem = getItem(position) as DataItem.UnsplashPhotoItem
                holder.bind(unsplashPhotoItem.unsplashPhoto, clickListener)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder.from(parent)
    }


    class ItemViewHolder private constructor(private val binding: ListItemUnsplashPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Set Aspect Ratio in Staggered LayoutManager using Constraint Layout
        // https://medium.com/mindorks/aspect-ratio-in-staggered-layoutmanager-using-constraint-layout-9845d04d1962
        private val set = ConstraintSet()


        fun bind(item: UnsplashPhoto, clickListener: PhotoClickListener) {
            binding.unsplashPhoto = item
            binding.clickListener = clickListener
            val thumbnailUrl = item.thumbnailUrl()
            if (thumbnailUrl.isNotEmpty()) {
                Picasso.get().load(thumbnailUrl).into(binding.photoThumbnail)
                val ratio = String.format("%d:%d", item.width, item.height)
                set.clone(binding.photoThumbnailContainer)
                set.setDimensionRatio(binding.photoThumbnail.id, ratio)
                set.applyTo(binding.photoThumbnailContainer)
            }

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemUnsplashPhotoBinding.inflate(layoutInflater, parent, false)
                return ItemViewHolder(binding)
            }
        }
    }


}

class UnsplashPhotoDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }
}

sealed class DataItem {
    data class UnsplashPhotoItem(val unsplashPhoto: UnsplashPhoto) : DataItem() {
        override val id = unsplashPhoto.id
    }


    abstract val id: String
}
