package com.dicoding.estomihi.githubsearchuser.helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.dicoding.estomihi.githubsearchuser.database.DatabaseContract.FavoriteColumns.Companion.DATE
import com.dicoding.estomihi.githubsearchuser.database.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.dicoding.estomihi.githubsearchuser.database.DatabaseContract.FavoriteColumns.Companion._ID

class FavoriteHelper(context: Context) {
    companion object{
        private const val DATABASE_TABLE = TABLE_NAME
        private  lateinit var databaseHelper: DatabaseHelper
        private var INSTANCE: FavoriteHelper? = null

        fun getInstance(context: Context): FavoriteHelper= INSTANCE ?: synchronized(this){
            INSTANCE ?: FavoriteHelper(context)
        }
        private lateinit var database : SQLiteDatabase
    }
    init{
        databaseHelper = DatabaseHelper(context)
    }
    @Throws(SQLException::class)
    fun open(){
        database = databaseHelper.writableDatabase
    }
    fun queryAll() : Cursor {
        return database.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                "$DATE DESC")
    }

    fun queryById(id : String) : Cursor {
        return database.query(
                DATABASE_TABLE,
                null,
                "$_ID= ?",
                arrayOf(id),
                null,
                null,
                null,
                null)
    }

    fun insert(values: ContentValues?) : Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun deleteById(id: String) : Int {
        return database.delete(DATABASE_TABLE,"$_ID = '$id'", null)
    }

}