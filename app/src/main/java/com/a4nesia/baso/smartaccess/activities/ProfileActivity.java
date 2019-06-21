package com.a4nesia.baso.smartaccess.activities;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.a4nesia.baso.smartaccess.R;
import com.a4nesia.baso.smartaccess.models.User;
import com.a4nesia.baso.smartaccess.requests.Request;
import com.a4nesia.baso.smartaccess.service.APIService;
import com.a4nesia.baso.smartaccess.utils.Utils;
import com.amulyakhare.textdrawable.TextDrawable;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.orhanobut.hawk.Hawk;

public class ProfileActivity extends AppCompatActivity {
    TextInputEditText editName, editEmail,editUsername;
    ImageView imgFoto;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Hawk.init(this).build();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Edit Profile");
        initData();
    }
    private void initData() {
        String name = Hawk.get("name","").trim();
        String email = Hawk.get("email","").trim();
        String username = Hawk.get("username","").trim();
        editName = findViewById(R.id.edit_name);
        editEmail = findViewById(R.id.edit_email);
        editUsername = findViewById(R.id.edit_username);
        imgFoto = findViewById(R.id.img_user);
        editName.setText(name);
        editEmail.setText(email);
        editUsername.setText(username);
        token = Hawk.get("token","");

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(Utils.getFirstLetters(name), Color.parseColor("#1565c0"));
        imgFoto.setImageDrawable(drawable);

        editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().trim().equals("")) {
                    TextDrawable drawable = TextDrawable.builder()
                            .buildRound(Utils.getFirstLetters(s.toString().trim()), Color.parseColor("#1565c0"));
                    imgFoto.setImageDrawable(drawable);
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(ProfileActivity.this)
                        .setMessage("Go back will not save changes, continue?")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onBackPressed();
                            }
                        });
                alertDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void save(View view) {
        final String name = editName.getText().toString().trim();
        final String email = editEmail.getText().toString().trim();
        final String username = editUsername.getText().toString().trim();
        boolean verified = true;
        if("".equals(name)){
            editName.setError("Name should not be empty!");
            verified = true;
        }
        if("".equals(email)){
            editEmail.setError("Email should not be empty!");
            verified = true;
        }
        if("".equals(username)){
            editEmail.setError("Username should not be empty!");
            verified = true;
        }
        if(verified){
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setUsername(username);
            Call<Request> updateProfilCall = APIService.service.updateProfile(token,user);
            updateProfilCall.enqueue(new Callback<Request>() {
                @Override
                public void onResponse(Call<Request> call, Response<Request> response) {
                    if(response.isSuccessful()){
                        Request request = response.body();
                        if(request.isSuccess()){
                            Hawk.put("name",name);
                            Hawk.put("email",email);
                            Hawk.put("username",username);
                        }
                        View parentLayout = findViewById(android.R.id.content);
                        Snackbar.make(parentLayout, request.getMessage(), Snackbar.LENGTH_SHORT)
                                .show();
                    }
                }

                @Override
                public void onFailure(Call<Request> call, Throwable t) {

                }
            });
        }
    }
}
