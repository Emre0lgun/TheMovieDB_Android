package com.pointo.movies.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginModel(
    val username: String?,
    val loginRequired: Boolean?
) : Parcelable