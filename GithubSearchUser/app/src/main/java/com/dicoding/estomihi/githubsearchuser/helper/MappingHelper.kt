package com.dicoding.estomihi.githubsearchuser.helper

import android.database.Cursor
import com.dicoding.estomihi.githubsearchuser.data.UserData
import com.dicoding.estomihi.githubsearchuser.database.DatabaseContract

object MappingHelper{
    fun mapCursorToArrayList(usersCursor : Cursor?) : ArrayList<UserData>{
        val usersList = ArrayList<UserData>()

        usersCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns._ID))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.USERNAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATAR))
                val url = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.URL))

                usersList.add(UserData
                    (id =id,
                    username = username,
                    avatar = avatar,
                    url = url))
            }
        }
        return usersList
    }
}