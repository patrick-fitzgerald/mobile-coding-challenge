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

    val isLoading = MutableLiveData<Boolean>().default(true)
    val listViewIsLoading = MutableLiveData<Boolean>().default(false)
    val unsplashPhotos = MutableLiveData<List<UnsplashPhoto>>()

    // MediatorLiveData to combine two MutableLiveData objects
    val photoListData = MediatorLiveData<PhotoListData>()

    init {
        photoListData.addSource(isLoading) {
            photoListData.value = PhotoListData(isLoading = isLoading.value, unsplashPhotos = null)
        }
        photoListData.addSource(unsplashPhotos) {
            photoListData.value = PhotoListData(isLoading = null, unsplashPhotos = unsplashPhotos.value)
        }
    }


    val selectedPhoto = MutableLiveData<UnsplashPhoto>()


    fun selectedPosition(): Int? {
        return unsplashPhotos.value?.indexOf(selectedPhoto.value)
    }

    // Sample HTTP request
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
