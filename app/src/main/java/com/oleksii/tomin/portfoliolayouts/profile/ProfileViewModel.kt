package com.oleksii.tomin.portfoliolayouts.profile

import Contact
import androidx.lifecycle.viewModelScope
import com.oleksii.tomin.portfoliolayouts.contentful.ContentfulService
import com.oleksii.tomin.portfoliolayouts.mvi.MviViewModel
import com.oleksii.tomin.portfoliolayouts.mvi.MviViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    contentfulService: ContentfulService,
) : MviViewModel<ProfileViewModelState, ProfileViewModelEvents>(
    ProfileViewModelState(
        profilePhotoUrl = null,
        showProfilePhotoShimmerEffect = true,
        contact = null,
        showContactsShimmerEffect = true,
        highlightPhoneNumber = false,
        secretHighlighting = true
    )
) {
    init {
        viewModelScope.launch {
            runCatching {
                contentfulService.fetchProfilePhotoUrl()
            }.onSuccess { url ->

                // The delay is used to demonstrate shimmer effect
                delay(2_000)

                updateState { copy(profilePhotoUrl = "https:$url") }
            }.onFailure {
                sendEvent(ProfileViewModelEvents.Error(it))
            }
        }

        viewModelScope.launch {
            runCatching {
                contentfulService.getContentfulResume()
            }.onSuccess { resume ->

                // The delay is used to demonstrate shimmer effect
                delay(1_000)

                updateState {
                    copy(
                        contact = resume.contact.copy(
                            formattedPhoneContact = resume.contact.formatPhone()
                        ),
                    )
                }
            }.onFailure {
                sendEvent(ProfileViewModelEvents.Error(it))
            }
        }

        viewModelScope.launch {
            repeat(Int.MAX_VALUE) {
                if (state.value.secretHighlighting) {
                    updateState { copy(highlightPhoneNumber = true) }
                }
                delay(500)
                if (state.value.secretHighlighting) {
                    updateState { copy(highlightPhoneNumber = false) }
                }
                delay(5_000)
            }
        }
    }

    fun stopProfilePhotoShimmerEffect() = updateState {
        copy(showProfilePhotoShimmerEffect = false)
    }

    fun stopContactsShimmerEffect() = updateState {
        copy(showContactsShimmerEffect = false)
    }

    fun requestToCallMe() {
        sendEvent(ProfileViewModelEvents.ShowRequestToCallMeDialog)
    }

    fun reportError(th: Throwable) {
        sendEvent(ProfileViewModelEvents.Error(th))
    }

    fun highlightPhoneNumber() = updateState {
        copy(
            highlightPhoneNumber = true,
            secretHighlighting = false
        )
    }

    fun stopHighlightingPhoneNumber() = updateState {
        copy(
            highlightPhoneNumber = false,
            secretHighlighting = true
        )
    }

    private fun Contact.formatPhone(): String {
        if (!phone.startsWith("+") || phone.length != 12) {
            phone
        }

        val countryCode = phone.substring(0, 2)
        val areaCode = phone.substring(2, 5)
        val firstPart = phone.substring(5, 8)
        val secondPart = phone.substring(8, 12)

        return "$countryCode ($areaCode) $firstPart $secondPart"
    }
}

data class ProfileViewModelState(
    val profilePhotoUrl: String?,
    val showProfilePhotoShimmerEffect: Boolean,
    val contact: Contact?,
    val showContactsShimmerEffect: Boolean,
    val highlightPhoneNumber: Boolean,
    val secretHighlighting: Boolean
) : MviViewState

sealed class ProfileViewModelEvents {
    data object ShowRequestToCallMeDialog : ProfileViewModelEvents()
    data class Error(val t: Throwable) : ProfileViewModelEvents()
}