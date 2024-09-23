package com.oleksii.tomin.portfoliolayouts.data.repository

import com.oleksii.tomin.portfoliolayouts.data.datastore.remote.profile.photo.RemoteProfilePhotoUrlSource
import com.oleksii.tomin.portfoliolayouts.data.mapper.Mapper
import com.oleksii.tomin.portfoliolayouts.data.model.profile.photo.ProfilePhotoModel
import com.oleksii.tomin.portfoliolayouts.domain.entity.profile.photo.ProfilePhoto

class ProfilePhotoRepositoryImpl(
    private val remoteProfilePhotoUrlSource: RemoteProfilePhotoUrlSource,
    private val profilePhotoMapper: Mapper<ProfilePhotoModel, ProfilePhoto>,
) : ProfilePhotoRepository {

    override suspend fun getProfilePhoto(): ProfilePhoto {
        return remoteProfilePhotoUrlSource.getProfilePhoto().let {
            profilePhotoMapper.mapToDomain(it)
        }
    }
}