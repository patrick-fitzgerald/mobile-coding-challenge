package com.example.ui.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.reactivex.disposables.CompositeDisposable
import org.koin.core.KoinComponent

abstract class BaseFragment : Fragment(), KoinComponent {

    lateinit var viewModelFactory: ViewModelProvider.Factory

    var compositeDisposable = CompositeDisposable()

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }
}
