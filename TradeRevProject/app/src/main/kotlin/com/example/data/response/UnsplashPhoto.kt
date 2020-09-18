package com.example.data.response

data class UnsplashPhoto(

    val id: String = "",

    val width: Int = 0,

    val height: Int = 0,

    val urls: UnsplashPhotoUrl? = null

){
    fun thumbnailUrl(): String {
        urls?.let {
            return it.thumb
        }
        return ""
    }
}




data class UnsplashPhotoUrl(
    val full: String = "",
    val thumb: String = ""
)
