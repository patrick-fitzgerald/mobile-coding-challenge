package com.example.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.api.Status
import com.example.data.response.UnsplashPhoto
import com.example.repository.UnsplashRepository
import com.example.ui.base.BaseViewModel
import com.example.util.extensions.default
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeViewModel(
    private val unsplashRepository: UnsplashRepository
) : BaseViewModel() {

    val isLoading = MutableLiveData<Boolean>().default(true)
    val unsplashPhotos = MutableLiveData<List<UnsplashPhoto>>()

    enum class ContextEvent {
        SCROLL_NEXT_PAGE_EVENT,
    }

    val contextEventBus: PublishSubject<ContextEvent> = PublishSubject.create()


    // Sample HTTP request
    fun getUnsplashPhotosRequest() {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.postValue(true)
            val response = unsplashRepository.getPhotos()
            isLoading.postValue(false)
            viewModelScope.launch(Dispatchers.Main) {
                when (response.status) {
                    Status.SUCCESS -> {
                        if (response.data != null) {
                            Timber.d("getUnsplashPhotosRequest response: ${response.data}")
                            unsplashPhotos.value = response.data
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
