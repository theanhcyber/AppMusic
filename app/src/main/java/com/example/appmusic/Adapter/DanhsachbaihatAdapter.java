package com.example.appmusic.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmusic.Activity.LoginAppMusicActivity;
import com.example.appmusic.Activity.PlaynhacActivity;
import com.example.appmusic.Model.Songs;
import com.example.appmusic.R;
import com.example.appmusic.Service.APIService;
import com.example.appmusic.Service.Dataservice;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DanhsachbaihatAdapter extends RecyclerView.Adapter<DanhsachbaihatAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Songs> mangbaihat;
    private int userId;
    private Set<String> likedSongs = new HashSet<>();

    public DanhsachbaihatAdapter(Context context, ArrayList<Songs> mangbaihat) {
        this.context = context;
        this.mangbaihat = mangbaihat;
        loadUserProfile();
        fetchLikedSongs();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_danh_sach_bai_hat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Songs songs = mangbaihat.get(position);
        holder.txtcasi.setText(songs.getSinger());
        holder.txttenbaihat.setText(songs.getNameSong());
        holder.txtindex.setText(String.valueOf(position + 1));

        boolean isLiked = likedSongs.contains(songs.getIdSong());
        holder.imgluotthich.setImageResource(isLiked ? R.drawable.iconloved : R.drawable.iconlove);

        holder.imgluotthich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userId == -1) {
                    // Nếu userId không tồn tại trong SharedPreferences, chuyển hướng đến màn hình đăng nhập
                    Log.d("DanhsachbaihatAdapter", "User not logged in. Redirecting to login.");
                    Intent intent = new Intent(context, LoginAppMusicActivity.class);
                    context.startActivity(intent);
                    return;
                }

                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    Songs song = mangbaihat.get(adapterPosition);
                    if (likedSongs.contains(song.getIdSong())) {
                        unlikeSong(song.getIdSong(), adapterPosition);
                    } else {
                        likeSong(song.getIdSong(), adapterPosition);
                    }
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(context, PlaynhacActivity.class);
                    intent.putExtra("cakhuc", mangbaihat.get(adapterPosition));
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mangbaihat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtindex, txttenbaihat, txtcasi;
        ImageView imgluotthich;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtcasi = itemView.findViewById(R.id.textviewtencasi);
            txtindex = itemView.findViewById(R.id.textviewdanhsachindex);
            txttenbaihat = itemView.findViewById(R.id.textviewtenbaihat);
            imgluotthich = itemView.findViewById(R.id.imageviewluotthichdanhsachbaihat);
        }
    }

    private void likeSong(String idSong, int position) {
        Dataservice dataservice = APIService.getService();
        Call<String> callback = dataservice.updateLuotThich("1", idSong, userId);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String result = response.body();
                    if (result.equals("Success")) {
                        Toast.makeText(context, "Đã Thích", Toast.LENGTH_SHORT).show();
                        likedSongs.add(idSong);
                        saveLikedSongs();
                        notifyItemChanged(position);
                    } else {
                        Toast.makeText(context, "Lỗi!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Lỗi!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("DanhsachbaihatAdapter", "Error liking song: " + t.getMessage());
                Toast.makeText(context, "Lỗi kết nối!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void unlikeSong(String idSong, int position) {
        Dataservice dataservice = APIService.getService();
        Call<String> callback = dataservice.updateLuotThich("-1", idSong, userId);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String result = response.body();
                    if (result.equals("Unliked")) {
                        Toast.makeText(context, "Đã Bỏ Thích", Toast.LENGTH_SHORT).show();
                        likedSongs.remove(idSong);
                        saveLikedSongs();
                        notifyItemChanged(position);
                    } else {
                        Toast.makeText(context, "Lỗi!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Lỗi!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("DanhsachbaihatAdapter", "Error unliking song: " + t.getMessage());
                Toast.makeText(context, "Lỗi kết nối!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUserProfile() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);
        Log.d("DanhsachbaihatAdapter", "Loaded user ID: " + userId);
    }

    private void fetchLikedSongs() {
        Dataservice dataservice = APIService.getService();
        Call<List<Songs>> call = dataservice.getLikedSongs(userId);
        call.enqueue(new Callback<List<Songs>>() {
            @Override
            public void onResponse(Call<List<Songs>> call, Response<List<Songs>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Set<String> loadedLikedSongs = new HashSet<>();
                    for (Songs song : response.body()) {
                        if (song.isLiked()) {
                            loadedLikedSongs.add(song.getIdSong());
                        }
                    }
                    likedSongs = loadedLikedSongs;
                    saveLikedSongs();
                    notifyDataSetChanged();
                } else {
                    Log.e("DanhsachbaihatAdapter", "Error fetching liked songs: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Songs>> call, Throwable t) {
                Log.e("DanhsachbaihatAdapter", "Error fetching liked songs: " + t.getMessage());
            }
        });
    }

    private void saveLikedSongs() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("likedSongs", likedSongs);
        editor.apply();
    }
}
