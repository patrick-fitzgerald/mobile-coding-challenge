package com.example.util.binding

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.squareup.picasso.Picasso

@Suppress("unused")
object BindingAdapters {

    @JvmStatic
    @BindingAdapter("visible_if")
    fun visibleIf(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("image_url")
    fun setImageUrl(imageView: ImageView, imageUrl: String) {
        if (!imageUrl.isBlank()) {
            val circularProgressDrawable = CircularProgressDrawable(imageView.context)
            circularProgressDrawable.apply {
                strokeWidth = 8f
                centerRadius = 60f
                start()
            }
            Picasso.get().load(imageUrl).placeholder(circularProgressDrawable).into(imageView)
        }
    }
}
