package com.example.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.R
import com.example.databinding.FragmentHomeBinding
import com.example.ui.base.BaseFragment
import com.example.ui.photo.PhotoViewModel
import com.example.util.Constants.Companion.UNSPLASH_PHOTOS_FIRST_PAGE
import com.example.util.autoCleared
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class HomeFragment : BaseFragment() {

    private var viewBinding by autoCleared<FragmentHomeBinding>()
    private val photoViewModel: PhotoViewModel by sharedViewModel()

    private lateinit var viewLayoutManager: RecyclerView.LayoutManager
    private lateinit var viewListAdapter: PhotoListAdapter

    // https://medium.com/@j.c.moreirapinto/recyclerview-shared-transitions-in-android-navigation-architecture-component-16eb902b39ba
    private val photoClickListener = PhotoClickListener { unsplashPhoto, imageView ->
        photoViewModel.selectedPhoto.value = unsplashPhoto

        val extras = FragmentNavigatorExtras(
            imageView to unsplashPhoto.id
        )

        navigateToPhotoFragment(extras)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Make Unsplash Photos initial request
        photoViewModel.getUnsplashPhotosRequest(UNSPLASH_PHOTOS_FIRST_PAGE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewBinding = FragmentHomeBinding.inflate(inflater, container, false)
        viewBinding.photoViewModel = photoViewModel
        viewBinding.lifecycleOwner = this

        // Setup Photo list RecycleView
        viewLayoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        viewListAdapter = PhotoListAdapter(photoClickListener)
        viewBinding.unsplashPhotoList.apply {
            layoutManager = viewLayoutManager
            adapter = viewListAdapter
            // pagination support
            addOnScrollListener(PaginationScrollListener(photoViewModel))
        }

        scrollToSelectedPhoto()

        return viewBinding.root
    }

    // Scroll to currently selected photo
    private fun scrollToSelectedPhoto() {
        val scrolledToPhoto = photoViewModel.scrolledToPhoto.value
        if (scrolledToPhoto != null) {
            val scrolledToPhotoPosition = photoViewModel.scrolledToPhotoPosition()
            if (scrolledToPhotoPosition != null) {
                viewBinding.unsplashPhotoList.smoothScrollToPosition(scrolledToPhotoPosition)
            }
        }
    }

    override fun onStart() {
        super.onStart()

        // observe Unsplash photos updates
        photoViewModel.photoListData.observe(
            viewLifecycleOwner,
            Observer { photoListData ->
                photoListData?.let {
                    if (it.unsplashPhotos != null) {
                        viewListAdapter.submitPhotoList(it.unsplashPhotos)
                    }
                }
            }
        )
    }

    private fun navigateToPhotoFragment(extras: Navigator.Extras) {

        Timber.d("navigateToPhotoFragment")
        findNavController().navigate(
            R.id.action_homeFragment_to_photoFragment,
            null, // Bundle of args
            null, // NavOptions
            extras
        )
    }
}
