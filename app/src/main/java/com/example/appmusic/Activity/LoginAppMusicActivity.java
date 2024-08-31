package com.example.appmusic.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appmusic.Model.User;
import com.example.appmusic.R;
import com.example.appmusic.Service.APIService;
import com.example.appmusic.Service.Dataservice;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginAppMusicActivity extends AppCompatActivity {

    EditText email, password;
    Button login;
    ImageView goback;

     TextView register;
    private Dataservice dataservice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_app_music);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        goback = findViewById(R.id.goback);
        register = findViewById(R.id.register_text);
        dataservice = APIService.getService();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailInput = email.getText().toString().trim();
                String passwordInput = password.getText().toString().trim();

                if (validateEmail(emailInput) && validatePassword(passwordInput)) {
                    loginUser(emailInput, passwordInput);
                } else if (emailInput.isEmpty()) {
                    Toast.makeText(LoginAppMusicActivity.this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (!validateEmail(emailInput)) {
                    Toast.makeText(LoginAppMusicActivity.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                } else if (passwordInput.isEmpty()) {
                    Toast.makeText(LoginAppMusicActivity.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginAppMusicActivity.this, "Password must be at least 8 characters with at least 1 uppercase letter, 1 lowercase letter, and 1 digit", Toast.LENGTH_SHORT).show();
                }
            }
        });

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private boolean validatePassword(String password) {
        // Password must be at least 8 characters with at least 1 uppercase letter, 1 lowercase letter, and 1 digit
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
        return password.matches(passwordPattern);
    }
    private boolean validateEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public void openRegisterActivity(View view) {
        Intent intent = new Intent(LoginAppMusicActivity.this, RegisterAppMusicActivity.class);
        startActivity(intent);
    }
    private void loginUser(String email, String password) {
        Call<User> call = dataservice.login(email, password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    int role = user.getRole();
                    int id = user.getIdUser();
                    int active = user.getActive();
                    String name = user.getDisplayname();
                    String avatar = user.getAvatar();

                    // Save session (email and role) to SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("userEmail", email);
                    editor.putInt("userRole", role); // Save role
                    editor.putInt("userId", id); // Save user ID
                    editor.putInt("userActive", active); // Save user active
                    editor.putString("userName", name); // Save user name
                    editor.putString("userAvatar", avatar); // Save user avatar
                    editor.apply();

                    // Navigate based on role
                    if (role == 1) {
                        // Admin role
                        Toast.makeText(LoginAppMusicActivity.this, "Login admin successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginAppMusicActivity.this, QuanLyPlaylis.class);
                        startActivity(intent);
                    } else if(role == 2){
                        if(active == 1){
                            Toast.makeText(LoginAppMusicActivity.this, "Login user successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginAppMusicActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginAppMusicActivity.this, "Your account is not active", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // User role (assuming it's 1 or another role ID)
                    }
                    else {
                        // Handle invalid role
                        Toast.makeText(LoginAppMusicActivity.this, "Email and password incorrectly", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle unsuccessful login
                    Toast.makeText(LoginAppMusicActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Handle failure
                if (t instanceof IOException) {
                    // Network or conversion error
                    Log.e("LoginAppMusic", "Network or conversion error: " + t.getMessage(), t);
                } else {
                    // Unexpected error
                    Log.e("LoginAppMusic", "Unexpected error: " + t.getMessage(), t);
                }
                Toast.makeText(LoginAppMusicActivity.this, "Login failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
