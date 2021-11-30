package com.ahmadabuhasan.appgithubuser.ui;

import static com.ahmadabuhasan.appgithubuser.ui.UserDetailActivity.DETAIL_USER;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ahmadabuhasan.appgithubuser.R;
import com.ahmadabuhasan.appgithubuser.adapter.FavoriteAdapter;
import com.ahmadabuhasan.appgithubuser.adapter.SearchAdapter;
import com.ahmadabuhasan.appgithubuser.databinding.ActivityFavoriteBinding;
import com.ahmadabuhasan.appgithubuser.db.FavoriteHelper;
import com.ahmadabuhasan.appgithubuser.model.UserDetail;

import java.util.ArrayList;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class FavoriteActivity extends AppCompatActivity {

    private ActivityFavoriteBinding binding;
    private FavoriteAdapter favoriteAdapter;
    private FavoriteHelper favoriteHelper;
    ArrayList<UserDetail> userDetailArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        setTitle(getResources().getString(R.string.favorite));

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        favoriteHelper = FavoriteHelper.getFavoriteHelper(FavoriteActivity.this);
        favoriteHelper.open();

        showRecyclerView();
        binding.rvFavorite.setHasFixedSize(true);
        getDataFavorite();
    }

    private void showRecyclerView() {
        if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvFavorite.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            binding.rvFavorite.setLayoutManager(new LinearLayoutManager(this));
        }

        favoriteAdapter = new FavoriteAdapter(this);
        binding.rvFavorite.setAdapter(favoriteAdapter);

        favoriteAdapter.setOnItemClickCallback(this::showSelectedUser);
    }

    private void showSelectedUser(UserDetail user) {
        Toasty.success(this, "You choose " + user.getUsername(), Toasty.LENGTH_SHORT).show();

        Intent i = new Intent(FavoriteAdapter.context, UserDetailActivity.class);
        i.putExtra(DETAIL_USER, user.getUsername());
        SearchAdapter.context.startActivity(i);
    }

    private void getDataFavorite() {
        userDetailArrayList = favoriteHelper.getAllFavorite();
        if (userDetailArrayList.size() == 0) {
            binding.NoData.setVisibility(View.VISIBLE);
            binding.rvFavorite.setVisibility(View.GONE);
        } else {
            binding.NoData.setVisibility(View.GONE);
            binding.rvFavorite.setVisibility(View.VISIBLE);
            favoriteAdapter.setFavorite(userDetailArrayList);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataFavorite();
        Log.d("Check", String.valueOf(userDetailArrayList));
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