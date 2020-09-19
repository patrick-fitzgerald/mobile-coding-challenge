package com.example.ui.base

import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import org.koin.core.KoinComponent

abstract class BaseFragment : Fragment(), KoinComponent {

    var compositeDisposable = CompositeDisposable()

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }
}
