package com.example.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.data.response.UnsplashPhoto
import com.example.databinding.ListItemLoadingBinding
import com.example.databinding.ListItemUnsplashPhotoBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class PhotoClickListener(val clickListener: (unsplashPhoto: UnsplashPhoto) -> Unit) {
    fun onClick(unsplashPhoto: UnsplashPhoto) = clickListener(unsplashPhoto)
}

private const val ITEM_VIEW_TYPE_LOADING = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

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
                items = items + unsplashPhotos.map { DataItem.UnsplashPhotoItem(it) } + DataItem.LoadingItem()
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
        return when (viewType) {
            ITEM_VIEW_TYPE_LOADING -> LoadingViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ItemViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.LoadingItem -> ITEM_VIEW_TYPE_LOADING
            is DataItem.UnsplashPhotoItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    class ItemViewHolder private constructor(private val binding: ListItemUnsplashPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UnsplashPhoto, clickListener: PhotoClickListener) {
            binding.unsplashPhoto = item
            binding.clickListener = clickListener
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

    class LoadingViewHolder private constructor(binding: ListItemLoadingBinding) :
        RecyclerView.ViewHolder(binding.root) {


        companion object {
            fun from(parent: ViewGroup): LoadingViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemLoadingBinding.inflate(layoutInflater, parent, false)
                return LoadingViewHolder(binding)
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

    class LoadingItem : DataItem() {
        override val id = "loading"
    }

    abstract val id: String
}
