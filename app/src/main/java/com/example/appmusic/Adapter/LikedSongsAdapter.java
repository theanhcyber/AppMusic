package com.example.appmusic.Adapter;

import android.content.Context;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

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

public class LikedSongsAdapter extends RecyclerView.Adapter<LikedSongsAdapter.ViewHolder> {
    Context context;
    ArrayList<Songs> likedSongsList;
    private int userId;
    private Set<String> likedSongs = new HashSet<>();

    public LikedSongsAdapter(Context context, ArrayList<Songs> likedSongsList) {
        this.context = context;
        this.likedSongsList = likedSongsList;
        loadUserProfile();
        fetchLikedSongs();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_bai_hat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Songs song = likedSongsList.get(position);
        if (song.isLiked()) {
            holder.txtSongName.setText(song.getNameSong());
            holder.txtSinger.setText(song.getSinger());
            String imageUrl = song.getImageSong();

            if (imageUrl != null && !imageUrl.isEmpty()) {
                Picasso.with(context)
                        .load(imageUrl)
                        .placeholder(R.drawable.profile)
                        .error(R.drawable.ic_face_black_24dp)
                        .into(holder.imgSong);
            } else {
                holder.imgSong.setImageResource(R.drawable.background_rounded);
            }

            // Set the heart icon based on the like status
            holder.imgLove.setImageResource(R.drawable.iconloved);

            // Set onClickListener for heart icon
            holder.imgLove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = holder.getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        showUnlikedConfirmationDialog(adapterPosition);
                    }
                }
            });

            // Set onClickListener for item view
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = holder.getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        Songs clickedSong = likedSongsList.get(adapterPosition);
                        Intent intent = new Intent(context, PlaynhacActivity.class);
                        intent.putExtra("cakhuc", clickedSong);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    private void showUnlikedConfirmationDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Bạn có muốn bỏ thích bài hát này?")
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Unset liked status and update list
                        Songs song = likedSongsList.get(position);
                        unlikeSong(song.getIdSong(), position);
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dismiss dialog if canceled
                        dialog.dismiss();
                    }
                })
                .show();
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
                        likedSongsList.remove(position);
                        saveLikedSongs(); // Lưu danh sách likedSongs sau khi thay đổi
                        notifyItemRemoved(position);
                    } else {
                        Toast.makeText(context, "Lỗi!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Lỗi!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("LikedSongsAdapter", "Error unliking song: " + t.getMessage());
                Toast.makeText(context, "Lỗi kết nối!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUserProfile() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);
        if (userId == -1) {
            Log.e("LikedSongsAdapter", "User ID not found in SharedPreferences");
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
                    Log.e("LikedSongsAdapter", "Error fetching liked songs: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Songs>> call, Throwable t) {
                Log.e("LikedSongsAdapter", "Error fetching liked songs: " + t.getMessage());
            }
        });
    }

    private void saveLikedSongs() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("likedSongs", likedSongs);
        editor.apply();
    }

    @Override
    public int getItemCount() {
        return likedSongsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSongName, txtSinger;
        ImageView imgSong, imgLove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSongName = itemView.findViewById(R.id.textviewtenbaihathot);
            txtSinger = itemView.findViewById(R.id.textviewcasibaihathot);
            imgSong = itemView.findViewById(R.id.imageviewbaihathot);
            imgLove = itemView.findViewById(R.id.imageviewluotthichdanhsachbaihat);
        }
    }
}
