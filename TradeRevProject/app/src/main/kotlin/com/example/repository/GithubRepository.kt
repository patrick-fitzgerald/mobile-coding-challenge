package com.example.repository

import com.example.api.GithubApi
import com.example.api.Resource
import com.example.data.response.GitHubRepo
import timber.log.Timber

class GithubRepository constructor(
    private val githubApi: GithubApi
) {

    suspend fun getRepos(): Resource<List<GitHubRepo>> {
        return try {
            val response = githubApi.repos()
            Timber.d("getRepos response=$response")
            Resource.success(data = response)
        } catch (exception: Exception) {
            Timber.e("getRepos exception=$exception")
            Resource.error(msg = exception.message ?: "An error occurred", data = null)
        }
    }
}
