package com.ahmadabuhasan.appgithubuser.ui

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmadabuhasan.appgithubuser.R
import com.ahmadabuhasan.appgithubuser.adapter.FavoriteAdapter
import com.ahmadabuhasan.appgithubuser.databinding.ActivityFavoriteBinding
import com.ahmadabuhasan.appgithubuser.db.FavoriteHelper
import com.ahmadabuhasan.appgithubuser.model.ResponseDetailUser

class FavoriteActivity : AppCompatActivity() {

    private val adapter = FavoriteAdapter()
    private lateinit var helper: FavoriteHelper
    private var listFavorite: ArrayList<ResponseDetailUser> = ArrayList()

    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.favorite)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        helper = FavoriteHelper.getInstance(applicationContext)
        helper.open()

        showRecyclerView()
        getDataFavorite()
    }

    private fun showRecyclerView() {
        binding.rvUser.layoutManager =
            if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                GridLayoutManager(this, 2)
            } else {
                LinearLayoutManager(this)
            }

        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.adapter = adapter

        adapter.setOnItemClickCallback { data -> selectedUser(data) }
    }

    private fun selectedUser(user: ResponseDetailUser) {
        Toast.makeText(this, "You choose ${user.login}", Toast.LENGTH_SHORT).show()

        val i = Intent(this, UserDetailActivity::class.java)
        i.putExtra(UserDetailActivity.EXTRA_USER, user.login)
        startActivity(i)
    }

    private fun getDataFavorite() {
        listFavorite = helper.queryAll()
        if (listFavorite.size > 0) {
            binding.rvUser.visibility = View.VISIBLE
            binding.tvNotFound.visibility = View.GONE
            adapter.setData(listFavorite)
        } else {
            binding.rvUser.visibility = View.GONE
            binding.tvNotFound.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        getDataFavorite()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}