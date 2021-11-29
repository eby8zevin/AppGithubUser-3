package com.ahmadabuhasan.appgithubuser.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.ViewModel;

import com.ahmadabuhasan.appgithubuser.setting.SettingPreferences;

public class SettingViewModel extends ViewModel {
    private final SettingPreferences pref;

    public SettingViewModel(SettingPreferences pref) {
        this.pref = pref;
    }

    public LiveData<Boolean> getThemeSettings() {
        return LiveDataReactiveStreams.fromPublisher(pref.getThemeSetting());
    }

    public void saveThemeSetting(Boolean isDarkModeActive) {
        pref.saveThemeSetting(isDarkModeActive);
    }
}