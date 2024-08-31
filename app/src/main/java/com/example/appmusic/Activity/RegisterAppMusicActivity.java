package com.example.appmusic.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appmusic.Model.User;
import com.example.appmusic.R;
import com.example.appmusic.Service.APIService;
import com.example.appmusic.Service.Dataservice;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterAppMusicActivity extends AppCompatActivity {

    EditText nameEditText, emailEditText, phoneEditText, passwordEditText, confirmPasswordEditText, avatarUrlEditText;
    Button registerButton;
    ImageView avatarImageView,gobackImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_app_music);

        // Bind views
        nameEditText = findViewById(R.id.name);
        emailEditText = findViewById(R.id.email);
        phoneEditText = findViewById(R.id.phone);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirm_password);
        avatarUrlEditText = findViewById(R.id.avatar_url);
        registerButton = findViewById(R.id.register);
        avatarImageView = findViewById(R.id.avatarImage);
        gobackImageView = findViewById(R.id.goback);

        // Set onClick listeners
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRegisterButtonClick();
            }
        });

        avatarUrlEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    loadAvatarImage(avatarUrlEditText.getText().toString().trim());
                }
            }
        });
        gobackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void handleRegisterButtonClick() {
        // Get input values
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        String avatarUrl = avatarUrlEditText.getText().toString().trim();

        // Validate inputs
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || avatarUrl.isEmpty()) {
            Toast.makeText(RegisterAppMusicActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!validateEmail(email)) {
            Toast.makeText(RegisterAppMusicActivity.this, "Invalid email format", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!validatePhone(phone)) {
            Toast.makeText(RegisterAppMusicActivity.this, "Invalid phone format, phone start with 0 and must be 10 digits", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!validatePassword(password)) {
            Toast.makeText(RegisterAppMusicActivity.this, "Password must be at least 8 characters with at least 1 uppercase letter, 1 lowercase letter, and 1 digit", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(RegisterAppMusicActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check email existence
        checkEmailExistence(email);
    }
    private boolean validatePassword(String password) {
        // Password must be at least 8 characters with at least 1 uppercase letter, 1 lowercase letter, and 1 digit
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
        return password.matches(passwordPattern);
    }
    private boolean validateEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validatePhone(String phone) {
        // Simple validation for a 10-digit phone number starting with '0'
        return Patterns.PHONE.matcher(phone).matches() && phone.length() == 10 && phone.startsWith("0");
    }

    private void loadAvatarImage(String url) {
        if (!url.isEmpty()) {
            Picasso.with(this).load(url).into(avatarImageView);
        } else {
            avatarImageView.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    private void checkEmailExistence(String email) {
        Dataservice dataservice = APIService.getService();
        Call<ResponseBody> call = dataservice.checkEmailExistence(email);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String jsonResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(jsonResponse);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if ("error".equals(status)) {
                            Toast.makeText(RegisterAppMusicActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else if ("success".equals(status)) {
                            performRegistration(nameEditText.getText().toString().trim(),
                                    emailEditText.getText().toString().trim(),
                                    phoneEditText.getText().toString().trim(),
                                    passwordEditText.getText().toString(),
                                    avatarUrlEditText.getText().toString().trim());
                            Toast.makeText(RegisterAppMusicActivity.this, "Register Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterAppMusicActivity.this, LoginAppMusicActivity.class));
                        } else {
                            Toast.makeText(RegisterAppMusicActivity.this, "Unexpected response", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(RegisterAppMusicActivity.this, "Failed to parse response", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(RegisterAppMusicActivity.this, "Failed to check email existence", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("RegisterAppMusic", "Network error, please try again", t);
                Toast.makeText(RegisterAppMusicActivity.this, "Network error, please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void performRegistration(String name, String email, String phone, String password, String avatarUrl) {
        Dataservice dataservice = APIService.getService();
        Call<User> call = dataservice.register(name, email, phone, password, avatarUrl);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User registeredUser = response.body();
                    if (registeredUser != null) {
                        Toast.makeText(RegisterAppMusicActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterAppMusicActivity.this, LoginAppMusicActivity.class));
                    } else {
                        Toast.makeText(RegisterAppMusicActivity.this, "Failed to register, please try again", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterAppMusicActivity.this, "Failed to register, please try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("RegisterAppMusic", "Failed to perform registration", t);
                Toast.makeText(RegisterAppMusicActivity.this, "Network error, please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

