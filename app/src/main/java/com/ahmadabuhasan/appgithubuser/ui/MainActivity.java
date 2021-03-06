package com.ahmadabuhasan.appgithubuser.ui;

import static com.ahmadabuhasan.appgithubuser.ui.UserDetailActivity.DETAIL_USER;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ahmadabuhasan.appgithubuser.R;
import com.ahmadabuhasan.appgithubuser.adapter.SearchAdapter;
import com.ahmadabuhasan.appgithubuser.databinding.ActivityMainBinding;
import com.ahmadabuhasan.appgithubuser.model.SearchData;
import com.ahmadabuhasan.appgithubuser.setting.SettingPreferences;
import com.ahmadabuhasan.appgithubuser.setting.SettingViewModelFactory;
import com.ahmadabuhasan.appgithubuser.viewmodel.SettingViewModel;
import com.ahmadabuhasan.appgithubuser.viewmodel.UserViewModel;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private UserViewModel userViewModel;
    private SearchAdapter searchAdapter;
    private long backPressed;

    private SettingViewModel settingViewModel;
    private SettingPreferences pref;
    ArrayList<SearchData> searchDataArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.users_search);

        RxDataStore<Preferences> dataStore = new RxPreferenceDataStoreBuilder(this, "settings").build();
        pref = SettingPreferences.getInstance(dataStore);
        darkModeCheck();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.NoData.setVisibility(View.VISIBLE);
        binding.rvGithub.setHasFixedSize(true);
        showViewModel();
        showRecyclerView();
        userViewModel.isLoading().observe(this, this::showLoading);

        binding.fab.setOnClickListener(v -> showFavorite());
    }

    private void showViewModel() {
        userViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(UserViewModel.class);
        userViewModel.getSearchData().observe(this, searchData -> {
            if (searchData.size() != 0) {
                binding.NoData.setVisibility(View.GONE);
                searchAdapter.setSearchData(searchData);
            } else {
                binding.NoData.setVisibility(View.VISIBLE);
                Toasty.warning(getApplicationContext(), "User Not Found!", Toasty.LENGTH_SHORT).show();
            }
        });
    }

    private void showRecyclerView() {
        if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvGithub.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            binding.rvGithub.setLayoutManager(new LinearLayoutManager(this));
        }

        searchAdapter = new SearchAdapter(this);
        searchAdapter.notifyDataSetChanged();
        binding.rvGithub.setAdapter(searchAdapter);

        searchAdapter.setOnItemClickCallback(this::showSelectedUser);
    }

    private void showSelectedUser(SearchData user) {
        Toasty.success(this, "You choose " + user.getUsername(), Toasty.LENGTH_SHORT).show();

        Intent i = new Intent(SearchAdapter.context, UserDetailActivity.class);
        i.putExtra(DETAIL_USER, user.getUsername());
        SearchAdapter.context.startActivity(i);
        hideKeyboard();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        if (searchManager != null) {
            SearchView searchView = (SearchView) (menu.findItem(R.id.search)).getActionView();
            MenuItem closeSearch = menu.findItem(R.id.search);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.search_hint));
            searchView.setIconifiedByDefault(true);
            searchView.setIconified(false);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchDataArrayList.clear();
                    searchAdapter.setSearchData(searchDataArrayList);
                    searchAdapter.notifyDataSetChanged();
                    userViewModel.setSearchUser(query);
                    searchView.clearFocus();
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    //userViewModel.setSearchUser(newText);
                    return false;
                }
            });
            closeSearch.getIcon().setVisible(false, false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.setting) {
            startActivity(new Intent(this, SettingActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void onBackPressed() {
        if (backPressed + 2000 > System.currentTimeMillis()) {
            finishAffinity();
        } else {
            Toasty.warning(this, "Press once again to exit", Toasty.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();
    }

    private void showFavorite() {
        Intent i = new Intent(this, FavoriteActivity.class);
        startActivity(i);
    }

    private void darkModeCheck() {
        settingViewModel = new ViewModelProvider(this, new SettingViewModelFactory(pref)).get(SettingViewModel.class);
        settingViewModel.getThemeSettings().observe(this, isDarkModeActive -> {
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
    }

    private void showLoading(Boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
        }
    }
}