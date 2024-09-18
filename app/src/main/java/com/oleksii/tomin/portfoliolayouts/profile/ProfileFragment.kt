package com.oleksii.tomin.portfoliolayouts.profile

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.toSpannable
import androidx.fragment.app.viewModels
import coil.load
import com.oleksii.tomin.portfoliolayouts.R
import com.oleksii.tomin.portfoliolayouts.databinding.FragmentProfileBinding
import com.oleksii.tomin.portfoliolayouts.mvi.MviFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : MviFragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModels()

    @SuppressLint("SetTextI18n")
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
                        name.text = toSpannable(getString(R.string.name), contact.name)
                        title.text = toSpannable(getString(R.string.title), contact.title)

                        if (contact.location.isNotEmpty()) {
                            location.text =
                                toSpannable(getString(R.string.location), contact.location)
                            location.visibility = View.VISIBLE
                        } else {
                            location.visibility = View.GONE
                        }

                        email.text = toSpannable(getString(R.string.email), contact.email)
                        linkedin.text = toSpannable(getString(R.string.linkedin), contact.linkedin)
                        phone.text = toSpannable(getString(R.string.phone), contact.phone)
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

    private fun toSpannable(bold: String, normal: String): Spannable {
        return "$bold $normal".toSpannable().apply {
            setSpan(
                StyleSpan(Typeface.BOLD),
                0,
                bold.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
}