package com.pointo.movies.data.models

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class BelongsToCollection(
    val backdrop_path: String?,
    val id: Int,
    val name: String,
    val poster_path: String?
) : Parcelable