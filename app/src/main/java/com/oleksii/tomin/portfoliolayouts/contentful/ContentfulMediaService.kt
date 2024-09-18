package com.oleksii.tomin.portfoliolayouts.contentful

import com.contentful.java.cda.CDAAsset
import com.contentful.java.cda.CDAClient

class ContentfulMediaService(
    private val contentfulClient: CDAClient,
) {

    fun fetchProfilePhotoUrl(): String {
        return contentfulClient
            .fetch(CDAAsset::class.java)
            .one(PROFILE_PHOTO_MEDIA_ID)
            .url()!!
    }

    companion object {
        const val PROFILE_PHOTO_MEDIA_ID = "6pNXsbMWXKfKrRu1HLJGNn"
    }
}