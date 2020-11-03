package com.example.railyatri.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.railyatri.Utils.SharedPref;
import com.google.firebase.messaging.FirebaseMessaging;


public class SplashActivity extends AppCompatActivity {

    private SharedPref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseMessaging.getInstance().subscribeToTopic("allDevices");

        pref = new SharedPref(SplashActivity.this);

        new Handler().postDelayed(() -> {
            if (pref.getFirstName().equals("")) {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, 500);
    }
}