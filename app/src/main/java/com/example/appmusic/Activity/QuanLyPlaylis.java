package com.example.appmusic.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appmusic.Adapter.QuanLyPlaylistAdapter;
import com.example.appmusic.Model.Playlist;
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

public class QuanLyPlaylis extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recycler_viewmanagelist;
    QuanLyPlaylistAdapter quanLyPlaylistAdapter;
    ArrayList<Playlist> arrplaylist;
    EditText searchBar;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_category);
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

    private void GetData() {
        Dataservice dataservice = APIService.getService();
        Call<List<Playlist>> callback = dataservice.GetDanhsachcaPlaylist();
        callback.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                arrplaylist = (ArrayList<Playlist>) response.body();
                if (arrplaylist == null) {
                    arrplaylist = new ArrayList<>(); // Initialize if null
                }
                quanLyPlaylistAdapter = new QuanLyPlaylistAdapter(QuanLyPlaylis.this, arrplaylist);
                recycler_viewmanagelist.setLayoutManager(new LinearLayoutManager(QuanLyPlaylis.this));
                recycler_viewmanagelist.setAdapter(quanLyPlaylistAdapter);

                for (Playlist playlist : arrplaylist) {
                    Log.d("PlaylistData", "Name: " + playlist.getNamePlaylist() + ", Image URL: " + playlist.getImagePlaylist());
                }
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
                Toast.makeText(QuanLyPlaylis.this, "Failed to load playlists", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filter(String text) {
        ArrayList<Playlist> filteredList = new ArrayList<>();

        for (Playlist playlist : arrplaylist) {
            if (playlist.getNamePlaylist().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(playlist);
            }
        }

        quanLyPlaylistAdapter.updateList(filteredList);
    }

    private void anhxa() {
        toolbar = findViewById(R.id.toolbar);
        searchBar = findViewById(R.id.search_barplaylist);
        recycler_viewmanagelist = findViewById(R.id.recycler_viewmanagelist);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    @SuppressLint("ResourceAsColor")
    private void Innit() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            Log.e("Danhsachcaplaylist", "ActionBar is null");
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuanLyPlaylis.this, AddPlaylistActivity.class);
                startActivity(intent);
            }
        });
    }
        private void setupBottomNavigation() {
        bottomNavigationView.setSelectedItemId(R.id.navigation_category);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if(itemId == R.id.navigation_category){
                    return true;
                } else if (itemId == R.id.navigation_song) {
                    startActivity(new Intent(QuanLyPlaylis.this, QuanLyBaiHat.class));
                    return true;
                } else if (itemId == R.id.navigation_account) {
                    startActivity(new Intent(QuanLyPlaylis.this, QuanLyUser.class));
                    return true;
                } else if (itemId == R.id.navigation_settings) {
                    startActivity(new Intent(QuanLyPlaylis.this, SettingsActivity.class));
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    public void deletePlaylist(String idPlaylist) {
        StringRequest request = new StringRequest(Request.Method.POST, "https://appmusic1230.000webhostapp.com/Server/deleteplaylist.php", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String status = jsonResponse.getString("status");
                    String message = jsonResponse.getString("message");

                    if (status.equalsIgnoreCase("success")) {
                        Toast.makeText(QuanLyPlaylis.this, message, Toast.LENGTH_SHORT).show();
                        GetData(); // Refresh the data after deletion
                    } else {
                        Toast.makeText(QuanLyPlaylis.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(QuanLyPlaylis.this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(QuanLyPlaylis.this, "Error deleting playlist: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_playlist", idPlaylist);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}
