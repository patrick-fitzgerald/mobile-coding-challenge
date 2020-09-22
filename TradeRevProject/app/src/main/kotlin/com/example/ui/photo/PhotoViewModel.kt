package com.example.ui.photo

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.api.Status
import com.example.data.response.UnsplashPhoto
import com.example.repository.UnsplashRepository
import com.example.ui.base.BaseViewModel
import com.example.ui.home.PhotoListData
import com.example.util.extensions.default
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class PhotoViewModel(private val unsplashRepository: UnsplashRepository) : BaseViewModel() {

    /**
     * Displays spinner when the first page is loading.
     */
    val isLoading = MutableLiveData<Boolean>().default(true)

    /**
     * Displays list view spinner at the bottom of the view when the next page of photos is loading.
     */
    val listViewIsLoading = MutableLiveData<Boolean>().default(false)

    /**
     * Unsplash photos.
     */
    val unsplashPhotos = MutableLiveData<List<UnsplashPhoto>>()

    /**
     * MediatorLiveData to combine two MutableLiveData objects: isLoading + unsplashPhotos.
     */
    val photoListData = MediatorLiveData<PhotoListData>()

    init {
        photoListData.addSource(isLoading) {
            photoListData.value = PhotoListData(isLoading = isLoading.value, unsplashPhotos = null)
        }
        photoListData.addSource(unsplashPhotos) {
            photoListData.value = PhotoListData(isLoading = null, unsplashPhotos = unsplashPhotos.value)
        }
    }


    /**
     * A user selects a photo when they click on a photo in the list of photos.
     */
    val selectedPhoto = MutableLiveData<UnsplashPhoto>()
    /**
     * A can scroll to a photo by paging in the photo viewer.
     */
    val scrolledToPhoto = MutableLiveData<UnsplashPhoto>()


    fun selectedPhotoPosition(): Int? {
        return unsplashPhotos.value?.indexOf(selectedPhoto.value)
    }

    fun scrolledToPhotoPosition(): Int? {
        return unsplashPhotos.value?.indexOf(scrolledToPhoto.value)
    }

    // Request Photos from the Unsplash API
    fun getUnsplashPhotosRequest(pageNumber: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            listViewIsLoading.postValue(true)
            val response = unsplashRepository.getPhotos(pageNumber = pageNumber)
            listViewIsLoading.postValue(false)
            isLoading.postValue(false)
            viewModelScope.launch(Dispatchers.Main) {
                when (response.status) {
                    Status.SUCCESS -> {
                        if (response.data != null) {
                            Timber.d("getUnsplashPhotosRequest response: ${response.data}")
                            var items: List<UnsplashPhoto> = unsplashPhotos.value ?: emptyList()
                            items = items + response.data
                            unsplashPhotos.value = items
                        } else {
                            Timber.e("getUnsplashPhotosRequest ERROR")
                        }
                    }
                    Status.ERROR -> {
                        Timber.e("getUnsplashPhotosRequest ERROR")
                    }
                }
            }
        }
    }
}
