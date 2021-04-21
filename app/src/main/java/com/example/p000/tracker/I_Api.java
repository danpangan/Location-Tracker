package com.example.p000.tracker;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;


public interface I_Api {

        @FormUrlEncoded
        @POST("/LocationApp/Backend/public/login")
        Call<LoginResponse> userLogin(
                @Field("email") String email,
                @Field("pword") String pword
        );

        @POST("/LocationApp/Backend/public/sendloc")
        Call<ResponseBody> sendLocation(
                @Header("authorization") String auth,
                @Body List<C_Details> location
        );

        /*@POST("/LocationApp/Backend/public/sendloc")
        Call<JSONObject> sendLocation(
                @Header("authorization") String auth,
                @Body JSONObject location
        );*/
}
