package com.oleksii.tomin.portfoliolayouts.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import coil.load
import com.oleksii.tomin.portfoliolayouts.databinding.FragmentProfileBinding
import com.oleksii.tomin.portfoliolayouts.mvi.MviFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : MviFragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        with(viewModel) {
            collectStateProperty(ProfileViewModelState::profilePhotoUrl) {
                binding.profilePhoto.load(it)
            }
        }

        return binding.root
    }
}