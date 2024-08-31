package com.example.appmusic.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmusic.Activity.EditPlayListActivity;
import com.example.appmusic.Activity.QuanLyPlaylis;
import com.example.appmusic.Model.Playlist;
import com.example.appmusic.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class QuanLyPlaylistAdapter extends RecyclerView.Adapter<QuanLyPlaylistAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Playlist> arrPlaylist;

    private ArrayList<Playlist> arrFilteredSong;

    public QuanLyPlaylistAdapter(@NonNull Context context, ArrayList<Playlist> arrPlaylist) {
        this.context = context;
        this.arrPlaylist = arrPlaylist;
        this.arrFilteredSong = new ArrayList<>(arrPlaylist);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Playlist playlist = arrFilteredSong.get(position);
        Picasso.with(context).load(playlist.getImagePlaylist()).into(holder.imagehinhnen);
        holder.txttemplaylist.setText(playlist.getNamePlaylist());
        holder.txtCode.setText("ID: " +playlist.getIdPlaylist());

        // Edit button click listener
        holder.buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditPlayListActivity.class);
                intent.putExtra("selectedPlaylist", playlist);
                context.startActivity(intent);
            }
        });

        // Delete button click listener
        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call deletePlaylist method of QuanLyPlaylis activity
                ((QuanLyPlaylis) context).deletePlaylist(playlist.getIdPlaylist());
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrFilteredSong.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imagehinhnen;
        TextView txttemplaylist;
        TextView txtCode;
        ImageButton buttonEdit;
        ImageButton buttonDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagehinhnen = itemView.findViewById(R.id.image_category);
            txttemplaylist = itemView.findViewById(R.id.text_category);
            txtCode = itemView.findViewById(R.id.text_code);
            buttonEdit = itemView.findViewById(R.id.button_edit);
            buttonDelete = itemView.findViewById(R.id.button_delete);
        }
    } public void updateList(ArrayList<Playlist> newList) {
        arrFilteredSong.clear();
        arrFilteredSong.addAll(newList);
        notifyDataSetChanged();
    }
}
