package com.dicoding.estomihi.githubsearchuser

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.estomihi.githubsearchuser.adapter.DetailAdapter
import com.dicoding.estomihi.githubsearchuser.adapter.SectionAdapter
import com.dicoding.estomihi.githubsearchuser.data.UserData
import com.dicoding.estomihi.githubsearchuser.database.DatabaseContract
import com.dicoding.estomihi.githubsearchuser.database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.dicoding.estomihi.githubsearchuser.helper.MappingHelper
import com.dicoding.estomihi.githubsearchuser.model.DetailViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.tab_layout.*
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity() {
    private lateinit var detailViewViewModel: DetailViewModel
    private lateinit var adapter: DetailAdapter
    private lateinit var uriWithId : Uri
    private lateinit var detailUserData : UserData
    private var isFavorite = false
    private var menuItem: Menu? = null

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        internal val TAG = DetailActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        adapter = DetailAdapter()
        detailUserData = UserData()

        val username = intent.getStringExtra(EXTRA_USERNAME)
        detailViewViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()
        ).get(DetailViewModel::class.java)

        detailViewViewModel.setUserDetail(username!!)
        detailViewViewModel.getDetailUser().observe(this, { UserDetail ->
            if (UserDetail != null) {
                adapter.setData(UserDetail)
            }
        })
        recyclerViewOptions()
        initToolbar()
        pagerAdapter()

        favoriteCheck()
    }
    private fun favoriteCheck(){
        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + detailUserData.id)
        val cursor = contentResolver.query(uriWithId, null, null, null, null)

        val favorite = MappingHelper.mapCursorToArrayList(cursor)
        for (userData in favorite) {
            if (detailUserData.id == userData.id) {
                isFavorite = true
                Log.d(TAG, "This is your Favorite User")
            }
        }
    }

    private fun insertFavoriteUser(){
        val values = ContentValues().apply {
            put(DatabaseContract.FavoriteColumns._ID, detailUserData.id)
            put(DatabaseContract.FavoriteColumns.USERNAME, detailUserData.username)
            put(DatabaseContract.FavoriteColumns.AVATAR, detailUserData.avatar)
            put(DatabaseContract.FavoriteColumns.URL, detailUserData.url)
            put(DatabaseContract.FavoriteColumns.DATE, getCurrentDate())
        }
        contentResolver.insert(CONTENT_URI, values)
        val favorites = "Add to Favorite User"
        showSnackMessage("${detailUserData.username} $favorites")
    }

    private fun getCurrentDate(): String? {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

    private fun showSnackMessage(message: String) {
        Snackbar.make(viewPager, message, Snackbar.LENGTH_SHORT)
            .show()
    }

    private fun deleteFavoriteUser(){
        contentResolver.delete(uriWithId, null, null)
        val unFav = "Remove User From Favorite"
        showSnackMessage("${detailUserData.username} $unFav")
        Log.d(TAG, "Deleted : $uriWithId")
    }

    private fun setFavoriteIcon(){
        if (isFavorite){
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite_24)
        } else {
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite_border_24)
        }
    }

    private fun recyclerViewOptions() {
        recyclerView_detail.layoutManager = LinearLayoutManager(this)
        recyclerView_detail.adapter = adapter
    }

    private fun pagerAdapter() {
        val sectionsPagerAdapter = SectionAdapter(this, supportFragmentManager)
        viewPager.adapter = sectionsPagerAdapter
        tab_layout.setupWithViewPager(viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu_detail, menu)
        menuItem = menu
        setFavoriteIcon()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.favorite -> {
                if(isFavorite) deleteFavoriteUser() else
                    insertFavoriteUser()

                isFavorite = !isFavorite
                setFavoriteIcon()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun initToolbar() {
        val actionBar = supportActionBar
        actionBar!!.title = resources.getString(R.string.title_detail)
        actionBar.setDisplayHomeAsUpEnabled(true)
    }


}