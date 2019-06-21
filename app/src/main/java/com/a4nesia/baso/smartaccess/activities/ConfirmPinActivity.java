package com.a4nesia.baso.smartaccess.activities;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.a4nesia.baso.smartaccess.R;
import com.a4nesia.baso.smartaccess.requests.Request;
import com.a4nesia.baso.smartaccess.service.APIService;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.google.android.material.snackbar.Snackbar;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.orhanobut.hawk.Hawk;

public class ConfirmPinActivity extends AppCompatActivity {
    TextView pinWarning;
    LottieAnimationView lottieAnimationView;
    boolean backToMain = false;
    public static int MODE_MANUAL = 99;
    int MODE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_confirm_pin);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Hawk.init(getApplicationContext()).build();
        final int accessId = getIntent().getIntExtra("access_id",0);
        MODE = getIntent().getIntExtra("mode",0);
        lottieAnimationView = findViewById(R.id.lottie);

        final OtpView otpView;
        final String token = Hawk.get("token","");

        otpView = findViewById(R.id.edit_pin);
        pinWarning = findViewById(R.id.pin_warning);
        otpView.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);


        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(final String otp) {
                otpView.setEnabled(false);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(otpView.getWindowToken(), 0);

                lottieAnimationView.setAnimation("loading.json");
                lottieAnimationView.setRepeatCount(-1);
                lottieAnimationView.playAnimation();

                Call<Request> confirmCall = APIService.service.confirmAccess(token,otp,accessId);
                confirmCall.enqueue(new Callback<Request>() {
                    @Override
                    public void onResponse(Call<Request> call, Response<Request> response) {
                        if(response.isSuccessful()){
                            Request request = response.body();
                            if(request.isSuccess()){
                                backToMain = true;
                                lottieAnimationView.setAnimation("success.json");
                                lottieAnimationView.playAnimation();
                                pinWarning.setTextColor(Color.GREEN);
                            }
                            else{
                                backToMain = false;
                                lottieAnimationView.setAnimation("failed.json");
                                lottieAnimationView.playAnimation();
                                pinWarning.setTextColor(Color.RED);
                            }
                            pinWarning.setVisibility(View.VISIBLE);
                            pinWarning.setText(request.getMessage());
                        }
                        else {
                            Log.e("ERROR",response.raw()+"");
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

    @Override
    public void onBackPressed() {
        if(backToMain || MODE == MODE_MANUAL){
            super.onBackPressed();
        }
        else{
            finishAffinity();
            finish();
        }
    }
}
