package com.oleksii.tomin.portfoliolayouts.data.datastore.remote.resume

import com.oleksii.tomin.portfoliolayouts.data.model.resume.ResumeResponseModel

interface RemoteResumeDetailsSource {
    suspend fun getResume(): ResumeResponseModel
}