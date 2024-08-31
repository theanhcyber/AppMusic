package com.example.appmusic.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmusic.Adapter.DanhsachbaihatAdapter;
import com.example.appmusic.Model.Album;
import com.example.appmusic.Model.Banner;
import com.example.appmusic.Model.Playlist;
import com.example.appmusic.Model.Songs;
import com.example.appmusic.Model.Type;
import com.example.appmusic.R;
import com.example.appmusic.Service.APIService;
import com.example.appmusic.Service.Dataservice;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DanhsachbaihatActivity extends AppCompatActivity {
    Banner quangcao;
    CoordinatorLayout coordinatorLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    RecyclerView recyclerViewdanhsachbaihat;
    FloatingActionButton floatingActionButton;
    ImageView imgdanhsachcakhuc;
    ArrayList<Songs> mangbaihat;
    DanhsachbaihatAdapter danhsachbaihatAdapter;
    Playlist playlist;
    Type type;
    Album album;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_danhsachbaihat);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        DataIntent();
        anhxa();
        init();
        if(quangcao != null && !quangcao.getNameSong().equals("")){
            setValueInview(quangcao.getNameSong(),quangcao.getImageSong());
            GetDataQuangCao(quangcao.getIdBanner());
        }
        if(playlist != null && !playlist.getNamePlaylist().equals("")){
            setValueInview(playlist.getNamePlaylist(),playlist.getImagePlaylist());
            GetDataPlayList(playlist.getIdPlaylist());
        }
        if(type != null && !type.getNameType().equals("")){
            setValueInview(type.getNameType(),type.getImageType());
            GetDataTheLoai(type.getIdType());
        }
        if(album != null && !album.getNameAlbum().equals("")){
            setValueInview(album.getNameAlbum(),album.getImageAlbum());
            GetDataAlbum(album.getIdAlbum());
        }
    }

    private void GetDataAlbum(String idAlbum) {
        Dataservice dataservice = APIService.getService();
        Call<List<Songs>> callback = dataservice.GetDanhsanhbaihattheoalbum(idAlbum);
        callback.enqueue(new Callback<List<Songs>>() {
            @Override
            public void onResponse(Call<List<Songs>> call, Response<List<Songs>> response) {
                mangbaihat = (ArrayList<Songs>) response.body();
                danhsachbaihatAdapter = new DanhsachbaihatAdapter(DanhsachbaihatActivity.this, mangbaihat);
                recyclerViewdanhsachbaihat.setLayoutManager(new LinearLayoutManager(DanhsachbaihatActivity.this));
                recyclerViewdanhsachbaihat.setAdapter(danhsachbaihatAdapter);
                evenClick();
            }

            @Override
            public void onFailure(Call<List<Songs>> call, Throwable t) {

            }
        });
    }

    private void GetDataTheLoai(String idtheloai){
        Dataservice dataservice = APIService.getService();
        Call<List<Songs>> callback = dataservice.GetDanhsachbaihattheotheloai(idtheloai);
        callback.enqueue(new Callback<List<Songs>>() {
            @Override
            public void onResponse(Call<List<Songs>> call, Response<List<Songs>> response) {
                if (response.body() != null) {
                    Log.d("DanhsachbaihatActivity", "GetDataTheLoai onResponse: received data");
                    mangbaihat = (ArrayList<Songs>) response.body();
                    danhsachbaihatAdapter = new DanhsachbaihatAdapter(DanhsachbaihatActivity.this, mangbaihat);
                    recyclerViewdanhsachbaihat.setLayoutManager(new LinearLayoutManager(DanhsachbaihatActivity.this));
                    recyclerViewdanhsachbaihat.setAdapter(danhsachbaihatAdapter);
                    evenClick();
                } else {
                    Log.d("DanhsachbaihatActivity", "GetDataTheLoai onResponse: response body is null");
                }
            }

            @Override
            public void onFailure(Call<List<Songs>> call, Throwable t) {
                Log.e("DanhsachbaihatActivity", "GetDataTheLoai onFailure: " + t.getMessage());
            }
        });
    }

    private void GetDataPlayList(String idplaylist) {
        Dataservice dataservice = APIService.getService();
        Call<List<Songs>> callback = dataservice.GetDanhsachbaihattheoplaylist(idplaylist);
        callback.enqueue(new Callback<List<Songs>>() {
            @Override
            public void onResponse(Call<List<Songs>> call, Response<List<Songs>> response) {
                if (response.body() != null) {
                    Log.d("DanhsachbaihatActivity", "GetDataPlayList onResponse: received data");
                    mangbaihat = (ArrayList<Songs>) response.body();
                    danhsachbaihatAdapter = new DanhsachbaihatAdapter(DanhsachbaihatActivity.this, mangbaihat);
                    recyclerViewdanhsachbaihat.setLayoutManager(new LinearLayoutManager(DanhsachbaihatActivity.this));
                    recyclerViewdanhsachbaihat.setAdapter(danhsachbaihatAdapter);
                    evenClick();
                } else {
                    Log.d("DanhsachbaihatActivity", "GetDataPlayList onResponse: response body is null");
                }
            }

            @Override
            public void onFailure(Call<List<Songs>> call, Throwable t) {
                Log.e("DanhsachbaihatActivity", "GetDataPlayList onFailure: " + t.getMessage());
            }
        });
    }

    private void GetDataQuangCao(String idquangcao) {
        Dataservice dataservice = APIService.getService();
        Call<List<Songs>> callback = dataservice.GetDanhsachbaihattheoquangcao(idquangcao);
        callback.enqueue(new Callback<List<Songs>>() {
            @Override
            public void onResponse(Call<List<Songs>> call, Response<List<Songs>> response) {
                if (response.body() != null) {
                    Log.d("DanhsachbaihatActivity", "GetDataQuangCao onResponse: received data");
                    mangbaihat = (ArrayList<Songs>) response.body();
                    danhsachbaihatAdapter = new DanhsachbaihatAdapter(DanhsachbaihatActivity.this, mangbaihat);
                    recyclerViewdanhsachbaihat.setLayoutManager(new LinearLayoutManager(DanhsachbaihatActivity.this));
                    recyclerViewdanhsachbaihat.setAdapter(danhsachbaihatAdapter);
                    evenClick();
                } else {
                    Log.d("DanhsachbaihatActivity", "GetDataQuangCao onResponse: response body is null");
                }
            }

            @Override
            public void onFailure(Call<List<Songs>> call, Throwable t) {
                Log.e("DanhsachbaihatActivity", "GetDataQuangCao onFailure: " + t.getMessage());
            }
        });
    }

    private void setValueInview(String ten, String hinh) {
        collapsingToolbarLayout.setTitle(ten);

        try {
            URL url = new URL(hinh);
            Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(),bitmap);
            collapsingToolbarLayout.setBackground(bitmapDrawable);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Picasso.with(this).load(hinh).into(imgdanhsachcakhuc);

    }

    private void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        floatingActionButton.setEnabled(false);
    }

    private void anhxa() {
        coordinatorLayout = findViewById(R.id.coordinatorlayout);
        collapsingToolbarLayout = findViewById(R.id.collapsingtoolbar);
        toolbar = findViewById(R.id.toolbardanhsach);
        recyclerViewdanhsachbaihat = findViewById(R.id.recyclerviewdanhsachbaihat);
        floatingActionButton = findViewById(R.id.floatingactionbutton);
        imgdanhsachcakhuc = findViewById(R.id.imageviewdanhsachcakhuc);
    }

    private void DataIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("banner")) {
                quangcao = (Banner) intent.getSerializableExtra("banner");
                Log.d("DanhsachbaihatActivity", "Received Banner: " + quangcao.getIdBanner());
            }
            if (intent.hasExtra("itemplaylist")) {
                playlist = (Playlist) intent.getSerializableExtra("itemplaylist");
                Log.d("DanhsachbaihatActivity", "Received Playlist: " + playlist.getIdPlaylist());
            }
            if (intent.hasExtra("idtheloai")) {
                type = (Type) intent.getSerializableExtra("idtheloai");
                Log.d("DanhsachbaihatActivity", "Received Type: " + type.getIdType());
            }
            if (intent.hasExtra("album")) {
                album = (Album) intent.getSerializableExtra("album");
            }
        }
    }
    private void evenClick(){
        floatingActionButton.setEnabled(true);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( DanhsachbaihatActivity.this, PlaynhacActivity.class);
                intent.putExtra("cacbaihat",mangbaihat);
                startActivity(intent);
            }
        });
    }
}
