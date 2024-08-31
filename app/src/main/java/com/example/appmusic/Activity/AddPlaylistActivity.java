package com.example.appmusic.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddPlaylistActivity extends AppCompatActivity {
    EditText txtImageurl, txtName, txticonUrl;
    Button btn_Insert;
    Toolbar toolbar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_category);


        txtImageurl = findViewById(R.id.edit_image);
        txtName = findViewById(R.id.edit_text_category);
        txticonUrl = findViewById(R.id.edit_text_icon);
        btn_Insert = findViewById(R.id.button_insert);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        btn_Insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });
    }

    private void insertData() {
        String imageUrl = txtImageurl.getText().toString().trim();
        String name = txtName.getText().toString().trim();
        String iconUrl = txticonUrl.getText().toString().trim();
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");

        if (imageUrl.isEmpty()) {
            Toast.makeText(this, "Enter Image Url", Toast.LENGTH_SHORT).show();
            return;
        } else if (name.isEmpty()) {
            Toast.makeText(this, "Enter name", Toast.LENGTH_SHORT).show();
            return;
        } else if (iconUrl.isEmpty()) {
            Toast.makeText(this, "Enter iconUrl", Toast.LENGTH_SHORT).show();
            return;
        } else {
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, "https://appmusic1230.000webhostapp.com/Server/addplaylist.php",
                    response -> {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString("status");
                            String message = jsonResponse.getString("message");

                            if (status.equalsIgnoreCase("success")) {
                                Toast.makeText(AddPlaylistActivity.this, "Data Inserted", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), QuanLyPlaylis.class));
                                finish();
                            } else {
                                Toast.makeText(AddPlaylistActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(AddPlaylistActivity.this, "Response parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }, volleyError -> {
                progressDialog.dismiss();
                Toast.makeText(AddPlaylistActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("name_playlist", name);
                    params.put("image_playlist", imageUrl);
                    params.put("image_icon", iconUrl);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(AddPlaylistActivity.this);
            requestQueue.add(request);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
