package com.example.appmusic.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.example.appmusic.Model.Songs;
import com.example.appmusic.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditSongActivity extends AppCompatActivity {
    private EditText txtId, txtName, txtImageurl, txtSinger, txtLink, txtFeedback;
    private Spinner spinnerPlaylist, spinnerAlbum, spinnerType;
    private Button btnUpdate;

    private ArrayList<String> playlistNames = new ArrayList<>();
    private ArrayList<Integer> playlistIds = new ArrayList<>();
    private ArrayList<String> albumNames = new ArrayList<>();
    private ArrayList<Integer> albumIds = new ArrayList<>();
    private ArrayList<String> typeNames = new ArrayList<>();
    private ArrayList<Integer> typeIds = new ArrayList<>();
    private Toolbar toolbar;
    private int selectedPlaylistId, selectedAlbumId, selectedTypeId;

    @SuppressLint({"MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_songs);

        txtId = findViewById(R.id.id_songs);
        txtName = findViewById(R.id.edit_text_namesongs);
        txtImageurl = findViewById(R.id.edit_imagesongs);
        txtSinger = findViewById(R.id.txt_Singer);
        txtLink = findViewById(R.id.text_songsurl);
        txtFeedback = findViewById(R.id.txt_feedback);
        spinnerPlaylist = findViewById(R.id.spinner_playlist);
        spinnerAlbum = findViewById(R.id.spinner_album);
        spinnerType = findViewById(R.id.spinner_type);
        btnUpdate = findViewById(R.id.button_update);

        loadSpinnerData();

        Songs selectedSong = getIntent().getParcelableExtra("selectedSong");
        if (selectedSong != null) {
            Log.d("EditSongActivity", "Selected song: " + selectedSong.toString());

            txtId.setText(selectedSong.getIdSong());
            txtName.setText(selectedSong.getNameSong());
            txtImageurl.setText(selectedSong.getImageSong());
            txtSinger.setText(selectedSong.getSinger());
            txtLink.setText(selectedSong.getLinkSong());
            txtFeedback.setText(selectedSong.getFeedback());

            // Đặt giá trị ban đầu cho Spinner
            spinnerPlaylist.setSelection(getIndex(spinnerPlaylist, selectedSong.getIdPlaylist()));
            spinnerAlbum.setSelection(getIndex(spinnerAlbum, selectedSong.getIdAlbum()));
            spinnerType.setSelection(getIndex(spinnerType, selectedSong.getIdType()));

            txtId.setEnabled(false);
            txtId.setFocusable(false);
            txtId.setCursorVisible(false);

        } else {
            Log.d("EditSongActivity", "Selected song is null");
        }
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        btnUpdate.setOnClickListener(v -> updateSong());

        spinnerPlaylist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPlaylistId = playlistIds.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerAlbum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAlbumId = albumIds.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTypeId = typeIds.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadSpinnerData() {
        loadPlaylists();
        loadAlbums();
        loadTypes();
    }

    private void loadPlaylists() {
        StringRequest request = new StringRequest(Request.Method.GET, "https://appmusic1230.000webhostapp.com/Server/get_playlistsAdmin.php",
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.getString("status").equals("success")) {
                            JSONArray playlists = jsonResponse.getJSONArray("data");
                            for (int i = 0; i < playlists.length(); i++) {
                                JSONObject playlist = playlists.getJSONObject(i);
                                playlistNames.add(playlist.getString("name_playlist"));
                                playlistIds.add(playlist.getInt("id_playlist"));
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, playlistNames);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerPlaylist.setAdapter(adapter);
                        } else {
                            Toast.makeText(this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to parse playlists: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Failed to load playlists: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void loadAlbums() {
        StringRequest request = new StringRequest(Request.Method.GET, "https://appmusic1230.000webhostapp.com/Server/get_albumAdmin.php",
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.getString("status").equals("success")) {
                            JSONArray albums = jsonResponse.getJSONArray("data");
                            for (int i = 0; i < albums.length(); i++) {
                                JSONObject album = albums.getJSONObject(i);
                                albumNames.add(album.getString("name_album"));
                                albumIds.add(album.getInt("id_album"));
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, albumNames);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerAlbum.setAdapter(adapter);
                        } else {
                            Toast.makeText(this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to parse albums: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Failed to load albums: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void loadTypes() {
        StringRequest request = new StringRequest(Request.Method.GET, "https://appmusic1230.000webhostapp.com/Server/get_typesAdmin.php",
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.getString("status").equals("success")) {
                            JSONArray types = jsonResponse.getJSONArray("data");
                            for (int i = 0; i < types.length(); i++) {
                                JSONObject type = types.getJSONObject(i);
                                typeNames.add(type.getString("name_type"));
                                typeIds.add(type.getInt("id_type"));
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, typeNames);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerType.setAdapter(adapter);
                        } else {
                            Toast.makeText(this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to parse types: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Failed to load types: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void updateSong() {
        String idsong = txtId.getText().toString().trim();
        String imageUrl = txtImageurl.getText().toString().trim();
        String name = txtName.getText().toString().trim();
        String linkUrl = txtLink.getText().toString().trim();
        String feedback = txtFeedback.getText().toString().trim();
        String singer = txtSinger.getText().toString().trim();
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating...");

        if (imageUrl.isEmpty()) {
            Toast.makeText(this, "Enter Image Url", Toast.LENGTH_SHORT).show();
            return;
        } else if (name.isEmpty()) {
            Toast.makeText(this, "Enter Song name", Toast.LENGTH_SHORT).show();
            return;
        } else if (linkUrl.isEmpty()) {
            Toast.makeText(this, "Enter link", Toast.LENGTH_SHORT).show();
            return;
        } else {
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, "https://appmusic1230.000webhostapp.com/Server/updateSongsAdmin.php",
                    response -> {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString("status");
                            String message = jsonResponse.getString("message");

                            if (status.equalsIgnoreCase("success")) {
                                Toast.makeText(EditSongActivity.this, "Data Updated", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), QuanLyBaiHat.class));
                                finish();
                            } else {
                                Toast.makeText(EditSongActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(EditSongActivity.this, "Response parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }, volleyError -> {
                progressDialog.dismiss();
                Toast.makeText(EditSongActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("id_song", idsong);
                    params.put("id_playlist", String.valueOf(selectedPlaylistId));
                    params.put("id_album", String.valueOf(selectedAlbumId));
                    params.put("id_type", String.valueOf(selectedTypeId));
                    params.put("name_song", name);
                    params.put("image_song", imageUrl);
                    params.put("feedback", feedback);
                    params.put("singer", singer);
                    params.put("link_song", linkUrl);

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(EditSongActivity.this);
            requestQueue.add(request);
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private int getIndex(Spinner spinner, int id) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().startsWith(String.valueOf(id))) {
                return i;
            }
        }
        return 0;
    }
}
