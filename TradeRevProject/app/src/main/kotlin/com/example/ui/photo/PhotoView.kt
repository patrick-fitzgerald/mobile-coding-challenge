package com.example.ui.photo

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.R
import com.example.data.response.UnsplashPhoto

class PhotoView @JvmOverloads constructor(
    container: ViewGroup?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(container?.context, attrs, defStyleAttr) {

    init {
        val layoutInflater =
            getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = layoutInflater.inflate(R.layout.item_photo, container, false)
        view.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
//        viewBinding = ItemPhotoBinding.inflate(layoutInflater, this, false)
    }

    fun bind(unsplashPhoto: UnsplashPhoto) {
//        val imageUrl = unsplashPhoto.fullUrl()
//        if (!imageUrl.isBlank()) {
//            Picasso.get().load(imageUrl).into(viewBinding.photoView)
//        }
    }
}
