package com.arjun.octokit.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.arjun.octokit.GithubApi
import com.arjun.octokit.db.GithubDatabase
import com.arjun.octokit.db.RemoteKeys
import com.arjun.octokit.model.GithubResponseItem
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException

// GitHub page API is 1 based: https://developer.github.com/v3/#pagination
private const val GITHUB_STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class GithubRemoteMediator(
    private val githubApi: GithubApi,
    private val githubDatabase: GithubDatabase
) : RemoteMediator<Int, GithubResponseItem>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, GithubResponseItem>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: GITHUB_STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                when {
                    remoteKeys == null -> GITHUB_STARTING_PAGE_INDEX
                    remoteKeys.nextKey == null -> throw InvalidObjectException("Remote key should not be null for $loadType")
                    else -> remoteKeys.nextKey
                }
            }

        }

        try {
            val apiResponse = githubApi.fetchRepos(page, state.config.pageSize)

            val endOfPaginationReached = apiResponse.isEmpty()
            githubDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    githubDatabase.remoteKeysDao().clearRemoteKeys()
                    githubDatabase.githubRepo().clearRepos()
                }
                val prevKey = if (page == GITHUB_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = apiResponse.map {
                    RemoteKeys(repoId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                githubDatabase.remoteKeysDao().insertAll(keys)
                githubDatabase.githubRepo().insertAll(apiResponse)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, GithubResponseItem>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { repo ->
                // Get the remote keys of the last item retrieved
                githubDatabase.remoteKeysDao().remoteKeysRepoId(repo.id)
            }
    }

//    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, GithubResponseItem>): RemoteKeys? {
//        // Get the first page that was retrieved, that contained items.
//        // From that first page, get the first item
//        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
//            ?.let { repo ->
//                // Get the remote keys of the first items retrieved
//                githubDatabase.remoteKeysDao().remoteKeysRepoId(repo.id)
//            }
//    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, GithubResponseItem>
    ): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                githubDatabase.remoteKeysDao().remoteKeysRepoId(repoId)
            }
        }
    }

}