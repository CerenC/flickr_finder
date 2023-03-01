package com.cerenb.flickrfinder.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.cerenb.flickrfinder.R
import com.cerenb.flickrfinder.databinding.FragmentFullPhotoBinding
import com.cerenb.flickrfinder.ui.extensions.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FullPhotoFragment : Fragment(R.layout.fragment_full_photo) {
    private var _binding: FragmentFullPhotoBinding? = null
    private val binding: FragmentFullPhotoBinding
        get() = requireNotNull(_binding)

    private val args: FullPhotoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFullPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi(args.fullPhotoImageUrl)
    }

    private fun setupUi(imageUrl: String) {
        binding.fullPhotoImage.loadImage(
            url = imageUrl,
            context = binding.root.context
        )
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}