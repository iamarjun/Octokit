package com.arjun.octokit.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.arjun.octokit.GithubApi
import com.arjun.octokit.db.GithubDatabase
import com.arjun.octokit.model.GithubResponseItem
import kotlinx.coroutines.flow.Flow

/**
 * Repository class that works with local and remote data sources.
 */
class GithubRepository(
    private val githubApi: GithubApi,
    private val githubDatabase: GithubDatabase
) {

    /**
     * Search repositories whose names match the query, exposed as a stream of data that will emit
     * every time we get more data from the network.
     */
    fun getSearchResultStream(query: String): Flow<PagingData<GithubResponseItem>> {
        Log.d("GithubRepository", "New query: $query")

        // appending '%' so we can allow other characters to be before and after the query string
        val dbQuery = "%${query.replace(' ', '%')}%"
        val pagingSourceFactory = { githubDatabase.githubRepo().reposByName(dbQuery) }

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
