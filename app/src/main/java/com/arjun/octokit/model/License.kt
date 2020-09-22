package com.arjun.octokit.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class License(
    @Json(name = "key")
    var key: String,
    @Json(name = "name")
    var name: String,
    @Json(name = "node_id")
    var nodeId: String,
    @Json(name = "spdx_id")
    var spdxId: String,
    @Json(name = "url")
    var url: String
)