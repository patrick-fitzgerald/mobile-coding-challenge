package com.example.ui.photo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_IDLE
import com.example.databinding.FragmentPhotoBinding
import com.example.ui.base.BaseFragment
import com.example.util.autoCleared
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class PhotoFragment : BaseFragment() {

    private var viewBinding by autoCleared<FragmentPhotoBinding>()
    private val photoViewModel: PhotoViewModel by sharedViewModel()
    private lateinit var viewPagerAdapter: PhotoViewPagerAdapter
    private var viewPagerPosition = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewBinding = FragmentPhotoBinding.inflate(inflater, container, false)
        viewBinding.photoViewModel = photoViewModel
        viewBinding.lifecycleOwner = this

        viewPagerAdapter = PhotoViewPagerAdapter()
        viewBinding.photoViewPager.adapter = viewPagerAdapter
        viewPagerAdapter.submitPhotoList(photoViewModel.unsplashPhotos.value)

        viewBinding.photoViewPager.apply {
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    viewPagerPosition = position
                }

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    if(state == SCROLL_STATE_IDLE){
                        // Update selectedPhoto on page scroll
                        Timber.d("onPageScrollStateChanged SCROLL_STATE_IDLE $viewPagerPosition")
                        photoViewModel.selectedPhoto.value = photoViewModel.unsplashPhotos.value?.get(viewPagerPosition)
                    }

                }
            })
        }

        viewBinding.photoViewPager.post {
            val selectedPosition = photoViewModel.selectedPosition()
            Timber.d("selectedPosition: $selectedPosition")
            if (selectedPosition != null) {
                viewBinding.photoViewPager.setCurrentItem(selectedPosition, false)
            }else{
                Timber.d("selectedPosition is null")
            }
        }



        return viewBinding.root
    }

}
