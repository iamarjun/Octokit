package com.arjun.octokit.di

import com.arjun.octokit.BuildConfig
import com.arjun.octokit.GithubApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Provides
    fun provideRetrofitBuilder(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }


    @Provides
    fun provideRestApi(retrofit: Retrofit): GithubApi {
        return retrofit.create(GithubApi::class.java)
    }
}