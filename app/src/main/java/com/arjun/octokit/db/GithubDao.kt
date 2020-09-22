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

    @Query(
        "SELECT * FROM github WHERE " +
                "name LIKE :queryString OR description LIKE :queryString " +
                "ORDER BY stars DESC, name ASC"
    )
    fun reposByName(queryString: String): PagingSource<Int, GithubResponseItem>

    @Query("DELETE FROM github")
    suspend fun clearRepos()

}