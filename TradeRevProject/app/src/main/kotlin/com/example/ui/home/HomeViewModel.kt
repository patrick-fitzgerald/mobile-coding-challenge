package com.example.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.api.Status
import com.example.data.response.GitHubRepo
import com.example.repository.GithubRepository
import com.example.ui.base.BaseViewModel
import com.example.util.extensions.default
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeViewModel(
    private val githubRepository: GithubRepository
) : BaseViewModel() {

    val isLoading = MutableLiveData<Boolean>().default(true)
    val gitHubRepos = MutableLiveData<List<GitHubRepo>>()

    enum class ContextEvent {
        GET_REPOS_BUTTON_CLICKED,
    }

    val contextEventBus: PublishSubject<ContextEvent> = PublishSubject.create()

    fun onGetReposBtnClick() {
        contextEventBus.onNext(ContextEvent.GET_REPOS_BUTTON_CLICKED)
        getGitHubReposRequest()
    }

    // Sample HTTP request
    fun getGitHubReposRequest() {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.postValue(true)
            val response = githubRepository.getRepos()
            isLoading.postValue(false)
            viewModelScope.launch(Dispatchers.Main) {
                when (response.status) {
                    Status.SUCCESS -> {
                        if (response.data != null) {
                            Timber.d("getGitHubReposRequest response: ${response.data}")
                            gitHubRepos.value = response.data
                        } else {
                            Timber.e("getGitHubReposRequest ERROR")
                        }
                    }
                    Status.ERROR -> {
                        Timber.e("getGitHubReposRequest ERROR")
                    }
                }
            }
        }
    }
}
