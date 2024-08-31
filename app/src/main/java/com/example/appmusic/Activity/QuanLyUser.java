package com.example.appmusic.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmusic.Adapter.QuanLyUserAdapter;
import com.example.appmusic.Model.User;
import com.example.appmusic.R;
import com.example.appmusic.Service.APIService;
import com.example.appmusic.Service.Dataservice;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuanLyUser extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerViewuser;
    EditText searchBar;
    QuanLyUserAdapter danhsachUserAdapter;
    ArrayList<User> arrUser = new ArrayList<>();
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_user);
        anhxa();
        Innit();
        GetData();
        setupSearchBar();
        setupBottomNavigation();
    }

    private void setupSearchBar() {
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setSelectedItemId(R.id.navigation_account);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if (itemId == R.id.navigation_category) {
                    startActivity(new Intent(QuanLyUser.this, QuanLyPlaylis.class));
                    return true;
                } else if (itemId == R.id.navigation_song) {
                    startActivity(new Intent(QuanLyUser.this, QuanLyBaiHat.class));
                    return true;
                } else if (itemId == R.id.navigation_account) {
                    // Đã ở trong activity này, không cần làm gì thêm
                    return true;
                } else if (itemId == R.id.navigation_settings) {
                    startActivity(new Intent(QuanLyUser.this, SettingsActivity.class));
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void GetData() {
        Dataservice dataservice = APIService.getService();
        Call<List<User>> callback = dataservice.GetUser();
        callback.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                arrUser = (ArrayList<User>) response.body();
                if (arrUser == null) {
                    arrUser = new ArrayList<>(); // Initialize if null
                }
                danhsachUserAdapter = new QuanLyUserAdapter(QuanLyUser.this, arrUser);
                recyclerViewuser.setLayoutManager(new LinearLayoutManager(QuanLyUser.this));
                recyclerViewuser.setAdapter(danhsachUserAdapter);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("QuanLyUser", "Failed to fetch data: " + t.getMessage());
            }
        });
    }

    private void filter(String text) {
        ArrayList<User> filteredList = new ArrayList<>();
        for (User user : arrUser) {
            if (user.getDisplayname().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(user);
            }
        }
        danhsachUserAdapter.updateList(filteredList);
    }

    private void anhxa() {
        toolbar = findViewById(R.id.toolbar);
        recyclerViewuser = findViewById(R.id.recycler_viewuser);
        searchBar = findViewById(R.id.search_bar);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    @SuppressLint("ResourceAsColor")
    private void Innit() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            Log.e("QuanLyUser", "ActionBar is null");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }
}
