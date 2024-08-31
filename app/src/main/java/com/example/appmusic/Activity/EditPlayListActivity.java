package com.example.appmusic.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appmusic.Model.Playlist;
import com.example.appmusic.R;

import java.util.HashMap;
import java.util.Map;

public class EditPlayListActivity extends AppCompatActivity {
    EditText txtCode, txtImageurl, txtName, txticonUrl;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_category);

        txtCode = findViewById(R.id.txt_code);
        txtImageurl = findViewById(R.id.update_image);
        txtName = findViewById(R.id.update_text_category);
        txticonUrl = findViewById(R.id.update_text_icon);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        Intent intent = getIntent();
        Playlist selectedPlaylist = (Playlist) intent.getSerializableExtra("selectedPlaylist");

        if (selectedPlaylist != null) {
            txtCode.setText(selectedPlaylist.getIdPlaylist());
            txtImageurl.setText(selectedPlaylist.getImagePlaylist());
            txtName.setText(selectedPlaylist.getNamePlaylist());
            txticonUrl.setText(selectedPlaylist.getImageIcon());
        }
    }

    public void btn_update(View view) {
        String Image = txtImageurl.getText().toString().trim();
        String Name = txtName.getText().toString().trim();
        String Icon = txticonUrl.getText().toString().trim();
        String Code = txtCode.getText().toString().trim();

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating...");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, "https://appmusic1230.000webhostapp.com/Server/updateplalits.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(EditPlayListActivity.this, response, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), QuanLyPlaylis.class));
                        finish();
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(EditPlayListActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_playlist", Code);
                params.put("name_playlist", Name);
                params.put("image_playlist", Image);
                params.put("image_icon", Icon);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(EditPlayListActivity.this);
        requestQueue.add(request);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
