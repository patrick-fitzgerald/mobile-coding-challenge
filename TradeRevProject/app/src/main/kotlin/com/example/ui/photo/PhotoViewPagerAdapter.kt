package com.example.ui.photo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.example.data.response.UnsplashPhoto
import com.example.databinding.PagerItemPhotoBinding

/**
 * Provides a view pager for the full screen photo viewer
 */
class PhotoViewPagerAdapter(private val list: List<UnsplashPhoto>) :
    PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int = list.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = LayoutInflater.from(container.context)
        val binding = PagerItemPhotoBinding.inflate(layoutInflater, container, false)
        binding.unsplashPhoto = list[position]
        container.addView(binding.root)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }

}