package com.oleksii.tomin.portfoliolayouts.data.repository

import com.oleksii.tomin.portfoliolayouts.domain.entity.profile.photo.ProfilePhoto

interface ProfilePhotoRepository {
    suspend fun getProfilePhoto(): ProfilePhoto
}