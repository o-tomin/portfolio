package com.oleksii.tomin.portfoliolayouts.contentful

import ResumeDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContentfulService(
    private val contentfulMediaService: ContentfulMediaService,
    private val contentfulApiService: ContentfulApiService,
    private val accessToken: String,
) {

    suspend fun fetchProfilePhotoUrl(): String = withContext(Dispatchers.IO) {
        return@withContext contentfulMediaService.fetchProfilePhotoUrl()
    }

    suspend fun getContentfulResume(): ResumeDetails = withContext(Dispatchers.IO) {
        return@withContext contentfulApiService.getResume(
            RESUME_ENTRY_ID,
            accessToken
        ).fields.resume
    }

    companion object {
        const val RESUME_ENTRY_ID = "3VIY4mJFJM9Y8yztlDyZAD"
    }
}