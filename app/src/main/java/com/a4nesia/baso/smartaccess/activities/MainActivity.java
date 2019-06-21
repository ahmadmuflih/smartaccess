package com.a4nesia.baso.smartaccess.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.a4nesia.baso.smartaccess.R;
import com.a4nesia.baso.smartaccess.adapters.HistoryAdapter;
import com.a4nesia.baso.smartaccess.models.Access;
import com.a4nesia.baso.smartaccess.requests.HistoryRequest;
import com.a4nesia.baso.smartaccess.requests.Request;
import com.a4nesia.baso.smartaccess.service.FirebaseInstance;
import com.a4nesia.baso.smartaccess.utils.CustomLinearLayoutManager;
import com.a4nesia.baso.smartaccess.utils.Utils;
import com.a4nesia.baso.smartaccess.adapters.RoomAdapter;
import com.a4nesia.baso.smartaccess.models.Privilege;
import com.a4nesia.baso.smartaccess.models.User;
import com.a4nesia.baso.smartaccess.requests.PrivilegeRequest;
import com.a4nesia.baso.smartaccess.requests.UserRequest;
import com.a4nesia.baso.smartaccess.service.APIService;
import com.amulyakhare.textdrawable.TextDrawable;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    BottomAppBar bar;
    RecyclerView recyclerView,historyView;
    BottomSheetDialog bottomSheetDialog;
    View navigationView;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Hawk.init(this).build();
        token =  Hawk.get("token");
        bar = findViewById(R.id.bar);
        navigationView = getLayoutInflater().inflate(R.layout.navigation_menu,null);
        recyclerView = navigationView.findViewById(R.id.privilege_list);
        historyView = findViewById(R.id.recyclerView);
        historyView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),1,false));
        recyclerView.setLayoutManager(new CustomLinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
        bottomSheetDialog.setContentView(navigationView);

        bar.replaceMenu(R.menu.bottom_bar_menu);

        bar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.setting:
                        startActivity(new Intent(getApplicationContext(),SettingActivity.class));
                        break;
                    case R.id.logout:
                        Hawk.put("token","");
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        finish();
                        break;
                }
                return false;
            }

        });
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.show();

            }
        });
        initFirebase();
        checkUser();
        loadData();
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(MainActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Call<Request> updateFCMTokenCall = APIService.service.updateFirebaseToken(token,newToken);
                updateFCMTokenCall.enqueue(new Callback<Request>() {
                    @Override
                    public void onResponse(Call<Request> call, Response<Request> response) {
                        if(response.isSuccessful()){
                            Request request = response.body();
                            if(request.isSuccess()){
                                Log.d("FCM TOKEN:", "FCM Token is updated!");
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

    private void checkUser() {
        Call<UserRequest> userRequestCall = APIService.service.getProfile(token);
        userRequestCall.enqueue(new Callback<UserRequest>() {
            @Override
            public void onResponse(Call<UserRequest> call, Response<UserRequest> response) {
                if(response.isSuccessful()){
                    UserRequest request = response.body();
                    if(request.isSuccess()){
                        User user = request.getUser();
                        String pin = user.getEmployee().getPin();
                        Hawk.put("name", user.getName());
                        Hawk.put("email", user.getEmail());
                        Hawk.put("username", user.getUsername());
                        Hawk.put("card_enabled", user.getEmployee().getCardEnabled());
                        Hawk.put("card_type", user.getEmployee().getCardType());
                        Hawk.put("pin", pin);
                    }
                }
                else{
                    Hawk.put("token", "");
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "Session has expired, please re-login", Snackbar.LENGTH_SHORT)
                            .show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                            finish();
                        }
                    }, 1000);

                }
            }

            @Override
            public void onFailure(Call<UserRequest> call, Throwable t) {
            }
        });
    }

    @Override
    protected void onResume() {
        loadData();
        super.onResume();
    }

    private void loadData() {
        String name = Hawk.get("name","").trim();
        String email = Hawk.get("email","").trim();


        ((TextView)navigationView.findViewById(R.id.name_user)).setText(name);
        ((TextView)navigationView.findViewById(R.id.email_user)).setText(email);
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(Utils.getFirstLetters(name), Color.parseColor("#1565c0"));
        ((ImageView)navigationView.findViewById(R.id.img_user)).setImageDrawable(drawable);

        Call<PrivilegeRequest> privilegeRequestCall = APIService.service.getPrivileges(token);
        privilegeRequestCall.enqueue(new Callback<PrivilegeRequest>() {
            @Override
            public void onResponse(Call<PrivilegeRequest> call, Response<PrivilegeRequest> response) {
                if(response.isSuccessful()){
                    PrivilegeRequest request = response.body();
                    if(request.isSuccess()){
                        ArrayList<Privilege> privileges = request.getPrivilege();
                        RoomAdapter adapter = new RoomAdapter(getApplicationContext(), privileges, new RoomAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Privilege privilege) {

                            }
                        });
                        ((TextView)navigationView.findViewById(R.id.privilege_number)).setText("Privilege Numbers ("+privileges.size()+")");
                        recyclerView.setAdapter(adapter);
                    }
                    else {
                    }
                }
                else{
                    Log.e("MAIN",response.raw()+"");
                }
            }

            @Override
            public void onFailure(Call<PrivilegeRequest> call, Throwable t) {
            }
        });

        Call<HistoryRequest> historyRequestCall = APIService.service.getAccessHistory(token);
        historyRequestCall.enqueue(new Callback<HistoryRequest>() {
            @Override
            public void onResponse(Call<HistoryRequest> call, Response<HistoryRequest> response) {
                if(response.isSuccessful()){
                    HistoryRequest request = response.body();
                    if(request.isSuccess()){
                        HistoryAdapter adapterH = new HistoryAdapter(getApplicationContext(), request.getData(), new HistoryAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Access access) {
                                if(access.getSuccessStatus()==0) {
                                    Intent intent = new Intent(MainActivity.this, ConfirmPinActivity.class);
                                    intent.putExtra("access_id", access.getId());
                                    intent.putExtra("mode",ConfirmPinActivity.MODE_MANUAL);
                                    startActivity(intent);
                                }
                            }
                        });
                        historyView.setAdapter(adapterH);
                        Log.d("HISTORY",request.getData().size()+"");
                    }
                    else {
                        Log.e("HISTORY",response.raw()+"");
                    }
                }else{
                    Log.e("HISTORY",response.raw()+"");
                }
            }

            @Override
            public void onFailure(Call<HistoryRequest> call, Throwable t) {
                Log.e("HISTORY",call.toString()+"");
            }
        });


    }
}
