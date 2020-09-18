package com.example.util.binding

import android.view.View
import androidx.databinding.BindingAdapter

@Suppress("unused")
object BindingAdapters {

    @JvmStatic
    @BindingAdapter("visible_if")
    fun visibleIf(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }
}
