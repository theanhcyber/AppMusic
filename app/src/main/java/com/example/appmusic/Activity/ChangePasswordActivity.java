package com.example.appmusic.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.appmusic.R;
import com.example.appmusic.Service.APIService;
import com.example.appmusic.Service.Dataservice;
import com.squareup.picasso.Picasso;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText editTextNewPassword, editTextConfirmPassword;
    private Button buttonChangePassword;

    private ImageView imageViewAvatar;
    private TextView textViewName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_custom);
            getSupportActionBar().setTitle("Change Password");
        }

        // Initialize views
        initView();
        loadUserData();

        buttonChangePassword.setOnClickListener(v -> {
            String newPassword = editTextNewPassword.getText().toString().trim();
            String confirmPassword = editTextConfirmPassword.getText().toString().trim();

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!validatePassword(newPassword)) {
                Toast.makeText(this, "Password must be at least 8 characters with at least 1 uppercase letter, 1 lowercase letter, and 1 digit", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get user ID from SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
            int userId = sharedPreferences.getInt("userId", -1);

            if (userId != -1) {
                changeUserPassword(userId, newPassword);
            } else {
                Log.e("ChangePasswordActivity", "User ID not found in SharedPreferences");
                Toast.makeText(this, "User ID not found, please login again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validatePassword(String password) {
        // Password must be at least 8 characters with at least 1 uppercase letter, 1 lowercase letter, and 1 digit
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
        return password.matches(passwordPattern);
    }
    private void initView() {
        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonChangePassword = findViewById(R.id.buttonChangePassword);
        imageViewAvatar = findViewById(R.id.imageViewAvatar);
        textViewName = findViewById(R.id.textViewName);
    }
    private void loadUserData() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String displayName = sharedPreferences.getString("userName", "Display Name");
        String avatarUrl = sharedPreferences.getString("userAvatar", ""); // Replace with actual key for avatar URL

        // Set display name
        textViewName.setText(displayName);

        // Load avatar image using Picasso
        if (!avatarUrl.isEmpty()) {
            Picasso.with(this).load(avatarUrl).placeholder(R.drawable.profile).into(imageViewAvatar);
        } else {
            imageViewAvatar.setImageResource(R.drawable.profile); // Default avatar if URL is empty
        }
    }
    private void changeUserPassword(int userId, String newPassword) {
        Dataservice dataservice = APIService.getService();
        Call<ResponseBody> call = dataservice.changePassword(userId, newPassword);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ChangePasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Failed to change password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("API_ERROR", "Failed to change password", t);
                Toast.makeText(ChangePasswordActivity.this, "Failed to change password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
