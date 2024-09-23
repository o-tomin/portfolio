package com.oleksii.tomin.portfoliolayouts.di

import com.contentful.java.cda.CDAClient
import com.oleksii.tomin.portfoliolayouts.data.datastore.remote.profile.photo.RemoteProfilePhotoUrlApi
import com.oleksii.tomin.portfoliolayouts.data.datastore.remote.profile.photo.RemoteProfilePhotoUrlApiImp
import com.oleksii.tomin.portfoliolayouts.data.datastore.remote.profile.photo.RemoteProfilePhotoUrlSource
import com.oleksii.tomin.portfoliolayouts.data.datastore.remote.profile.photo.RemoteProfilePhotoUrlSourceImpl
import com.oleksii.tomin.portfoliolayouts.data.datastore.remote.resume.RemoteResumeDetailsSource
import com.oleksii.tomin.portfoliolayouts.data.datastore.remote.resume.RemoteResumeDetailsSourceImpl
import com.oleksii.tomin.portfoliolayouts.data.datastore.remote.resume.ResumeApi
import com.oleksii.tomin.portfoliolayouts.data.mapper.profile.photo.ProfilePhotoMapper
import com.oleksii.tomin.portfoliolayouts.data.mapper.resume.ContactsMapper
import com.oleksii.tomin.portfoliolayouts.data.mapper.resume.EducationMapper
import com.oleksii.tomin.portfoliolayouts.data.mapper.resume.ExperienceMapper
import com.oleksii.tomin.portfoliolayouts.data.mapper.resume.ResumeMapper
import com.oleksii.tomin.portfoliolayouts.data.mapper.resume.StackMapper
import com.oleksii.tomin.portfoliolayouts.data.repository.ProfilePhotoRepository
import com.oleksii.tomin.portfoliolayouts.data.repository.ProfilePhotoRepositoryImpl
import com.oleksii.tomin.portfoliolayouts.data.repository.ResumeRepository
import com.oleksii.tomin.portfoliolayouts.data.repository.ResumeRepositoryImpl
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
object DataModule {

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
    ): ResumeApi {

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

        return retrofit.create(ResumeApi::class.java)
    }

    @Provides
    fun provideContentfulMediaService(
        cdaClient: CDAClient,
    ): RemoteProfilePhotoUrlApi =
        RemoteProfilePhotoUrlApiImp(
            cdaClient,
        )

    @Provides
    fun provideProfilePhotoMapper() =
        ProfilePhotoMapper()

    @Provides
    fun provideContactsMapper() =
        ContactsMapper()

    @Provides
    fun provideStackMapper() =
        StackMapper()

    @Provides
    fun provideExperienceMapper() =
        ExperienceMapper()

    @Provides
    fun provideEducationMapper() =
        EducationMapper()

    @Provides
    fun provideResumeMapper(
        contactsMapper: ContactsMapper,
        stackMapper: StackMapper,
        experienceMapper: ExperienceMapper,
        educationMapper: EducationMapper,
    ) =
        ResumeMapper(
            contactsMapper = contactsMapper,
            stackMapper = stackMapper,
            experienceMapper = experienceMapper,
            educationMapper = educationMapper,
        )

    @Provides
    fun provideRemoteProfilePhotoUrlSource(
        remoteProfilePhotoUrlApi: RemoteProfilePhotoUrlApi
    ): RemoteProfilePhotoUrlSource =
        RemoteProfilePhotoUrlSourceImpl(
            remoteProfilePhotoUrlApi,
        )

    @Provides
    fun provideRemoteResumeDetailsSource(
        resumeApi: ResumeApi,
        @Named(NamedValues.CONTENTFUL_AUTHORISATION_TOKEN)
        accessToken: String,
    ): RemoteResumeDetailsSource =
        RemoteResumeDetailsSourceImpl(
            resumeApi = resumeApi,
            accessToken = accessToken,
        )

    @Provides
    fun provideProfilePhotoRepository(
        remoteProfilePhotoUrlSource: RemoteProfilePhotoUrlSource,
        profilePhotoMapper: ProfilePhotoMapper,
    ): ProfilePhotoRepository {
        return ProfilePhotoRepositoryImpl(
            remoteProfilePhotoUrlSource = remoteProfilePhotoUrlSource,
            profilePhotoMapper = profilePhotoMapper
        )
    }

    @Provides
    fun provideResumeRepository(
        remoteResumeDetailsSource: RemoteResumeDetailsSource,
        resumeMapper: ResumeMapper
    ): ResumeRepository =
        ResumeRepositoryImpl(
            remoteResumeDetailsSource = remoteResumeDetailsSource,
            resumeMapper = resumeMapper
        )
}