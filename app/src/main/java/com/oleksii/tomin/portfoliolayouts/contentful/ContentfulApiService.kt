package com.oleksii.tomin.portfoliolayouts.contentful

import ResumeResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ContentfulApiService {

    @GET("environments/master/entries/{entryId}")
    suspend fun getResume(
        @Path("entryId") resumeEntryId: String,
        @Query("access_token") accessToken: String
    ): ResumeResponse
}