package com.arjun.octokit.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.arjun.octokit.model.GithubResponseItem
import com.arjun.octokit.model.RemoteKeys

@Database(
    entities = [GithubResponseItem::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class GithubDatabase : RoomDatabase() {

    abstract fun githubRepo(): GithubDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {

        private const val DB_NAME = "github.db"

        @Volatile
        private var instance: GithubDatabase? = null

        fun getInstance(context: Context): GithubDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }

            }

        private fun buildDatabase(context: Context): GithubDatabase =
            Room.databaseBuilder(context.applicationContext, GithubDatabase::class.java, DB_NAME)
                .build()
    }
}