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
    private lateinit var viewAdapter: GitHubRepoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentHomeBinding.inflate(inflater, container, false)
        viewBinding.viewModel = homeViewModel
        viewBinding.lifecycleOwner = this

        viewLayoutManager = LinearLayoutManager(context)
        viewAdapter = GitHubRepoAdapter()

        viewBinding.githubRepoList.apply {
            setHasFixedSize(true)
            layoutManager = viewLayoutManager
            adapter = viewAdapter
        }

        homeViewModel.getGitHubReposRequest()

        return viewBinding.root
    }

    override fun onStart() {
        super.onStart()

        // github repos update
        homeViewModel.gitHubRepos.observe(
            viewLifecycleOwner,
            Observer { gitHubRepos ->
                gitHubRepos?.let {
                    viewAdapter.addHeaderAndSubmitList(it)
                }
            }
        )

        // click events
        homeViewModel.contextEventBus.subscribe { contextEvent ->
            context?.let {
                when (contextEvent) {
                    HomeViewModel.ContextEvent.GET_REPOS_BUTTON_CLICKED -> Timber.d("HomeFragment - getRepoButtonClicked")
                    else -> Unit
                }
            }
        }.addTo(compositeDisposable)
    }
}
