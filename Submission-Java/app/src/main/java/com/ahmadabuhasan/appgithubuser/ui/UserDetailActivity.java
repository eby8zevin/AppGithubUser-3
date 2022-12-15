package com.ahmadabuhasan.appgithubuser.ui;

import static com.ahmadabuhasan.appgithubuser.db.DatabaseContract.FavoriteColumns.TABLE_NAME;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.ahmadabuhasan.appgithubuser.R;
import com.ahmadabuhasan.appgithubuser.adapter.ViewPagerAdapter;
import com.ahmadabuhasan.appgithubuser.databinding.ActivityUserDetailBinding;
import com.ahmadabuhasan.appgithubuser.db.DatabaseContract;
import com.ahmadabuhasan.appgithubuser.db.DatabaseHelper;
import com.ahmadabuhasan.appgithubuser.db.FavoriteHelper;
import com.ahmadabuhasan.appgithubuser.model.UserDetail;
import com.ahmadabuhasan.appgithubuser.viewmodel.UserViewModel;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class UserDetailActivity extends AppCompatActivity {

    private ActivityUserDetailBinding binding;
    public static String DETAIL_USER = "DETAIL_USER";
    public static String dataUser;
    private UserViewModel userViewModel;
    private final int[] TAB_CLICK = new int[]{
            R.string.followers,
            R.string.following
    };

    ArrayList<UserDetail> userDetailArrayList = new ArrayList<>();
    private FavoriteHelper favoriteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        setTitle(R.string.detail_user);

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding = ActivityUserDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        favoriteHelper = FavoriteHelper.getFavoriteHelper(getApplicationContext());
        favoriteHelper.open();

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        binding.viewPager.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(binding.tabsLayout, binding.viewPager,
                (tab, position) -> tab.setText(getResources().getString(TAB_CLICK[position]))
        ).attach();

        dataUser = getIntent().getStringExtra(DETAIL_USER);
        showViewModel();
        userViewModel.isLoading().observe(this, this::showLoading);
    }

    private void showViewModel() {
        userViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(UserViewModel.class);
        userViewModel.setUserDetail(dataUser);
        userViewModel.getUserDetail().observe(this, user -> {
            Glide.with(getApplicationContext())
                    .load(user.getAvatarUrl())
                    .into(binding.imgUser);
            binding.tvNameDetail.setText(user.getName());
            binding.tvUsernameDetail.setText(String.format("@%s", user.getUsername()));
            binding.tvCompanyDetail.setText(String.format("%s%s", getResources().getString(R.string.company), user.getCompany()));
            binding.tvLocationDetail.setText(String.format("%s%s", getResources().getString(R.string.location), user.getLocation()));
            binding.tvBlogDetail.setText(String.format("%s%s", getResources().getString(R.string.blog), user.getHtmlUrl()));
            binding.tvFollowers.setText(user.getFollowers());
            binding.tvRepository.setText(user.getRepository());
            binding.tvFollowing.setText(user.getFollowing());

            if (FavoriteExist(dataUser)) {
                binding.fabFavorite.setFavorite(true);
                binding.fabFavorite.setOnFavoriteChangeListener(
                        (buttonView, favorite) -> {
                            if (favorite) {
                                userDetailArrayList = favoriteHelper.getAllFavorite();
                                favoriteHelper.insertFavorite(user);
                                Toasty.success(getApplicationContext(), "Added Favorite", Toasty.LENGTH_SHORT).show();
                            } else {
                                userDetailArrayList = favoriteHelper.getAllFavorite();
                                favoriteHelper.deleteFavorite(dataUser);
                                Toasty.error(getApplicationContext(), "Deleted Favorite", Toasty.LENGTH_SHORT).show();
                            }
                        });
            } else {
                binding.fabFavorite.setOnFavoriteChangeListener(
                        (buttonView, favorite) -> {
                            if (favorite) {
                                userDetailArrayList = favoriteHelper.getAllFavorite();
                                favoriteHelper.insertFavorite(user);
                                Toasty.success(getApplicationContext(), "Added Favorite", Toasty.LENGTH_SHORT).show();
                            } else {
                                userDetailArrayList = favoriteHelper.getAllFavorite();
                                favoriteHelper.deleteFavorite(dataUser);
                                Toasty.error(getApplicationContext(), "Deleted Favorite", Toasty.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    public Boolean FavoriteExist(String user) {
        String select = DatabaseContract.FavoriteColumns.USERNAME + " =?";
        String[] Arg = {user};
        String limit = "1";
        favoriteHelper = new FavoriteHelper(this);
        favoriteHelper.open();
        DatabaseHelper dataBaseHelper = new DatabaseHelper(UserDetailActivity.this);
        SQLiteDatabase database = dataBaseHelper.getWritableDatabase();
        Cursor cursor = database.query(TABLE_NAME, null, select, Arg, null, null, null, limit);
        boolean exists;
        exists = (cursor.getCount() > 0);
        cursor.close();

        return exists;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.share) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://github.com/" + dataUser));
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLoading(Boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
        }
    }
}