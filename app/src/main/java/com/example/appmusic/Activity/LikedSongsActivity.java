package com.example.appmusic.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmusic.Adapter.LikedSongsAdapter;
import com.example.appmusic.Model.Songs;
import com.example.appmusic.R;
import com.example.appmusic.Service.APIService;
import com.example.appmusic.Service.Dataservice;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LikedSongsActivity extends AppCompatActivity {
    RecyclerView recyclerViewLikedSongs;
    LikedSongsAdapter likedSongsAdapter;
    ArrayList<Songs> likedSongsList;
    int userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_songs);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_custom);

            // Set custom title
            LayoutInflater inflater = LayoutInflater.from(this);
            View customTitleView = inflater.inflate(R.layout.custom_action_bar_title, null);
            TextView titleTextView = customTitleView.findViewById(R.id.action_bar_title);

            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setCustomView(customTitleView);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
        }

        recyclerViewLikedSongs = findViewById(R.id.recyclerViewLikedSongs);
        recyclerViewLikedSongs.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewLikedSongs.setHasFixedSize(true);

        loadUserProfile();
        getLikedSongs();
    }

    private void loadUserProfile() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);
        if (userId == -1) {
            Log.e("LikedSongsActivity", "User ID not found in SharedPreferences");
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getLikedSongs() {
        Dataservice dataservice = APIService.getService();
        Call<List<Songs>> callback = dataservice.getLikedSongs(userId);
        callback.enqueue(new Callback<List<Songs>>() {
            @Override
            public void onResponse(Call<List<Songs>> call, Response<List<Songs>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Songs> allSongs = response.body();
                    likedSongsList = new ArrayList<>(allSongs.stream().filter(Songs::isLiked).collect(Collectors.toList()));

                    for (Songs song : likedSongsList) {
                        Log.d("LikedSongsActivity", "Song: " + song.getNameSong() + " isLiked: " + song.isLiked());
                    }

                    likedSongsAdapter = new LikedSongsAdapter(LikedSongsActivity.this, likedSongsList);
                    recyclerViewLikedSongs.setAdapter(likedSongsAdapter);
                } else {
                    Log.e("LikedSongsActivity", "Response error or body is null");
                }
            }

            @Override
            public void onFailure(Call<List<Songs>> call, Throwable t) {
                Log.e("LikedSongsActivity", "Error fetching liked songs: " + t.getMessage());
            }
        });
    }
}
