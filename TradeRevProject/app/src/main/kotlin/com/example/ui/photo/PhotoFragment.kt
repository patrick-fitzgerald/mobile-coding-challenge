package com.example.ui.photo

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import androidx.viewpager.widget.ViewPager.SCROLL_STATE_IDLE
import com.example.databinding.FragmentPhotoBinding
import com.example.ui.base.BaseFragment
import com.example.util.autoCleared
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PhotoFragment : BaseFragment() {

    private var viewBinding by autoCleared<FragmentPhotoBinding>()
    private val photoViewModel: PhotoViewModel by sharedViewModel()

    private var viewPagerPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewBinding = FragmentPhotoBinding.inflate(inflater, container, false)
        viewBinding.photoViewModel = photoViewModel
        viewBinding.lifecycleOwner = this

        // reset scrolledToPhoto position
        photoViewModel.scrolledToPhoto.value = null

        val selectedPosition = photoViewModel.selectedPhotoPosition()
        viewBinding.photoViewPager.apply {

            adapter = PhotoViewPagerAdapter(photoViewModel.unsplashPhotos.value ?: emptyList())
            if (selectedPosition != null) {
                setCurrentItem(selectedPosition, false)
                transitionName = photoViewModel.selectedPhoto.value?.id
            }

            addOnPageChangeListener(object : OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    viewPagerPosition = position
                }

                override fun onPageScrollStateChanged(state: Int) {
                    if (state == SCROLL_STATE_IDLE) {
                        // Update scrolledToPhoto on page scroll
                        photoViewModel.scrolledToPhoto.value = photoViewModel.unsplashPhotos.value?.get(viewPagerPosition)
                    }
                }
            })
        }

        return viewBinding.root
    }
}
