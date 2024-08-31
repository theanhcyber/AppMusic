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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaihathotAdapter extends RecyclerView.Adapter<BaihathotAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Songs> baihatArrayList;
    private int userId;
    private Set<String> likedSongs = new HashSet<>();

    public BaihathotAdapter(Context context, ArrayList<Songs> baihatArrayList) {
        this.context = context;
        this.baihatArrayList = baihatArrayList;
        loadUserProfile();
        fetchLikedSongs();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_bai_hat_hot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Songs baihat = baihatArrayList.get(position);
        holder.txtcasi.setText(baihat.getSinger());
        holder.txtten.setText(baihat.getNameSong());
        Picasso.with(context).load(baihat.getImageSong()).into(holder.imghinh);

        String songId = baihat.getIdSong();
        boolean isLiked = likedSongs.contains(songId);
        holder.imgluotthich.setImageResource(isLiked ? R.drawable.iconloved : R.drawable.iconlove);

        // Log thông tin để kiểm tra
        Log.d("BaihathotAdapter", "Song ID: " + songId + " | Is Liked: " + isLiked);

        // Xử lý sự kiện khi click vào nút like/unlike
        holder.imgluotthich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userId == -1) {
                    // Nếu userId không tồn tại trong SharedPreferences, chuyển hướng đến màn hình đăng nhập
                    Log.d("BaihathotAdapter", "User not logged in. Redirecting to login.");
                    Intent intent = new Intent(context, LoginAppMusicActivity.class);
                    context.startActivity(intent);
                    return;
                }

                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Songs song = baihatArrayList.get(position);
                    if (likedSongs.contains(song.getIdSong())) {
                        unlikeSong(song.getIdSong(), position);
                    } else {
                        likeSong(song.getIdSong(), position);
                    }
                }
            }
        });

        // Xử lý sự kiện khi click vào item bài hát
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(context, PlaynhacActivity.class);
                    intent.putExtra("cakhuc", baihatArrayList.get(position));
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return baihatArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtten, txtcasi;
        ImageView imghinh, imgluotthich;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtten = itemView.findViewById(R.id.textviewtenbaihathot);
            txtcasi = itemView.findViewById(R.id.textviewcasibaihathot);
            imghinh = itemView.findViewById(R.id.imageviewbaihathot);
            imgluotthich = itemView.findViewById(R.id.imageviewluotthich);
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
                        saveLikedSongs(); // Lưu danh sách likedSongs sau khi thay đổi
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
                Log.e("BaihathotAdapter", "Error liking song: " + t.getMessage());
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
                        saveLikedSongs(); // Lưu danh sách likedSongs sau khi thay đổi
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
                Log.e("BaihathotAdapter", "Error unliking song: " + t.getMessage());
                Toast.makeText(context, "Lỗi kết nối!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUserProfile() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);
        if (userId == -1) {
            Log.e("BaihathotAdapter", "User ID not found in SharedPreferences");
        }
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
                    saveLikedSongs(); // Lưu danh sách likedSongs sau khi thay đổi
                    notifyDataSetChanged(); // Cập nhật RecyclerView
                } else {
                    Log.e("BaihathotAdapter", "Error fetching liked songs: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Songs>> call, Throwable t) {
                Log.e("BaihathotAdapter", "Error fetching liked songs: " + t.getMessage());
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
