package com.oleksii.tomin.portfoliolayouts.profile

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
    )
) {
    init {
        viewModelScope.launch {
            runCatching {
                contentfulService.fetchProfilePhotoUrl()
            }.onSuccess { url ->

                // The delay is used to demonstrate shimmer effect
                delay(5_000)

                updateState { copy(profilePhotoUrl = "https:$url") }
            }.onFailure {
                sendEvent(ProfileViewModelEvents.Error(it))
            }
        }
    }

    fun stopProfilePhotoShimmerEffect() = updateState {
        copy(showProfilePhotoShimmerEffect = false)
    }

}

data class ProfileViewModelState(
    val profilePhotoUrl: String?,
    val showProfilePhotoShimmerEffect: Boolean,
) : MviViewState


sealed class ProfileViewModelEvents {
    data class Error(val t: Throwable) : ProfileViewModelEvents()
}