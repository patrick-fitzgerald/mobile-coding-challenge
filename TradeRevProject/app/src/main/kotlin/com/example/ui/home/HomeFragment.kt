package com.example.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.R
import com.example.data.response.UnsplashPhoto
import com.example.databinding.FragmentHomeBinding
import com.example.ui.base.BaseFragment
import com.example.ui.photo.PhotoViewModel
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

    private var pageNumber: Int = 1

    private val photoClickListener = PhotoClickListener { unsplashPhoto: UnsplashPhoto ->
        photoViewModel.selectedPhoto.value = unsplashPhoto
        navigateToPhotoFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentHomeBinding.inflate(inflater, container, false)
        viewBinding.photoViewModel = photoViewModel
        viewBinding.lifecycleOwner = this

        viewLayoutManager = LinearLayoutManager(context)
        viewListAdapter = PhotoListAdapter(photoClickListener)

        viewBinding.unsplashPhotoList.apply {
            setHasFixedSize(true)
            layoutManager = viewLayoutManager
            adapter = viewListAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager?

                    layoutManager?.let {
                        val totalItemCount = it.itemCount
                        val visibleItemCount = it.childCount

                        val lastVisibleItemPosition = it.findLastVisibleItemPosition()

                        if (photoViewModel.isLoading.value != true && lastVisibleItemPosition + 1 >= totalItemCount) {
                            Timber.d("makeRequest - pageNumber: $pageNumber")
                            pageNumber += 1
                            photoViewModel.getUnsplashPhotosRequest(pageNumber)
                        }
                    }
                }
            })
        }


        // Make initial request
        photoViewModel.getUnsplashPhotosRequest(pageNumber)

        return viewBinding.root
    }

    var items: List<DataItem> = emptyList()

    override fun onStart() {
        super.onStart()
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

    private fun navigateToPhotoFragment() {
        Timber.d("navigateToPhotoFragment")
        findNavController().navigate(R.id.action_homeFragment_to_photoFragment)
    }
}
