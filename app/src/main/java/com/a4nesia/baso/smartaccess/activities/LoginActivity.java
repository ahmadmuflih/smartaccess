package com.a4nesia.baso.smartaccess.activities;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.a4nesia.baso.smartaccess.R;
import com.a4nesia.baso.smartaccess.models.User;
import com.a4nesia.baso.smartaccess.requests.LoginRequest;
import com.a4nesia.baso.smartaccess.service.APIService;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.orhanobut.hawk.Hawk;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText editUsername, editPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        editUsername = findViewById(R.id.edit_username);
        editPassword = findViewById(R.id.edit_password);

    }

    public void login(View view) {
        String username, password;
        boolean verified = true;
        username = editUsername.getText().toString().trim();
        password = editPassword.getText().toString();

        if(("").equals(username))
        {
            editUsername.setError("Username should not be empty!");
            verified=false;
        }
        if(("").equals(password))
        {
            editUsername.setError("Username should not be empty!");
            verified=false;
        }

        if(verified){
            final View parentLayout = findViewById(android.R.id.content);

            final Call<LoginRequest> loginRequestCall = APIService.service.login(username,password);
            loginRequestCall.enqueue(new Callback<LoginRequest>() {
                @Override
                public void onResponse(Call<LoginRequest> call, Response<LoginRequest> response) {
                    if(response.isSuccessful()){
                        LoginRequest login = response.body();
                        if(login.isSuccess()) {
                            User user = login.getUser();
                            String pin = user.getEmployee().getPin();
                            Hawk.put("token", "Bearer" + user.getJwtToken());
                            Hawk.put("name", user.getName());
                            Hawk.put("email", user.getEmail());
                            Hawk.put("email", user.getUsername());
                            Hawk.put("card_enabled", user.getEmployee().getCardEnabled());
                            Hawk.put("card_type", user.getEmployee().getCardType());
                            Hawk.put("pin", pin);
                            Intent i;
                            if ("".equals(pin)) {
                                i = new Intent(getApplicationContext(), NewPinActivity.class);
                                i.putExtra("mode", NewPinActivity.MODE_NEW_PIN);
                            }
                            else {
                                i = new Intent(getApplicationContext(), MainActivity.class);
                            }
                            startActivity(i);
                            finish();
                        }
                        else {
                            Snackbar.make(parentLayout, login.getMessage(), Snackbar.LENGTH_SHORT)
                                    .show();
                        }
                    }
                    else{
                        Snackbar.make(parentLayout, "Username & Password not found", Snackbar.LENGTH_SHORT)
                                .show();
                    }
                }

                @Override
                public void onFailure(Call<LoginRequest> call, Throwable t) {
                    Log.e("ERROR","ERROR"+t.getMessage());
                    Snackbar.make(parentLayout, "ERROR"+t.getMessage(), Snackbar.LENGTH_SHORT)
                            .show();
                }
            });
        }
    }
}
