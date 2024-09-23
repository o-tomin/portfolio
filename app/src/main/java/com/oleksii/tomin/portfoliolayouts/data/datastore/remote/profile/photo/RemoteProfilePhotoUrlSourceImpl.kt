package com.oleksii.tomin.portfoliolayouts.data.datastore.remote.profile.photo

import com.oleksii.tomin.portfoliolayouts.data.model.profile.photo.ProfilePhotoModel

class RemoteProfilePhotoUrlSourceImpl(
    private val remoteProfilePhotoUrlApi: RemoteProfilePhotoUrlApi
) : RemoteProfilePhotoUrlSource {

    override suspend fun getProfilePhoto(): ProfilePhotoModel {
        return remoteProfilePhotoUrlApi.fetchProfilePhotoUrl(PROFILE_PHOTO_MEDIA_ID)
    }

    companion object {
        const val PROFILE_PHOTO_MEDIA_ID = "6pNXsbMWXKfKrRu1HLJGNn"
    }
}