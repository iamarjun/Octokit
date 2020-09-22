package com.arjun.octokit.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.arjun.octokit.GithubApi
import com.arjun.octokit.db.GithubDatabase
import com.arjun.octokit.model.GithubResponseItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Repository class that works with local and remote data sources.
 */
class GithubRepository @Inject constructor(
    private val githubApi: GithubApi,
    private val githubDatabase: GithubDatabase
) {

    /**
     * Fetches repositories whose names match the query, exposed as a stream of data that will emit
     * every time we get more data from the network.
     */
    fun resultStream(): Flow<PagingData<GithubResponseItem>> {

        val pagingSourceFactory = { githubDatabase.githubRepo().getRepos() }

        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = GithubRemoteMediator(
                githubApi,
                githubDatabase
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }
}
