package com.a4nesia.baso.smartaccess.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.a4nesia.baso.smartaccess.R;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.orhanobut.hawk.Hawk;

public class PinViewActivity extends AppCompatActivity {
    TextView pinWarning;
    int countFalse=3;
    public static int MODE_AUTH_LAUNCH = 97;
    public static int MODE_AUTH_SETTING = 98;
    private int MODE_SELECTED;
    private String pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_view);
        setContentView(R.layout.activity_pin_view);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        final OtpView otpView;

        Hawk.init(this).build();
        pin = Hawk.get("pin","");

        MODE_SELECTED = getIntent().getIntExtra("mode",MODE_AUTH_LAUNCH);

        otpView = findViewById(R.id.edit_pin);
        pinWarning = findViewById(R.id.pin_warning);
        otpView.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        otpView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==1) {
                    pinWarning.setVisibility(View.INVISIBLE);
                    otpView.setItemBackgroundColor(Color.parseColor("#111565c0"));
                }
            }
        });
        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(otpView.getWindowToken(), 0);
                if(otp.equals(pin)){
                    Intent i;
                    if(MODE_SELECTED == MODE_AUTH_SETTING){
                        i = new Intent(getApplicationContext(),NewPinActivity.class);
                        i.putExtra("mode",NewPinActivity.MODE_CHANGE_PIN);

                    }
                    else {
                        i  = new Intent(getApplicationContext(),MainActivity.class);
                    }
                    startActivity(i);
                    finish();
                }else{
                    countFalse--;
                    if(countFalse==0){
                        onBackPressed();
                    }
                    pinWarning.setVisibility(View.VISIBLE);
                    pinWarning.setText("Your pin is wrong");
                    pinWarning.setTextColor(Color.RED);
                    otpView.setItemBackgroundColor(Color.parseColor("#11b71c1c"));
                    otpView.setText("");
                }

            }
        });
    }
}
