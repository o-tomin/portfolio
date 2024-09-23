package com.oleksii.tomin.portfoliolayouts.data.datastore.remote.profile.photo

import com.contentful.java.cda.CDAAsset
import com.contentful.java.cda.CDAClient
import com.oleksii.tomin.portfoliolayouts.data.model.profile.photo.ProfilePhotoModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteProfilePhotoUrlApiImp(
    private val contentfulClient: CDAClient,
) : RemoteProfilePhotoUrlApi {

    override suspend fun fetchProfilePhotoUrl(mediaId: String): ProfilePhotoModel =
        withContext(Dispatchers.IO) {
            return@withContext contentfulClient
                .fetch(CDAAsset::class.java)
                .one(mediaId)
                .url()
                .let(::ProfilePhotoModel)
        }
}