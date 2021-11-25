package com.ahmadabuhasan.appgithubuser.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.ahmadabuhasan.appgithubuser.R;
import com.ahmadabuhasan.appgithubuser.adapter.FavoriteAdapter;
import com.ahmadabuhasan.appgithubuser.adapter.SearchAdapter;
import com.ahmadabuhasan.appgithubuser.databinding.ActivityFavoriteBinding;
import com.ahmadabuhasan.appgithubuser.db.FavoriteHelper;
import com.ahmadabuhasan.appgithubuser.model.SearchData;
import com.ahmadabuhasan.appgithubuser.model.UserDetail;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

import static com.ahmadabuhasan.appgithubuser.ui.UserDetailActivity.DETAIL_USER;

public class FavoriteActivity extends AppCompatActivity {

    private ActivityFavoriteBinding binding;
    private FavoriteAdapter favoriteAdapter;
    FavoriteHelper favoriteHelper;
    ArrayList<UserDetail> userDetailArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        setTitle(getResources().getString(R.string.favorite));

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        favoriteHelper = FavoriteHelper.getFavoriteHelper(FavoriteActivity.this);
        favoriteHelper.open();
        binding.rvFavorite.setHasFixedSize(true);
        showRecyclerView();
        getUserFavorite();
    }

    private void showRecyclerView() {
        if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvFavorite.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            binding.rvFavorite.setLayoutManager(new LinearLayoutManager(this));
        }

        favoriteAdapter = new FavoriteAdapter();
        favoriteAdapter.notifyDataSetChanged();
        binding.rvFavorite.setAdapter(favoriteAdapter);

        favoriteAdapter.setOnItemClickCallback(this::showSelectedUser);
    }

    private void showSelectedUser(SearchData user) {
        Toasty.success(this, "You choose " + user.getUsername(), Toasty.LENGTH_SHORT).show();

        Intent i = new Intent(SearchAdapter.context, UserDetailActivity.class);
        i.putExtra(DETAIL_USER, user.getUsername());
        SearchAdapter.context.startActivity(i);
    }

    private void getUserFavorite() {
        if (userDetailArrayList.size() == 0) {
            binding.rvFavorite.setVisibility(View.GONE);
        } else {
            binding.rvFavorite.setVisibility(View.VISIBLE);
            favoriteAdapter.setFavorite(userDetailArrayList);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}