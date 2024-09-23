package com.oleksii.tomin.portfoliolayouts.data.datastore.remote.resume

import com.oleksii.tomin.portfoliolayouts.data.model.resume.ResumeResponseModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ResumeApi {

    @GET("environments/master/entries/{entryId}")
    suspend fun getResume(
        @Path("entryId") resumeEntryId: String,
        @Query("access_token") accessToken: String
    ): ResumeResponseModel
}