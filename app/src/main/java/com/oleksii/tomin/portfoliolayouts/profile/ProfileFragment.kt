package com.oleksii.tomin.portfoliolayouts.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.text.toSpannable
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import com.oleksii.tomin.portfoliolayouts.R
import com.oleksii.tomin.portfoliolayouts.databinding.FragmentProfileBinding
import com.oleksii.tomin.portfoliolayouts.ext.copyTextToClipboard
import com.oleksii.tomin.portfoliolayouts.ext.eLog
import com.oleksii.tomin.portfoliolayouts.ext.scopedClickAndDebounce
import com.oleksii.tomin.portfoliolayouts.ext.toast
import com.oleksii.tomin.portfoliolayouts.mvi.MviFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ProfileFragment : MviFragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModels()
    private val colorSpan by lazy {
        ForegroundColorSpan(
            ContextCompat.getColor(requireContext(), R.color.colorPrimary)
        )
    }

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
                        phone.text =
                            toSpannable(getString(R.string.phone), contact.formattedPhoneContact)
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

            collectStateProperty(ProfileViewModelState::highlightPhoneNumber) { isHighlight ->
                currentState.contact?.let { contact ->
                    if (isHighlight) {
                        val phoneTextSpannable = toSpannable(
                            getString(R.string.phone),
                            contact.formattedPhoneContact
                        )

                        binding.profileContactsLayout.phone.text = phoneTextSpannable.apply {
                            setSpan(
                                colorSpan,
                                phoneTextSpannable.length - contact.formattedPhoneContact.length,
                                phoneTextSpannable.length,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                        }
                    } else {
                        binding.profileContactsLayout.phone.text =
                            binding.profileContactsLayout.phone.text.toSpannable().apply {
                                removeSpan(colorSpan)
                            }
                    }
                }
            }

            collectStateProperty(ProfileViewModelState::highlightLinkedInUrl) { isHighlight ->
                currentState.contact?.let { contact ->
                    if (isHighlight) {
                        val linkedInTextSpannable = toSpannable(
                            getString(R.string.linkedin),
                            contact.linkedin
                        )

                        binding.profileContactsLayout.linkedin.text = linkedInTextSpannable.apply {
                            setSpan(
                                colorSpan,
                                linkedInTextSpannable.length - contact.linkedin.length,
                                linkedInTextSpannable.length,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                        }
                    } else {
                        binding.profileContactsLayout.linkedin.text =
                            binding.profileContactsLayout.linkedin.text.toSpannable().apply {
                                removeSpan(colorSpan)
                            }
                    }
                }
            }

            collectStateProperty(ProfileViewModelState::highlightEmail) { isHighlight ->
                currentState.contact?.let { contact ->
                    if (isHighlight) {
                        val emailTextSpannable = toSpannable(
                            getString(R.string.email),
                            contact.email
                        )

                        binding.profileContactsLayout.email.text = emailTextSpannable.apply {
                            setSpan(
                                colorSpan,
                                emailTextSpannable.length - contact.email.length,
                                emailTextSpannable.length,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                        }
                    } else {
                        binding.profileContactsLayout.email.text =
                            binding.profileContactsLayout.email.text.toSpannable().apply {
                                removeSpan(colorSpan)
                            }
                    }
                }
            }

            collectEvents { event ->
                when (event) {
                    ProfileViewModelEvents.ShowRequestToCallMeDialog -> {
                        currentState.contact?.phone?.let { phone ->
                            showRequestToRedirectDialog(
                                message = R.string.call_alex_message,
                                positiveButton = R.string.call,
                                negativeButton = R.string.later,
                            ) {
                                startActivity(
                                    Intent(Intent.ACTION_DIAL).apply {
                                        data = Uri.parse("tel:$phone")
                                    }
                                )
                            }
                        }
                    }

                    ProfileViewModelEvents.ShowMyLinkedIn -> {
                        currentState.contact?.linkedinViewProfileUrl?.let { linkedInUrl ->
                            showRequestToRedirectDialog(
                                message = R.string.open_alex_linked_in_message,
                                positiveButton = R.string.redirect,
                                negativeButton = R.string.later,
                            ) {
                                startActivity(
                                    Intent(Intent.ACTION_VIEW, Uri.parse(linkedInUrl))
                                )
                            }
                        }
                    }

                    is ProfileViewModelEvents.Error -> eLog(event.t)
                }
            }

            with(binding.profileContactsLayout) {
                phone.scopedClickAndDebounce()
                    .onEach {
                        viewModel.highlightPhoneNumber()
                        viewModel.requestToCallMe()
                    }
                    .catch { viewModel.reportError(it) }
                    .launchIn(lifecycleScope)

                linkedin.scopedClickAndDebounce()
                    .onEach {
                        viewModel.highlightLinkedInUrl()
                        viewModel.showMyLinkedIn()
                    }
                    .catch { viewModel.reportError(it) }
                    .launchIn(lifecycleScope)

                copyEmail.scopedClickAndDebounce()
                    .onEach {
                        currentState.contact?.email?.let { email ->
                            copyTextToClipboard(email)
                            toast(getString(R.string.copied))
                        }
                    }
                    .catch { viewModel.reportError(it) }
                    .launchIn(lifecycleScope)

                copyLinkedin.scopedClickAndDebounce()
                    .onEach {
                        currentState.contact?.linkedinViewProfileUrl?.let { linkedIn ->
                            copyTextToClipboard(linkedIn)
                            toast(getString(R.string.copied))
                        }
                    }
                    .catch { viewModel.reportError(it) }
                    .launchIn(lifecycleScope)

                copyPhone.scopedClickAndDebounce()
                    .onEach {
                        currentState.contact?.phone?.let { phone ->
                            copyTextToClipboard(phone)
                            toast(getString(R.string.copied))
                        }
                    }
                    .catch { viewModel.reportError(it) }
                    .launchIn(lifecycleScope)
            }
        }

        return binding.root
    }

    private fun toSpannable(
        bold: String,
        normal: String,
    ): Spannable {
        return "$bold $normal".toSpannable().apply {
            setSpan(
                StyleSpan(Typeface.BOLD),
                0,
                bold.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    private fun showRequestToRedirectDialog(
        @StringRes message: Int,
        @StringRes positiveButton: Int,
        @StringRes negativeButton: Int,
        onRedirect: () -> Unit,
    ) =
        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton(positiveButton) { _, _ -> onRedirect() }
            .setNegativeButton(negativeButton) { _, _ -> }
            .create()
            .apply {
                setOnDismissListener {
                    viewModel.stopHighlightContacts()
                }
            }
            .show()
}