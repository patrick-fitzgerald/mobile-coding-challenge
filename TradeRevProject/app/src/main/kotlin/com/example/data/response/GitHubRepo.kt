package com.example.data.response

import com.google.gson.annotations.SerializedName

data class GitHubRepo(

    @SerializedName("id")
    val id: String = "",

    @SerializedName("description")
    val description: String = "",

    @SerializedName("name")
    val name: String = ""

)
