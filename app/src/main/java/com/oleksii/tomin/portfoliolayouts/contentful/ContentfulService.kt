package com.oleksii.tomin.portfoliolayouts.contentful

import com.contentful.java.cda.CDAAsset
import com.contentful.java.cda.CDAClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContentfulService(
    private val contentfulClient: CDAClient,
) {

    suspend fun fetchProfilePhotoUrl(): String = withContext(Dispatchers.IO) {
        return@withContext contentfulClient
            .fetch(CDAAsset::class.java)
            .one(PROFILE_PHOTO_MEDIA_ID)
            .url()
    }

    companion object {
        const val PROFILE_PHOTO_MEDIA_ID = "6pNXsbMWXKfKrRu1HLJGNn"
    }
}