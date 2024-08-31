package com.example.appmusic.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmusic.Adapter.DanhsachtatcachudeAdaper;
import com.example.appmusic.Model.Theme;
import com.example.appmusic.R;
import com.example.appmusic.Service.APIService;
import com.example.appmusic.Service.Dataservice;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DanhsachtatcachudeActivity extends AppCompatActivity {

    RecyclerView recyclerViewtatcacacchude;
    Toolbar toolbartatcacacchude;
    DanhsachtatcachudeAdaper danhsachtatcachudeAdaper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_danhsachtatcachude);
        init();
        GetData();
    }

    private void GetData() {
        Dataservice dataservice = APIService.getService();
        Call<List<Theme>> callback = dataservice.GetAllChuDe();
        callback.enqueue(new Callback<List<Theme>>() {
            @Override
            public void onResponse(Call<List<Theme>> call, Response<List<Theme>> response) {
                ArrayList<Theme> mangchude  = (ArrayList<Theme>) response.body();
                danhsachtatcachudeAdaper = new DanhsachtatcachudeAdaper(DanhsachtatcachudeActivity.this,mangchude);
                recyclerViewtatcacacchude.setLayoutManager(new GridLayoutManager(DanhsachtatcachudeActivity.this,1));
                recyclerViewtatcacacchude.setAdapter(danhsachtatcachudeAdaper);
            }

            @Override
            public void onFailure(Call<List<Theme>> call, Throwable t) {

            }
        });
    }

    private void init() {
        recyclerViewtatcacacchude = findViewById(R.id.recycleviewAllchude);
        toolbartatcacacchude = findViewById(R.id.toolbarallchude);
        setSupportActionBar(toolbartatcacacchude);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tất Cả Chủ Đề");
        toolbartatcacacchude.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}