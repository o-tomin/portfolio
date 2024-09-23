package com.oleksii.tomin.portfoliolayouts.data.datastore.remote.profile.photo

import com.oleksii.tomin.portfoliolayouts.data.model.profile.photo.ProfilePhotoModel

interface RemoteProfilePhotoUrlSource {
    suspend fun getProfilePhoto(): ProfilePhotoModel
}