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
import io.reactivex.rxkotlin.addTo
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class HomeFragment : BaseFragment() {

    private var viewBinding by autoCleared<FragmentHomeBinding>()
    private val homeViewModel: HomeViewModel by viewModel()
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
        Timber.d("onCreate")
        // Make initial request
        photoViewModel.getUnsplashPhotosRequest(UNSPLASH_PHOTOS_FIRST_PAGE)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Timber.d("onCreateView")

        viewBinding = FragmentHomeBinding.inflate(inflater, container, false)
        viewBinding.photoViewModel = photoViewModel
        viewBinding.lifecycleOwner = this

        viewLayoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        viewListAdapter = PhotoListAdapter(photoClickListener)

        viewBinding.unsplashPhotoList.apply {

            setHasFixedSize(true)
            layoutManager = viewLayoutManager
            adapter = viewListAdapter
            addOnScrollListener(PaginationScrollListener(photoViewModel))
        }

        scrollToSelectedPhoto()


        return viewBinding.root
    }

    private fun scrollToSelectedPhoto() {

        val scrolledToPhotoPosition = photoViewModel.scrolledToPhotoPosition()
        val scrolledToPhoto = photoViewModel.scrolledToPhoto.value
        if (scrolledToPhoto != null && scrolledToPhotoPosition != null) {
            viewBinding.unsplashPhotoList.scrollToPosition(scrolledToPhotoPosition)
        }

    }


    override fun onStart() {
        super.onStart()

        Timber.d("onStart")

        subscribeToContextEvents()

        // unsplash photos update
        photoViewModel.photoListData.observe(
            viewLifecycleOwner,
            Observer { photoListData ->
                photoListData?.let {
                    if (it.unsplashPhotos != null) {
                        Timber.d("submitPhotoList isLoading: false")
                        viewListAdapter.submitPhotoList(it.unsplashPhotos)
                    }
//                    if (it.isLoading != null) {
//                        Timber.d("submitPhotoList isLoading: true")
//                        viewListAdapter.submitPhotoList(it.unsplashPhotos, true)
//                    }
                }
            }
        )
    }

    private fun subscribeToContextEvents() {
        // context events
        homeViewModel.contextEventBus.subscribe { contextEvent ->
            context?.let {
                when (contextEvent) {
                    HomeViewModel.ContextEvent.SCROLL_NEXT_PAGE_EVENT -> Timber.d("HomeFragment - scroll next page event")
                    else -> Unit
                }
            }
        }.addTo(compositeDisposable)
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
