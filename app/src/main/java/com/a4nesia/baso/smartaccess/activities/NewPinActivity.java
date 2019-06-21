package com.a4nesia.baso.smartaccess.activities;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.a4nesia.baso.smartaccess.R;
import com.a4nesia.baso.smartaccess.requests.Request;
import com.a4nesia.baso.smartaccess.service.APIService;
import com.google.android.material.snackbar.Snackbar;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.orhanobut.hawk.Hawk;

public class NewPinActivity extends AppCompatActivity {
    TextView pinWarning;
    public static int MODE_NEW_PIN = 88;
    public static int MODE_CHANGE_PIN = 99;
    int SELECTED_MODE;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pin);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Hawk.init(this).build();
        token = Hawk.get("token","");
        SELECTED_MODE = getIntent().getIntExtra("mode",MODE_NEW_PIN);
        final OtpView otpView;

        otpView = findViewById(R.id.edit_pin);
        pinWarning = findViewById(R.id.pin_warning);
        otpView.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);


        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(final String otp) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(otpView.getWindowToken(), 0);
                final View parentLayout = findViewById(android.R.id.content);


                Call<Request> changePinCall = APIService.service.updatePin(token,otp);
                changePinCall.enqueue(new Callback<Request>() {
                    @Override
                    public void onResponse(Call<Request> call, Response<Request> response) {
                        if(response.isSuccessful()){
                            Request request = response.body();
                            if(request.isSuccess()){
                                Snackbar.make(parentLayout, request.getMessage(), Snackbar.LENGTH_SHORT)
                                        .show();
                                Hawk.put("pin",otp);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        if(SELECTED_MODE == MODE_NEW_PIN)
                                        {
                                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                            finish();
                                        }
                                        else {
                                            onBackPressed();
                                        }
                                    }
                                }, 2000);
                            }
                            else{
                                Snackbar.make(parentLayout, request.getMessage(), Snackbar.LENGTH_SHORT)
                                        .show();
                            }
                        }
                        else {
                            Snackbar.make(parentLayout, "Failed to change pin", Snackbar.LENGTH_SHORT)
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Request> call, Throwable t) {
                        Log.e("ERROR",t.getMessage());
                    }
                });


            }
        });
    }
}
