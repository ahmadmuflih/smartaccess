package com.a4nesia.baso.smartaccess.service;


import com.a4nesia.baso.smartaccess.MyApp;
import com.a4nesia.baso.smartaccess.models.Access;
import com.a4nesia.baso.smartaccess.models.User;
import com.a4nesia.baso.smartaccess.requests.HistoryRequest;
import com.a4nesia.baso.smartaccess.requests.LoginRequest;
import com.a4nesia.baso.smartaccess.requests.PrivilegeRequest;
import com.a4nesia.baso.smartaccess.requests.Request;
import com.a4nesia.baso.smartaccess.requests.UserRequest;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface APIService {


    @FormUrlEncoded
    @POST("login")
    Call<LoginRequest> login(@Field("username") String username, @Field("password") String password);



    @GET("user")
    Call<UserRequest> getProfile(@Header("Authorization") String token);

    @GET("privilege")
    Call<PrivilegeRequest> getPrivileges(@Header("Authorization") String token);

    @GET("history")
    Call<HistoryRequest> getAccessHistory(@Header("Authorization") String token);

    @PUT("user")
    Call<Request> updateProfile(@Header("Authorization") String token,@Body User user);

    @FormUrlEncoded
    @PUT("user/pin")
    Call<Request> updatePin(@Header("Authorization") String token, @Field("pin") String newPin);

    @FormUrlEncoded
    @PUT("user/firebase_token")
    Call<Request> updateFirebaseToken(@Header("Authorization") String token, @Field("token") String newToken);

    @FormUrlEncoded
    @PUT("user/card")
    Call<Request> updateCardStatus(@Header("Authorization") String token, @Field("card_enabled") int newStatus);

    @FormUrlEncoded
    @PUT("user/password")
    Call<Request> updatePassword(@Header("Authorization") String token, @Field("old_password") String currentPassword, @Field("new_password") String newPassword);

    @FormUrlEncoded
    @PUT("history/confirm")
    Call<Request> confirmAccess(@Header("Authorization") String token, @Field("pin") String pin,@Field("access_id") int accessId);
//
//    @GET("news")
//    Call<NewsRequest> getNews(@Header("Authorization") String token, @Query("page") int page);
//
//    @GET("pengaduanbyuser")
//    Call<PengaduanRequest> getPengaduan(@Header("Authorization") String token);
//
//    @GET("getlayanan")
//    Call<LayananRequest> getLayanan(@Header("Authorization") String token);
//
//    @GET("getsurvey")
//    Call<SurveyIndividuRequest> getSurveyIndividu(@Header("Authorization") String token);
//
//    @POST("addpengaduan")
//    @Multipart
//    Call<AddPengaduanRequest> addPengaduan(@Header("Authorization") String token, @Part("id_siswa") int idSiswa,
//                                           @Part("id_jenis") int idJenis, @Part("isi") RequestBody isi, @Part MultipartBody.Part file);



    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(MyApp.getBaseUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    APIService service = retrofit.create(APIService.class);
}