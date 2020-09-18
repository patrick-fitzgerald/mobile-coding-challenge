package com.example.ui.photo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.databinding.FragmentPhotoBinding
import com.example.ui.base.BaseFragment
import com.example.util.autoCleared
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

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
        photoViewModel.unsplashPhotos.value?.let {
            viewBinding.photoViewPager.adapter = viewPagerAdapter
        }

        return viewBinding.root
    }

    override fun onStart() {
        super.onStart()

        // unsplash photos update
        photoViewModel.unsplashPhotos.observe(
            viewLifecycleOwner,
            Observer { unsplashPhotos ->
                unsplashPhotos?.let {
                    viewPagerAdapter.submitPhotoList(it)
                }
            }
        )
    }
}
