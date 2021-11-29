package com.ahmadabuhasan.appgithubuser.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;
import androidx.lifecycle.ViewModelProvider;

import com.ahmadabuhasan.appgithubuser.R;
import com.ahmadabuhasan.appgithubuser.databinding.ActivitySettingBinding;
import com.ahmadabuhasan.appgithubuser.setting.SettingPreferences;
import com.ahmadabuhasan.appgithubuser.setting.SettingViewModelFactory;
import com.ahmadabuhasan.appgithubuser.viewmodel.SettingViewModel;

public class SettingActivity extends AppCompatActivity {

    private ActivitySettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        showViewModel();
    }

    private void showViewModel() {
        RxDataStore<Preferences> dataStore = new RxPreferenceDataStoreBuilder(this, "settings").build();
        SettingPreferences pref = SettingPreferences.getInstance(dataStore);
        SettingViewModel settingViewModel = new ViewModelProvider(this, new SettingViewModelFactory(pref)).get(SettingViewModel.class);
        settingViewModel.getThemeSettings().observe(this, isDarkModeActive -> {
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                binding.switchTheme.setChecked(true);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                binding.switchTheme.setChecked(false);
            }
        });

        binding.switchTheme.setOnCheckedChangeListener((buttonView, isChecked) ->
                settingViewModel.saveThemeSetting(isChecked)
        );
    }
}