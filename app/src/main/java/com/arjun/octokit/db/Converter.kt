package com.arjun.octokit.db

import androidx.room.TypeConverter
import com.arjun.octokit.model.License
import com.arjun.octokit.model.Permissions
import com.squareup.moshi.Moshi

class Converter {
    @TypeConverter
    fun stringToLicense(input: String?): License? =
        input?.let { Moshi.Builder().build().adapter(License::class.java).fromJson(it) }

    @TypeConverter
    fun licenseToString(input: License): String? =
        Moshi.Builder().build().adapter(License::class.java).toJson(input)

    @TypeConverter
    fun stringToPermissions(input: String?): Permissions? =
        input?.let { Moshi.Builder().build().adapter(Permissions::class.java).fromJson(it) }

    @TypeConverter
    fun permissionsToString(input: Permissions): String? =
        Moshi.Builder().build().adapter(Permissions::class.java).toJson(input)
}