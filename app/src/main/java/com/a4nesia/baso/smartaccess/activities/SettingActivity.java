package com.a4nesia.baso.smartaccess.activities;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.a4nesia.baso.smartaccess.R;
import com.a4nesia.baso.smartaccess.utils.Utils;
import com.a4nesia.baso.smartaccess.requests.Request;
import com.a4nesia.baso.smartaccess.service.APIService;
import com.amulyakhare.textdrawable.TextDrawable;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.orhanobut.hawk.Hawk;

public class SettingActivity extends AppCompatActivity {
    SwitchMaterial switchCard;
    TextView txtCard;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Hawk.init(this).build();
        token = Hawk.get("token","");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Settings");





        initData();
        
    }

    private void initData() {
        String name = Hawk.get("name");
        String email = Hawk.get("email");
        ((TextView)findViewById(R.id.name_user)).setText(name);
        ((TextView)findViewById(R.id.email_user)).setText(email);
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(Utils.getFirstLetters(name), Color.parseColor("#1565c0"));
        ((ImageView)findViewById(R.id.img_user)).setImageDrawable(drawable);
        ((LinearLayout)(findViewById(R.id.edit_profile))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                startActivity(intent);
            }
        });
        ((LinearLayout)(findViewById(R.id.edit_pin))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PinViewActivity.class);
                intent.putExtra("mode", PinViewActivity.MODE_AUTH_SETTING);
                startActivity(intent);
            }
        });
        ((LinearLayout)findViewById(R.id.edit_password)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.dialog_edit_password,null);
                final TextInputEditText editOldPassword = view.findViewById(R.id.edit_oldpassword);
                final TextInputEditText editNewPassword = view.findViewById(R.id.edit_newpassword);

                final AlertDialog alertDialog = new AlertDialog.Builder(SettingActivity.this)
                        .setTitle("Change Password")
                        .setView(view)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("Save", null).show();
                alertDialog.show();
                Button saveBtn = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button cancelBtn = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                saveBtn.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                saveBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
                cancelBtn.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                cancelBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
                saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String oldPassword = editOldPassword.getText().toString();
                        String newPassword = editNewPassword.getText().toString();
                        Call<Request> changePasswordCall = APIService.service.updatePassword(token,oldPassword,newPassword);
                        changePasswordCall.enqueue(new Callback<Request>() {
                            @Override
                            public void onResponse(Call<Request> call, Response<Request> response) {
                                if(response.isSuccessful()){
                                    Request request = response.body();
                                    Toast.makeText(SettingActivity.this, request.getMessage(), Toast.LENGTH_SHORT).show();
                                    if(request.isSuccess())
                                        alertDialog.dismiss();
                                }
                                else{
                                    Log.e("ERROR",response.raw()+"");
                                }
                            }

                            @Override
                            public void onFailure(Call<Request> call, Throwable t) {
                                Log.e("ERROR",call.toString()+"");
                            }
                        });
                    }
                });
            }
        });
        boolean cardStatus = Hawk.get("card_enabled",1) == 1;
        txtCard = findViewById(R.id.card);

        if(cardStatus)
            txtCard.setText("Enabled");
        else
            txtCard.setText("Disabled");


        switchCard = findViewById(R.id.switch_card);
        switchCard.setChecked(cardStatus);
        final View parentLayout = findViewById(android.R.id.content);
        switchCard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                Call<Request> changeCardStatusCall = APIService.service.updateCardStatus(token,(isChecked) ? 1 : 0);
                changeCardStatusCall.enqueue(new Callback<Request>() {
                    @Override
                    public void onResponse(Call<Request> call, Response<Request> response) {
                        if(response.isSuccessful()){
                            Request request = response.body();
                            if(request.isSuccess()){
                                Snackbar.make(parentLayout, request.getMessage(), Snackbar.LENGTH_SHORT)
                                        .show();
                                Hawk.put("card_enabled",(isChecked) ? 1 : 0);
                                if(isChecked)
                                    txtCard.setText("Enabled");
                                else
                                    txtCard.setText("Disabled");
                            }
                            else{
                                Snackbar.make(parentLayout, request.getMessage(), Snackbar.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Request> call, Throwable t) {

                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        initData();
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
