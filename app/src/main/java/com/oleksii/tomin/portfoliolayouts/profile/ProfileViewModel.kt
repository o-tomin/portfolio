package com.oleksii.tomin.portfoliolayouts.profile

import Contact
import androidx.lifecycle.viewModelScope
import com.oleksii.tomin.portfoliolayouts.contentful.ContentfulService
import com.oleksii.tomin.portfoliolayouts.mvi.MviViewModel
import com.oleksii.tomin.portfoliolayouts.mvi.MviViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
        appRepo = null,
        showContactsShimmerEffect = true,
        highlightPhoneNumber = false,
        highlightLinkedInUrl = false,
        highlightEmail = false,
        highlightAppRepoUrl = false,
        secretHighlighting = true,
    )
) {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                contentfulService.fetchProfilePhotoUrl()
            }.onSuccess { url ->
                updateState { copy(profilePhotoUrl = "https:$url") }
            }.onFailure {
                sendEvent(ProfileViewModelEvents.Error(it))
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                contentfulService.getContentfulResume()
            }.onSuccess { resume ->
                updateState {
                    copy(
                        contact = resume.contact.copy(
                            formattedPhoneContact = resume.contact.formatPhone()
                        ),
                        appRepo = resume.github
                    )
                }
            }.onFailure {
                sendEvent(ProfileViewModelEvents.Error(it))
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            repeat(Int.MAX_VALUE) {
                delay(5_000)

                if (state.value.secretHighlighting) {
                    updateState {
                        copy(
                            highlightPhoneNumber = false,
                            highlightLinkedInUrl = false,
                            highlightEmail = true,
                        )
                    }
                }
                delay(500)
                if (state.value.secretHighlighting) {
                    updateState {
                        copy(
                            highlightPhoneNumber = false,
                            highlightLinkedInUrl = true,
                            highlightEmail = false,
                        )
                    }
                }
                delay(500)
                if (state.value.secretHighlighting) {
                    updateState {
                        copy(
                            highlightPhoneNumber = true,
                            highlightLinkedInUrl = false,
                            highlightEmail = false,
                        )
                    }
                }
                delay(500)
                if (state.value.secretHighlighting) {
                    updateState {
                        copy(
                            highlightPhoneNumber = false,
                            highlightLinkedInUrl = false,
                            highlightEmail = false,
                        )
                    }
                }
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
            highlightLinkedInUrl = false,
            highlightEmail = false,
            highlightAppRepoUrl = false,
            secretHighlighting = false
        )
    }

    fun highlightLinkedInUrl() = updateState {
        copy(
            highlightLinkedInUrl = true,
            highlightEmail = false,
            highlightPhoneNumber = false,
            highlightAppRepoUrl = false,
            secretHighlighting = false
        )
    }

    fun highlightEmail() = updateState {
        copy(
            highlightEmail = true,
            highlightPhoneNumber = false,
            highlightLinkedInUrl = false,
            highlightAppRepoUrl = false,
            secretHighlighting = false
        )
    }

    fun stopHighlightContacts() = updateState {
        copy(
            highlightEmail = false,
            highlightPhoneNumber = false,
            highlightLinkedInUrl = false,
            highlightAppRepoUrl = false,
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

    fun showMyLinkedIn() {
        sendEvent(ProfileViewModelEvents.ShowMyLinkedIn)
    }

    fun requestToEmailMe() {
        sendEvent(ProfileViewModelEvents.EmailMe)
    }

    fun highlightPortfolio() = updateState {
        copy(
            highlightEmail = true,
            highlightPhoneNumber = false,
            highlightLinkedInUrl = false,
            highlightAppRepoUrl = true,
            secretHighlighting = false
        )
    }

    fun showAppCode() {
        sendEvent(ProfileViewModelEvents.ShowAppCode)
    }
}

data class ProfileViewModelState(
    val profilePhotoUrl: String?,
    val showProfilePhotoShimmerEffect: Boolean,
    val contact: Contact?,
    val appRepo: String?,
    val showContactsShimmerEffect: Boolean,
    val highlightPhoneNumber: Boolean,
    val highlightLinkedInUrl: Boolean,
    val highlightEmail: Boolean,
    val highlightAppRepoUrl: Boolean,
    val secretHighlighting: Boolean
) : MviViewState

sealed class ProfileViewModelEvents {
    data object ShowRequestToCallMeDialog : ProfileViewModelEvents()
    data object ShowMyLinkedIn : ProfileViewModelEvents()
    data object EmailMe : ProfileViewModelEvents()
    data object ShowAppCode : ProfileViewModelEvents()
    data class Error(val t: Throwable) : ProfileViewModelEvents()
}