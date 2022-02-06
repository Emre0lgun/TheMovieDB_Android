package com.pointo.movies.data.models

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Genre(
    val id: Int,
    val name: String
) : Parcelable