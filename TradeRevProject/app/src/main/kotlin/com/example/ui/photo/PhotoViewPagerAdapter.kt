package com.example.ui.photo

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.data.response.UnsplashPhoto
import com.example.databinding.PagerItemPhotoBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PhotoViewPagerAdapter :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(UnsplashPhotoDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun submitPhotoList(list: List<UnsplashPhoto>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> emptyList()
                else -> list.map { DataItem.UnsplashPhotoItem(it) }
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
                holder.bind(unsplashPhotoItem.unsplashPhoto)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder.from(parent)
    }

    class ItemViewHolder private constructor(private val binding: PagerItemPhotoBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UnsplashPhoto) {
            binding.unsplashPhoto = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PagerItemPhotoBinding.inflate(layoutInflater, parent, false)
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
        return oldItem == newItem
    }
}

sealed class DataItem {
    data class UnsplashPhotoItem(val unsplashPhoto: UnsplashPhoto) : DataItem() {
        override val id = unsplashPhoto.id
    }

    abstract val id: String
}
