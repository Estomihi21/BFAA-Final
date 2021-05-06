package com.dicoding.estomihi.githubsearchuser.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserData (
    var id          : Int = 0,
    var url         : String? = "",
    var date        : String? = null,
    var name        : String? = "",
    var username    : String? = "",
    var login       : String? = "",
    var location    : String? = "",
    var company     : String? = "",
    var repository  : Int? = 0,
    var followers   : Int? = 0,
    var following   : Int? = 0,
    var avatar     : String? = null
) : Parcelable