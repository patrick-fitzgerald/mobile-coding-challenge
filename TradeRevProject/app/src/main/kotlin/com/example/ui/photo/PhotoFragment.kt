package com.example.ui.photo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.databinding.FragmentPhotoBinding
import com.example.ui.base.BaseFragment
import com.example.util.autoCleared
import org.koin.androidx.viewmodel.ext.android.viewModel

class PhotoFragment : BaseFragment() {

    private var viewBinding by autoCleared<FragmentPhotoBinding>()
    private val photoViewModel: PhotoViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentPhotoBinding.inflate(inflater, container, false)
        viewBinding.viewModel = photoViewModel
        viewBinding.lifecycleOwner = this

        return viewBinding.root
    }

}
