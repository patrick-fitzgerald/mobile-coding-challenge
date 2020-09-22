package com.example.ui.photo

import android.os.Bundle
import android.transition.TransitionInflater
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

    private var viewPagerPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate")
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated")



        if (viewBinding.photoViewPager.adapter != null)
            viewBinding.photoViewPager.adapter = null

        val viewPagerAdapter = PhotoViewPagerAdapter(photoViewModel.unsplashPhotos.value ?: emptyList())
        viewBinding.photoViewPager.adapter = viewPagerAdapter

        val selectedPosition = photoViewModel.selectedPhotoPosition()
        if (selectedPosition != null) {
            Timber.d("selectedPosition: $selectedPosition")

            viewBinding.photoViewPager.setCurrentItem(selectedPosition, false)

        }
        viewBinding.photoViewPager.transitionName = photoViewModel.selectedPhoto.value?.id


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("onCreateView")

        viewBinding = FragmentPhotoBinding.inflate(inflater, container, false)
        viewBinding.photoViewModel = photoViewModel
        viewBinding.lifecycleOwner = this


        // reset scrolledToPhoto position
        photoViewModel.scrolledToPhoto.value = null

//            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//                override fun onPageSelected(position: Int) {
//                    super.onPageSelected(position)
//                    viewPagerPosition = position
//                }
//
//                override fun onPageScrollStateChanged(state: Int) {
//                    super.onPageScrollStateChanged(state)
//                    if(state == SCROLL_STATE_IDLE){
//                        // Update selectedPhoto on page scroll
//                        Timber.d("onPageScrollStateChanged SCROLL_STATE_IDLE $viewPagerPosition")
//                        photoViewModel.scrolledToPhoto.value = photoViewModel.unsplashPhotos.value?.get(viewPagerPosition)
//                    }
//
//                }
//            })
//        }


        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.d("onDestroyView")
        viewBinding.photoViewPager.transitionName = null
    }

    override fun onStop() {
        super.onStop()
        Timber.d("onDestroyView")
    }

    override fun onDetach() {
        super.onDetach()
        Timber.d("onDetach")
    }

}
