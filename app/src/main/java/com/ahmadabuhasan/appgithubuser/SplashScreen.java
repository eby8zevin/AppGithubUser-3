package com.ahmadabuhasan.appgithubuser;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.ahmadabuhasan.appgithubuser.ui.MainActivity;

import java.util.Objects;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            SplashScreen.this.startActivity(new Intent(SplashScreen.this, MainActivity.class));
            SplashScreen.this.finish();
        }, 3000);
    }
}