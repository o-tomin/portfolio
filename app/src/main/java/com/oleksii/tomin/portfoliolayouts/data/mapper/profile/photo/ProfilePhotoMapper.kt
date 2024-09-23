package com.oleksii.tomin.portfoliolayouts.data.mapper.profile.photo

import com.oleksii.tomin.portfoliolayouts.data.mapper.Mapper
import com.oleksii.tomin.portfoliolayouts.data.model.profile.photo.ProfilePhotoModel
import com.oleksii.tomin.portfoliolayouts.domain.entity.profile.photo.ProfilePhoto

class ProfilePhotoMapper : Mapper<ProfilePhotoModel, ProfilePhoto> {

    override fun mapToDomain(model: ProfilePhotoModel): ProfilePhoto {
        requireNotNull(model.url) { "Profile photo url cant be null" }

        return ProfilePhoto(
            url = "https:${model.url}"
        )
    }
}