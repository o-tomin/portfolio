package com.oleksii.tomin.portfoliolayouts.di

import com.contentful.java.cda.CDAClient
import com.oleksii.tomin.portfoliolayouts.contentful.ContentfulService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
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
    fun provideContentfulService(
        cdaClient: CDAClient,
    ) =
        ContentfulService(
            cdaClient,
        )
}