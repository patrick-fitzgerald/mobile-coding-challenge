package com.example.ui.home

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.ui.photo.PhotoViewModel
import com.example.util.Constants.Companion.UNSPLASH_PHOTOS_FIRST_PAGE

class PaginationScrollListener(val photoViewModel :PhotoViewModel): RecyclerView.OnScrollListener() {

    private var pageNumber: Int = UNSPLASH_PHOTOS_FIRST_PAGE

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)

        val layoutManager = recyclerView.layoutManager as StaggeredGridLayoutManager?

        layoutManager?.let {
            val totalItemCount = it.itemCount
            val lastVisibleItemPositions =
                it.findLastVisibleItemPositions(IntArray(it.spanCount))

            val endOfList =
                lastVisibleItemPositions[0] + 1 >= totalItemCount || lastVisibleItemPositions[1] + 1 >= totalItemCount

            if (photoViewModel.isLoading.value != true && endOfList) {
                pageNumber += 1
                photoViewModel.getUnsplashPhotosRequest(pageNumber)
            }
        }
    }
}