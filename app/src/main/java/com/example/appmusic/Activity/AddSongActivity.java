package com.example.appmusic.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appmusic.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddSongActivity extends AppCompatActivity {
    EditText txtsingger, txtImageurl, txtName, txtlink, txtfeedback;
    Button btn_Insert;
    Spinner spinnerPlaylist, spinnerType, spinnerAlbum;
    ArrayList<String> playlistNames, typeNames, albumNames;
    ArrayList<Integer> playlistIds, typeIds, albumIds;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_songs);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        txtImageurl = findViewById(R.id.edit_imagesongs);
        txtName = findViewById(R.id.edit_text_namesongs);
        txtsingger = findViewById(R.id.txt_Singer);
        txtlink = findViewById(R.id.text_songsurl);
        txtfeedback = findViewById(R.id.txt_feedback);
        btn_Insert = findViewById(R.id.button_insert);
        spinnerPlaylist = findViewById(R.id.spinner_playlist);
        spinnerType = findViewById(R.id.spinner_type);
        spinnerAlbum = findViewById(R.id.spinner_album);

        playlistNames = new ArrayList<>();
        typeNames = new ArrayList<>();
        albumNames = new ArrayList<>();
        playlistIds = new ArrayList<>();
        typeIds = new ArrayList<>();
        albumIds = new ArrayList<>();

        fetchPlaylistData();
        fetchTypeData();
        fetchAlbumData();

        btn_Insert.setOnClickListener(v -> insertData());
    }

    private void fetchPlaylistData() {
        String url = "https://appmusic1230.000webhostapp.com/Server/get_playlistsAdmin.php";
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                if (jsonResponse.getString("status").equals("success")) {
                    JSONArray playlists = jsonResponse.getJSONArray("data");
                    for (int i = 0; i < playlists.length(); i++) {
                        JSONObject playlist = playlists.getJSONObject(i);
                        playlistIds.add(playlist.getInt("id_playlist"));
                        playlistNames.add(playlist.getString("name_playlist"));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, playlistNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerPlaylist.setAdapter(adapter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> error.printStackTrace());
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void fetchTypeData() {
        String url = "https://appmusic1230.000webhostapp.com/Server/get_typesAdmin.php";
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                if (jsonResponse.getString("status").equals("success")) {
                    JSONArray types = jsonResponse.getJSONArray("data");
                    for (int i = 0; i < types.length(); i++) {
                        JSONObject type = types.getJSONObject(i);
                        typeIds.add(type.getInt("id_type"));
                        typeNames.add(type.getString("name_type"));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, typeNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerType.setAdapter(adapter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> error.printStackTrace());
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void fetchAlbumData() {
        String url = "https://appmusic1230.000webhostapp.com/Server/get_albumAdmin.php";
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                if (jsonResponse.getString("status").equals("success")) {
                    JSONArray albums = jsonResponse.getJSONArray("data");
                    for (int i = 0; i < albums.length(); i++) {
                        JSONObject album = albums.getJSONObject(i);
                        albumIds.add(album.getInt("id_album"));
                        albumNames.add(album.getString("name_album"));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, albumNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerAlbum.setAdapter(adapter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> error.printStackTrace());
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void insertData() {
        String imageUrl = txtImageurl.getText().toString().trim();
        String name = txtName.getText().toString().trim();
        String iconUrl = txtlink.getText().toString().trim();
        String feedback = txtfeedback.getText().toString().trim();
        String singer = txtsingger.getText().toString().trim();
        int selectedPlaylistId = playlistIds.get(spinnerPlaylist.getSelectedItemPosition());
        int selectedTypeId = typeIds.get(spinnerType.getSelectedItemPosition());
        int selectedAlbumId = albumIds.get(spinnerAlbum.getSelectedItemPosition());
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

        if (imageUrl.isEmpty()) {
            Toast.makeText(this, "Enter Image Url", Toast.LENGTH_SHORT).show();
            return;
        } else if (name.isEmpty()) {
            Toast.makeText(this, "Enter Song name", Toast.LENGTH_SHORT).show();
            return;
        } else if (iconUrl.isEmpty()) {
            Toast.makeText(this, "Enter link", Toast.LENGTH_SHORT).show();
            return;
        } else {
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, "https://appmusic1230.000webhostapp.com/Server/addSongs.php",
                    response -> {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString("status");
                            String message = jsonResponse.getString("message");

                            if (status.equalsIgnoreCase("success")) {
                                Toast.makeText(AddSongActivity.this, "Data Inserted", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), QuanLyBaiHat.class));
                                finish();
                            } else {
                                Toast.makeText(AddSongActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(AddSongActivity.this, "Response parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }, volleyError -> {
                progressDialog.dismiss();
                Toast.makeText(AddSongActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("name_song", name);
                    params.put("image_song", imageUrl);
                    params.put("feedback", feedback);
                    params.put("singer", singer);
                    params.put("link_song", iconUrl);
                    params.put("id_playlist", String.valueOf(selectedPlaylistId));
                    params.put("id_type", String.valueOf(selectedTypeId));
                    params.put("id_album", String.valueOf(selectedAlbumId));

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(AddSongActivity.this);
            requestQueue.add(request);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
