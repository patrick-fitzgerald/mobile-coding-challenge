package com.example.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.databinding.FragmentHomeBinding
import com.example.ui.base.BaseFragment
import com.example.util.autoCleared
import io.reactivex.rxkotlin.addTo
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class HomeFragment : BaseFragment() {

    private var viewBinding by autoCleared<FragmentHomeBinding>()
    private val homeViewModel: HomeViewModel by viewModel()

    private lateinit var viewLayoutManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: UnsplashPhotoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentHomeBinding.inflate(inflater, container, false)
        viewBinding.viewModel = homeViewModel
        viewBinding.lifecycleOwner = this

        viewLayoutManager = LinearLayoutManager(context)
        viewAdapter = UnsplashPhotoAdapter()

        viewBinding.unsplashPhotoList.apply {
            setHasFixedSize(true)
            layoutManager = viewLayoutManager
            adapter = viewAdapter
        }

        homeViewModel.getUnsplashPhotosRequest()

        return viewBinding.root
    }

    override fun onStart() {
        super.onStart()
        subscribeToContextEvents()

        // unsplash photos update
        homeViewModel.unsplashPhotos.observe(
            viewLifecycleOwner,
            Observer { unsplashPhotos ->
                unsplashPhotos?.let {
                    viewAdapter.addHeaderAndSubmitList(it)
                }
            }
        )


    }

    private fun subscribeToContextEvents(){
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
}
