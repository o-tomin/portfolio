package com.oleksii.tomin.portfoliolayouts.di

import com.contentful.java.cda.CDAClient
import com.oleksii.tomin.portfoliolayouts.contentful.ContentfulApiService
import com.oleksii.tomin.portfoliolayouts.contentful.ContentfulMediaService
import com.oleksii.tomin.portfoliolayouts.contentful.ContentfulService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
object ContentfulServiceModule {

    @Provides
    fun provideCdaClient(
        @Named(NamedValues.CONTENTFUL_SPACE_ID)
        spaceId: String,
        @Named(NamedValues.CONTENTFUL_AUTHORISATION_TOKEN)
        authorisationToken: String,
    ) =
        CDAClient.builder()
            .setSpace(spaceId)
            .setToken(authorisationToken)
            .build()

    @Provides
    @Named(NamedValues.CONTENTFUL_SPACE_ID)
    fun provideContentfulSpaceId() =
        "jqb6fezzjx0p"

    @Provides
    @Named(NamedValues.CONTENTFUL_AUTHORISATION_TOKEN)
    fun provideContentfulAuthorisationToken() =
        "tyc4eq70xL-BLGRPqI87DiXvTiBDyLaT-Sy6YHnajgs"

    @Provides
    fun provideContentfulApiService(
        @Named(NamedValues.CONTENTFUL_SPACE_ID)
        spaceId: String,
    ): ContentfulApiService {

        // Create an interceptor instance
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            // Set the desired log level (BODY will show full request/response details)
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Build OkHttp client and attach interceptor
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()


        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://cdn.contentful.com/spaces/$spaceId/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        return retrofit.create(ContentfulApiService::class.java)
    }

    @Provides
    fun provideContentfulMediaService(
        cdaClient: CDAClient,
    ): ContentfulMediaService {
        return ContentfulMediaService(
            cdaClient,
        )
    }

    @Provides
    fun provideContentfulService(
        contentfulMediaService: ContentfulMediaService,
        contentfulApiService: ContentfulApiService,
        @Named(NamedValues.CONTENTFUL_AUTHORISATION_TOKEN)
        accessToken: String,
    ): ContentfulService {
        return ContentfulService(
            contentfulMediaService = contentfulMediaService,
            contentfulApiService = contentfulApiService,
            accessToken = accessToken
        )
    }
}