package com.example.appmusic.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.appmusic.Fragment.Fragment_Banner;
import com.example.appmusic.Fragment.Fragment_Playlist;
import com.example.appmusic.Fragment.Fragment_Trang_Chu;
import com.example.appmusic.R;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ImageView imageViewAvatar;
    private TextView textViewName;
    private SharedPreferences sharedPreferences;
    private boolean isLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        imageViewAvatar = headerView.findViewById(R.id.nav_header_image);
        textViewName = headerView.findViewById(R.id.nav_header_text);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Load the default fragment (Trang Chủ) when the app starts
        if (savedInstanceState == null) {
            loadFragment(new Fragment_Trang_Chu());
            navigationView.setCheckedItem(R.id.home);
        }

        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        isLoggedIn = sharedPreferences.contains("userId");

        // Update menu items visibility based on login status
        updateMenuItems(navigationView.getMenu());

        loadUserData();

        // Handle the back press
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }

    private void promptLogin() {
        Toast.makeText(this, "You must be logged in to access this feature", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginAppMusicActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;

        int id = item.getItemId();

        if (id == R.id.home) {
            selectedFragment = new Fragment_Trang_Chu();
        } else if (id == R.id.category) {
            selectedFragment = new Fragment_Banner();
        } else if (id == R.id.all_songs) {
            selectedFragment = new Fragment_Playlist();
        } else if (id == R.id.popular_songs) {
            // Add your code here
        } else if (id == R.id.userprofile) {
            if (isLoggedIn) {
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
            } else {
                promptLogin();
                return true;
            }
        } else if (id == R.id.change_password) {
            if (isLoggedIn) {
                Intent intent = new Intent(this, ChangePasswordActivity.class);
                startActivity(intent);
            } else {
                promptLogin();
                return true;
            }
        } else if (id == R.id.favorite_songs) {
            if (isLoggedIn) {
                Intent intent = new Intent(MainActivity.this, LikedSongsActivity.class);
                startActivity(intent);
            } else {
                promptLogin();
                return true;
            }
        } else if (!isLoggedIn && id == R.id.login) {
            Intent intent = new Intent(MainActivity.this, LoginAppMusicActivity.class);
            startActivity(intent);
        } else if (id == R.id.sign_out) {
            signOut();
        }

        if (selectedFragment != null) {
            loadFragment(selectedFragment);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadUserData() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String displayName = sharedPreferences.getString("userName", "Display Name");
        String avatarUrl = sharedPreferences.getString("userAvatar", ""); // Replace with actual key for avatar URL

        // Set display name
        textViewName.setText(displayName);

        if (!avatarUrl.isEmpty()) {
            Picasso.with(this).load(avatarUrl).placeholder(R.drawable.ic_launcher_foreground).into(imageViewAvatar);
        } else {
            imageViewAvatar.setImageResource(R.drawable.ic_launcher_foreground); // Default avatar if URL is empty
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void signOut() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        isLoggedIn = false;

        Toast.makeText(this, "Signed out successfully", Toast.LENGTH_SHORT).show();

        // Redirect to main screen
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem loginItem = menu.findItem(R.id.login);
        MenuItem signOutItem = menu.findItem(R.id.sign_out);

        loginItem.setVisible(!isLoggedIn);
        signOutItem.setVisible(isLoggedIn);

        return super.onPrepareOptionsMenu(menu);
    }

    private void updateMenuItems(Menu menu) {
        MenuItem loginItem = menu.findItem(R.id.login);
        MenuItem signOutItem = menu.findItem(R.id.sign_out);

        loginItem.setVisible(!isLoggedIn);
        signOutItem.setVisible(isLoggedIn);
    }

    private void updateLoginStatus(boolean loggedIn) {
        isLoggedIn = loggedIn;
        invalidateOptionsMenu();
    }
}
