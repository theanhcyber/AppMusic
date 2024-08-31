package com.example.appmusic.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.appmusic.Model.User;
import com.example.appmusic.R;
import com.example.appmusic.Service.APIService;
import com.example.appmusic.Service.Dataservice;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editTextDisplayName, editTextEmail, editTextPhone, editTextAvatar;
    private Button buttonSave;
    private User currentUser; // To store current user information

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_custom);
            getSupportActionBar().setTitle("Edit Profile");
        }

        // Initialize views
        initView();

        // Load user profile from SharedPreferences
        loadUserProfile();

        buttonSave.setOnClickListener(v -> {
            if (currentUser != null) {
                // Get updated values from EditText fields
                String displayName = editTextDisplayName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String phone = editTextPhone.getText().toString().trim();
                String avatar = editTextAvatar.getText().toString().trim();
                if (!validatePhone(phone)) {
                    Toast.makeText(EditProfileActivity.this, "Invalid phone format, phone start with 0 and must be 10 digits", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Update user profile with current user ID and updated fields
                updateUserProfile(currentUser.getIdUser(), displayName, email, phone, avatar);
            }
        });
    }
    private boolean validatePhone(String phone) {
        // Simple validation for a 10-digit phone number starting with '0'
        return Patterns.PHONE.matcher(phone).matches() && phone.length() == 10 && phone.startsWith("0");
    }
    private void loadUserProfile() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1); // -1 is the default value if userId is not found
        int role = sharedPreferences.getInt("role", -1); // -1 is the default value if role is not found
        // Assuming userId is retrieved successfully from SharedPreferences
        if (userId != -1) {
            getUserInfo(userId);
        } else {
            // Handle case where userId is not found
            Log.e("EditProfileActivity", "User ID not found in SharedPreferences");
            Toast.makeText(this, "User ID not found, please login again", Toast.LENGTH_SHORT).show();
            // Optionally, redirect to login screen or handle the absence of user ID
        }
    }

    private void initView() {
        editTextDisplayName = findViewById(R.id.editTextDisplayName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextAvatar = findViewById(R.id.editTextAvatar);
        buttonSave = findViewById(R.id.buttonSave);
    }

    private void getUserInfo(int userId) {
        Dataservice dataservice = APIService.getService();
        Call<User> call = dataservice.getUserInfo(userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentUser = response.body();
                    // Update EditText fields with current user information
                    updateUIWithUserData(currentUser);
                } else {
                    Toast.makeText(EditProfileActivity.this, "Failed to fetch user info", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("API_ERROR", "Failed to fetch user info", t);
                Toast.makeText(EditProfileActivity.this, "Failed to fetch user info", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUIWithUserData(User user) {
        editTextDisplayName.setText(user.getDisplayname());
        editTextEmail.setText(user.getMail());
        editTextPhone.setText(user.getPhone());
        editTextAvatar.setText(user.getAvatar()); // Update with avatar URL or other relevant field
    }

    private void updateUserProfile(int userId, String displayName, String email, String phone, String avatar) {
        Dataservice dataservice = APIService.getService();
        Call<User> call = dataservice.updateUserInfo(userId, displayName, email, phone, avatar);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User updatedUser = response.body();
                    Intent intent = new Intent();
                    intent.putExtra("UPDATED_USER", updatedUser);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    // Handle error
                    String errorMessage = "Failed to update profile";
                    if (response.errorBody() != null) {
                        try {
                            errorMessage = response.errorBody().string();
                        } catch (Exception e) {
                            Log.e("API_ERROR", "Failed to parse error response", e);
                        }
                    }
                    Toast.makeText(EditProfileActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("API_ERROR", "Failed to update profile", t);
                Toast.makeText(EditProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
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