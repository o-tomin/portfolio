package com.oleksii.tomin.portfoliolayouts.data.datastore.remote.resume

import com.oleksii.tomin.portfoliolayouts.data.model.resume.ResumeResponseModel

class RemoteResumeDetailsSourceImpl(
    private val resumeApi: ResumeApi,
    private val accessToken: String,
) : RemoteResumeDetailsSource {

    override suspend fun getResume(): ResumeResponseModel {
        return resumeApi.getResume(RESUME_ENTRY_ID, accessToken)
    }

    companion object {
        const val RESUME_ENTRY_ID = "3VIY4mJFJM9Y8yztlDyZAD"
    }
}