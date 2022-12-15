package com.ahmadabuhasan.appgithubuser.setting;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ahmadabuhasan.appgithubuser.viewmodel.SettingViewModel;

public class SettingViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final SettingPreferences pref;

    public SettingViewModelFactory(SettingPreferences dataStore) {
        this.pref = dataStore;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SettingViewModel.class)) {
            return (T) new SettingViewModel(pref);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}