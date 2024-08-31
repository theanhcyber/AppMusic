package com.example.appmusic.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmusic.Activity.EditSongActivity;
import com.example.appmusic.Activity.QuanLyBaiHat;
import com.example.appmusic.Model.Songs;
import com.example.appmusic.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class QuanLyBaiHatAdapter extends RecyclerView.Adapter<QuanLyBaiHatAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Songs> arrSong;
    private ArrayList<Songs> arrFilteredSong; // Dùng để lưu danh sách bài hát đã lọc

    public QuanLyBaiHatAdapter(Context context, ArrayList<Songs> arrSong) {
        this.context = context;
        this.arrSong = arrSong;
        this.arrFilteredSong = new ArrayList<>(arrSong); // Khởi tạo danh sách bài hát lọc từ danh sách ban đầu
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Songs song = arrFilteredSong.get(position);
        holder.txtTenBaiHat.setText(song.getNameSong());
        holder.txtTenCaSi.setText("Singer: " + song.getSinger());
        holder.txtCode.setText("Id: " + song.getIdSong());
        holder.feedback.setText("Feedback: " + song.getFeedback());

        Picasso.with(context).load(song.getImageSong()).into(holder.imageNhac);

        // Edit button click listener
        holder.buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (song != null) {
                    Intent intent = new Intent(context, EditSongActivity.class);
                    Log.d("QuanLyBaiHatAdapter", "Selected song: " + song.toString());
                    intent.putExtra("selectedSong", song);
                    context.startActivity(intent);
                } else {
                    Log.d("QuanLyBaiHatAdapter", "Selected song is null");
                }
            }
        });

        // Delete button click listener
        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof QuanLyBaiHat) {
                    ((QuanLyBaiHat) context).deleteSong(song.getIdSong());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrFilteredSong.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageNhac;
        TextView txtTenBaiHat;
        TextView txtTenCaSi;
        TextView txtCode;
        TextView feedback;
        ImageButton buttonEdit;
        ImageButton buttonDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCode = itemView.findViewById(R.id.text_codeSong);
            imageNhac = itemView.findViewById(R.id.manageimage_song);
            txtTenBaiHat = itemView.findViewById(R.id.text_titlesong);
            txtTenCaSi = itemView.findViewById(R.id.text_artistsong);
            feedback = itemView.findViewById(R.id.text_feedbacksong);
            buttonEdit = itemView.findViewById(R.id.button_editsongs);
            buttonDelete = itemView.findViewById(R.id.button_deletesongs);
        }
    }
        public void updateList(ArrayList<Songs> newList) {
            arrFilteredSong.clear();
            arrFilteredSong.addAll(newList);
            notifyDataSetChanged();
    }
}
