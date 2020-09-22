package com.arjun.octokit.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arjun.octokit.model.GithubResponseItem

@Dao
interface GithubDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<GithubResponseItem>)

    @Query("SELECT * FROM githubresponseitem")
    fun getRepos(): PagingSource<Int, GithubResponseItem>

    @Query("DELETE FROM githubresponseitem")
    suspend fun clearRepos()

}