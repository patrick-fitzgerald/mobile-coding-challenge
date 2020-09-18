package com.example.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.R
import com.example.databinding.FragmentSplashBinding
import com.example.ui.base.BaseFragment
import com.example.util.autoCleared
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class SplashFragment : BaseFragment() {

    private var viewBinding by autoCleared<FragmentSplashBinding>()
    private val splashViewModel: SplashViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentSplashBinding.inflate(inflater, container, false)
        viewBinding.viewModel = splashViewModel
        viewBinding.lifecycleOwner = this

        navigateUser()
        return viewBinding.root
    }

    private fun navigateUser() {
        // Add auth logic here
        navigateToHomeFragment()
    }

    private fun navigateToHomeFragment() {
        Timber.d("navigateToHomeFragment")
        findNavController().navigate(
            R.id.action_splashFragment_to_homeFragment,
            null,
            NavOptions.Builder().setPopUpTo(R.id.splashFragment, true).build()
        )
    }
}
