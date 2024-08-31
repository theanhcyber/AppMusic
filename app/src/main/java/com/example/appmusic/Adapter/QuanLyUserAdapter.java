package com.example.appmusic.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appmusic.Model.User;
import com.example.appmusic.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuanLyUserAdapter extends RecyclerView.Adapter<QuanLyUserAdapter.ViewHolder> {

    private Context context;
    private ArrayList<User> arrUser;
    private ArrayList<User> filteredList;
    private ProgressDialog progressDialog;
    private static final String TAG = "QuanLyUserAdapter";

    public QuanLyUserAdapter(Context context, ArrayList<User> arrUser) {
        this.context = context;
        this.arrUser = arrUser;
        this.filteredList = new ArrayList<>(arrUser); // Khởi tạo danh sách filteredList với arrUser
        this.progressDialog = new ProgressDialog(context);
        this.progressDialog.setMessage("Loading...");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_artist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = filteredList.get(position); // Sử dụng filteredList
        Picasso.with(context).load(user.getAvatar()).into(holder.avatar);
        holder.txttenuser.setText("Name: " + user.getDisplayname());
        holder.txtactive.setText(user.getActive() == 1 ? "Active" : "Inactive");
        holder.buttonEdit.setOnClickListener(v -> {
            int newActiveStatus = user.getActive() == 1 ? 0 : 1;
            updateActiveStatus(user.getIdUser(), newActiveStatus, holder.txtactive, user);
        });
    }

    private void updateActiveStatus(int idUser, int newActiveStatus, TextView txtactive, User user) {
        progressDialog.show();
        String url = "https://appmusic1230.000webhostapp.com/Server/updateuserbyAdmin.php";

        Log.d("QuanLyUserAdapter", "Params: {active=" + newActiveStatus + ", id_user=" + idUser + "}");

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            progressDialog.dismiss();
            Log.d("QuanLyUserAdapter", "Server Response: " + response);
            try {
                JSONObject jsonResponse = new JSONObject(response);
                String status = jsonResponse.getString("status");
                if (status.equals("success")) {
                    user.setActive(newActiveStatus);
                    txtactive.setText(newActiveStatus == 1 ? "Active" : "Inactive");
                    Toast.makeText(context, "Status updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    String message = jsonResponse.getString("message");
                    Toast.makeText(context, "Failed to update status: " + message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Toast.makeText(context, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            progressDialog.dismiss();
            Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", String.valueOf(idUser));
                params.put("active", String.valueOf(newActiveStatus));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    @Override
    public int getItemCount() {
        return filteredList.size(); // Sử dụng filteredList
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView txttenuser;
        TextView txtactive;
        ImageButton buttonEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.imageviewavatar);
            txttenuser = itemView.findViewById(R.id.txtviewtenuser);
            txtactive = itemView.findViewById(R.id.txtviewactive);
            buttonEdit = itemView.findViewById(R.id.button_edit);
        }
    }

    // Phương thức cập nhật danh sách người dùng
    public void updateList(ArrayList<User> newList) {
        filteredList.clear();
        filteredList.addAll(newList);
        notifyDataSetChanged();
    }
}
