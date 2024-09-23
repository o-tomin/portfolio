package com.oleksii.tomin.portfoliolayouts.data.datastore.remote.profile.photo

import com.oleksii.tomin.portfoliolayouts.data.model.profile.photo.ProfilePhotoModel

interface RemoteProfilePhotoUrlApi {
    suspend fun fetchProfilePhotoUrl(mediaId: String): ProfilePhotoModel
}