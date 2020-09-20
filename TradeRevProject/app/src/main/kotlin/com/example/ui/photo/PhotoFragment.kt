package com.example.ui.photo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.databinding.FragmentPhotoBinding
import com.example.ui.base.BaseFragment
import com.example.util.autoCleared
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class PhotoFragment : BaseFragment() {

    private var viewBinding by autoCleared<FragmentPhotoBinding>()
    private val photoViewModel: PhotoViewModel by sharedViewModel()
    private lateinit var viewPagerAdapter: PhotoViewPagerAdapter

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
        viewBinding.photoViewPager.post {
            val selectedPosition = photoViewModel.selectedPosition()
            if (selectedPosition != null) {
                viewBinding.photoViewPager.setCurrentItem(selectedPosition, false)
            }
        }

        return viewBinding.root
    }

}
