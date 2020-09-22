package com.arjun.octokit.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity
@JsonClass(generateAdapter = true)
data class GithubResponseItem(
    @PrimaryKey
    @Json(name = "id")
    var id: Int,
    @Json(name = "description")
    var description: String?,
    @Json(name = "full_name")
    var fullName: String?,
    @Json(name = "license")
    var license: License?,
    @Json(name = "name")
    var name: String?,
    @Json(name = "open_issues_count")
    var openIssuesCount: Int?,
    @Json(name = "permissions")
    var permissions: Permissions?,
)