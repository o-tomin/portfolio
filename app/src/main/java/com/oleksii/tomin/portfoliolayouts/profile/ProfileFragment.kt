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
            collectStateProperty(ProfileViewModelState::profilePhotoUrl) { url ->
                url?.let {
                    binding.profilePhotoLayout.profilePhoto.load(url) {
                        crossfade(true)
                        listener(
                            onSuccess = { _, _ ->
                                viewModel.stopProfilePhotoShimmerEffect()
                            },
                            onError = { _, _ ->
                                viewModel.stopProfilePhotoShimmerEffect()
                            }
                        )
                    }
                }
            }
            collectStateProperty(ProfileViewModelState::showProfilePhotoShimmerEffect) { showShimmer ->
                binding.profilePhotoLayout.shimmerProfilePhoto.apply {
                    if (showShimmer) {
                        binding.profilePhotoLayout.cvProfilePhoto.visibility = View.GONE
                        binding.profilePhotoLayout.shimmerProfilePhoto.visibility = View.VISIBLE
                        startShimmer()
                    } else {
                        binding.profilePhotoLayout.cvProfilePhoto.visibility = View.VISIBLE
                        binding.profilePhotoLayout.shimmerProfilePhoto.visibility = View.GONE
                        stopShimmer()
                    }
                }
            }
            collectStateProperty(ProfileViewModelState::contact) { contact ->
                contact?.let {
                    with(binding.profileContactsLayout) {
                        name.text = contact.name
                        title.text = contact.title

                        if (contact.location.isNotEmpty()) {
                            location.text = contact.location
                            location.visibility = View.VISIBLE
                        } else {
                            location.visibility = View.GONE
                        }

                        email.text = contact.email
                        linkedin.text = contact.linkedin
                        phone.text = contact.phone
                    }
                }?.also {
                    viewModel.stopContactsShimmerEffect()
                }
            }
            collectStateProperty(ProfileViewModelState::showContactsShimmerEffect) { showShimmer ->
                if (showShimmer) {
                    binding.profileContactsLayout.root.visibility = View.GONE
                    binding.profileContactsShifferLayout.root.visibility = View.VISIBLE
                    binding.profileContactsShifferLayout.root.startShimmer()
                } else {
                    binding.profileContactsLayout.root.visibility = View.VISIBLE
                    binding.profileContactsShifferLayout.root.visibility = View.GONE
                    binding.profileContactsShifferLayout.root.stopShimmer()
                }
            }
        }

        return binding.root
    }
}