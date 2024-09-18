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

                updateState { copy(contact = resume.contact) }
            }.onFailure {
                sendEvent(ProfileViewModelEvents.Error(it))
            }
        }
    }

    fun stopProfilePhotoShimmerEffect() = updateState {
        copy(showProfilePhotoShimmerEffect = false)
    }

    fun stopContactsShimmerEffect() = updateState {
        copy(showContactsShimmerEffect = false)
    }
}

data class ProfileViewModelState(
    val profilePhotoUrl: String?,
    val showProfilePhotoShimmerEffect: Boolean,
    val contact: Contact?,
    val showContactsShimmerEffect: Boolean,
) : MviViewState


sealed class ProfileViewModelEvents {
    data class Error(val t: Throwable) : ProfileViewModelEvents()
}