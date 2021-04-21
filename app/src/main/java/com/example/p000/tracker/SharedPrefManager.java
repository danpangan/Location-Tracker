package com.example.p000.tracker;

import android.content.Context;
import android.content.SharedPreferences;

import static com.example.p000.tracker.C_GlobalHolder.auth;

public class SharedPrefManager {

    public static final String SHARED_PREF_NAME = "session_auth";

    private static SharedPrefManager mInstance;
    private Context mCtx;

    public SharedPrefManager(Context mCtx) {
        this.mCtx = mCtx;
    }

    public static synchronized SharedPrefManager getmInstance(Context mCtx) {
        if(mInstance == null) {
            mInstance = new SharedPrefManager(mCtx);
        }
        return mInstance;
    }

    public void saveAuth(String auth, C_User user, String currActivity) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("id", user.getId());
        editor.putString("auth", auth);
        editor.putString("activity", currActivity);

        editor.apply();
    }

    public boolean isLoggedIn() {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        C_User user = new C_User();

        if((sharedPreferences.getInt("id", -1) == user.getId()) &&
                (sharedPreferences.getString("auth", "") == auth)) {

            return true;
        }

        return false;
    }

    public void setCurrActivity(String currActivity) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("activity", currActivity);

        editor.apply();
    }

    public String getCurrActivity() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("activity", null);
    }

    /*public C_User getUser() {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        C_User user = new C_User();

                user.setId(sharedPreferences.getInt("id", -1));
                user.setAuth(sharedPreferences.getString("auth", null));

        return user;
    }*/

    public void clear() {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.apply();
    }
}
