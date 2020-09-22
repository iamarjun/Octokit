package com.arjun.octokit

import com.arjun.octokit.model.GithubResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApi {

    @GET("orgs/octokit/repos")
    suspend fun fetchRepos(
        @Query("page") pageNo: Int,
        @Query("per_page") pageSize: Int,
    ): GithubResponse
}