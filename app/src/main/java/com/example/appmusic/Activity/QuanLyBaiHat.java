package com.example.appmusic.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appmusic.Adapter.QuanLyBaiHatAdapter;
import com.example.appmusic.Model.Songs;
import com.example.appmusic.R;
import com.example.appmusic.Service.APIService;
import com.example.appmusic.Service.Dataservice;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuanLyBaiHat extends AppCompatActivity {
    Toolbar toolbar;
    EditText searchBar;
    RecyclerView recycler_viewmanagelist;
    QuanLyBaiHatAdapter quanLyBaiHatAdapter;
    ArrayList<Songs> arrplaylist;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_song);
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

        bottomNavigationView.setSelectedItemId(R.id.navigation_song);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.navigation_category) {
                    startActivity(new Intent(QuanLyBaiHat.this, QuanLyPlaylis.class));
                    return true;
                } else if (itemId == R.id.navigation_song) {
                    // Đã ở trong activity này, không cần làm gì thêm
                    return true;
                } else if (itemId == R.id.navigation_account) {
                    startActivity(new Intent(QuanLyBaiHat.this, QuanLyUser.class));
                    return true;
                } else if (itemId == R.id.navigation_settings) {
                    startActivity(new Intent(QuanLyBaiHat.this, SettingsActivity.class));
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void filter(String text) {
        ArrayList<Songs> filteredList = new ArrayList<>();

        for (Songs song : arrplaylist) {
            if (song.getNameSong().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(song);
            }
        }

        quanLyBaiHatAdapter.updateList(filteredList);
    }

    private void GetData() {
        Dataservice dataservice = APIService.getService();
        Call<List<Songs>> callback = dataservice.GetSongs();
        callback.enqueue(new Callback<List<Songs>>() {
            @Override
            public void onResponse(Call<List<Songs>> call, Response<List<Songs>> response) {
                arrplaylist = (ArrayList<Songs>) response.body();
                if (arrplaylist == null) {
                    arrplaylist = new ArrayList<>(); // Initialize if null
                }
                quanLyBaiHatAdapter = new QuanLyBaiHatAdapter(QuanLyBaiHat.this, arrplaylist);
                recycler_viewmanagelist.setLayoutManager(new LinearLayoutManager(QuanLyBaiHat.this));
                recycler_viewmanagelist.setAdapter(quanLyBaiHatAdapter);
            }

            @Override
            public void onFailure(Call<List<Songs>> call, Throwable t) {
                Toast.makeText(QuanLyBaiHat.this, "Failed to load playlists", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void anhxa() {
        toolbar = findViewById(R.id.toolbar);
        recycler_viewmanagelist = findViewById(R.id.recycler_viewsong);
        searchBar = findViewById(R.id.search_barsong);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    @SuppressLint("ResourceAsColor")
    private void Innit() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            Log.e("Song Playlist", "ActionBar is null");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
        findViewById(R.id.fab_add).setOnClickListener(v -> {
            Intent intent = new Intent(QuanLyBaiHat.this, AddSongActivity.class);
            startActivity(intent);
        });
    }

    public void deleteSong(String idSong) {
        StringRequest request = new StringRequest(Request.Method.POST, "https://appmusic1230.000webhostapp.com/Server/delete_songAdmin.php", response -> {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                String status = jsonResponse.getString("status");
                String message = jsonResponse.getString("message");

                if (status.equalsIgnoreCase("success")) {
                    Toast.makeText(QuanLyBaiHat.this, message, Toast.LENGTH_SHORT).show();
                    GetData(); // Refresh the data after deletion
                } else {
                    Toast.makeText(QuanLyBaiHat.this, message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(QuanLyBaiHat.this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(QuanLyBaiHat.this, "Error deleting song: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_song", idSong);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}
