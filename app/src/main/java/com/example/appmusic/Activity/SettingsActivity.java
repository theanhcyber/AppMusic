package com.example.appmusic.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.appmusic.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.squareup.picasso.Picasso;

public class SettingsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;

    private ImageView imageViewAvatar;
    private TextView textViewName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initView();
        loadUserData();
        setupBottomNavigation();
    }
    private void initView() {
        imageViewAvatar = findViewById(R.id.profile_imagesetting);
        textViewName = findViewById(R.id.email_textsetting);
    }
    private void loadUserData() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String displayName = sharedPreferences.getString("userName", "Display Name");
        String avatarUrl = sharedPreferences.getString("userAvatar", ""); // Replace with actual key for avatar URL

        // Set display name
        textViewName.setText(displayName);

        // Load avatar image using Picasso
        if (!avatarUrl.isEmpty()) {
            Picasso.with(this).load(avatarUrl).placeholder(R.drawable.profile).into(imageViewAvatar);
        } else {
            imageViewAvatar.setImageResource(R.drawable.profile); // Default avatar if URL is empty
        }
    }
    private void setupBottomNavigation() {
        bottomNavigationView.setSelectedItemId(R.id.navigation_settings);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_settings) {
                    return true;
                } else if (itemId == R.id.navigation_song) {
                    startActivity(new Intent(SettingsActivity.this, QuanLyBaiHat.class));
                    return true;
                } else if (itemId == R.id.navigation_account) {
                    startActivity(new Intent(SettingsActivity.this, QuanLyUser.class));
                    return true;
                } else if (itemId == R.id.navigation_category) {
                    startActivity(new Intent(SettingsActivity.this, QuanLyPlaylis.class));
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    // Phương thức xử lý sự kiện khi click vào Change Password
    public void onChangePasswordClick(View view) {
        Intent intent = new Intent(SettingsActivity.this, ChangePasswordActivity.class);
        startActivity(intent);
    }

    public void onSignOutClick(View view) {
        // Clear user session data (e.g., userId)
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("userId");
        editor.apply();
        Toast.makeText(this, "Sign out successful", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginAppMusicActivity.class);
        startActivity(intent);
        finish(); // Close MainActivity thúc SettingsActivity
    }
}
