package com.arjun.octokit.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Permissions(
    @Json(name = "admin")
    var admin: Boolean,
    @Json(name = "pull")
    var pull: Boolean,
    @Json(name = "push")
    var push: Boolean
)