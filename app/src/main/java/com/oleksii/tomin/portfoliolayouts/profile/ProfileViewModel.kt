package com.oleksii.tomin.portfoliolayouts.profile

import androidx.lifecycle.viewModelScope
import com.oleksii.tomin.portfoliolayouts.contentful.ContentfulService
import com.oleksii.tomin.portfoliolayouts.mvi.MviViewModel
import com.oleksii.tomin.portfoliolayouts.mvi.MviViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    contentfulService: ContentfulService,
) : MviViewModel<ProfileViewModelState, ProfileViewModelEvents>(
    ProfileViewModelState(
        profilePhotoUrl = ""
    )
) {
    init {
        viewModelScope.launch {
            runCatching {
                contentfulService.fetchProfilePhotoUrl()
            }.onSuccess { url ->
                updateState { copy(profilePhotoUrl = "https:$url") }
            }.onFailure {
                sendEvent(ProfileViewModelEvents.Error(it))
            }
        }
    }
}

data class ProfileViewModelState(
    val profilePhotoUrl: String,
) : MviViewState

sealed class ProfileViewModelEvents {
    data class Error(val t: Throwable) : ProfileViewModelEvents()
}