package com.a4nesia.baso.smartaccess.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.a4nesia.baso.smartaccess.R;
import com.orhanobut.hawk.Hawk;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Hawk.init(this).build();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if(Hawk.get("token","").equals("")){
                    intent = new Intent(getApplicationContext(),LoginActivity.class);
                }
                else{
                    intent = new Intent(SplashActivity.this, PinViewActivity.class);
                    intent.putExtra("mode",PinViewActivity.MODE_AUTH_LAUNCH);
                }
                startActivity(intent);
                finish();
            }
        }, 1000);
    }
}
