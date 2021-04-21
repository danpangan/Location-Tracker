package com.example.p000.tracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.p000.tracker.C_GlobalHolder.auth;

public class A_Login extends AppCompatActivity{

    private ImageView imgViewRBoxEmail, imgViewRBoxPass;
    private EditText eTxtEmail, eTxtPass;
    private ImageButton btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a__login);
        setEvents();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(SharedPrefManager.getmInstance(this).isLoggedIn()) {

            if(SharedPrefManager.getmInstance(this).getCurrActivity() == "setTime") {

                Intent intent = new Intent(this, A_SetTime.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            } else if(SharedPrefManager.getmInstance(this).getCurrActivity() == "tracking") {

                Intent intent = new Intent(this, A_Tracking.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            } else if(SharedPrefManager.getmInstance(this).getCurrActivity() == "summary") {

                Intent intent = new Intent(this, A_Summary.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }

        }
    }

    private void userLogin() {

        EditText emailTxt = findViewById(R.id.eTxt_email);
        EditText passwordTxt = findViewById(R.id.eTxt_password);

        String email = emailTxt.getText().toString().trim();
        String password = passwordTxt.getText().toString().trim();

        if(email.isEmpty()) {
            eTxtEmail.setError("Email is required");
            eTxtEmail.requestFocus();
            return;
        }

        if(password.isEmpty()) {
            eTxtPass.setError("Password is required");
            eTxtPass.requestFocus();
            return;
        }

        Call<LoginResponse> call = RetrofitClient.getInstance().getApi().userLogin(email, password);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if(response.code() == 200) {
                    auth = response.headers().get("auth");
                    /*currActivity = "startActivity";*/
                    LoginResponse loginResponse = response.body();
                    /*System.out.print(response.headers());*/

                    SharedPrefManager.getmInstance(A_Login.this).saveAuth(auth, loginResponse.getUser(), "setTime");

                    Intent openSetTimePage = new Intent(A_Login.this, A_SetTime.class);
                    openSetTimePage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(openSetTimePage);

                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);  //slide animation
                    finish();

                    Toast.makeText(A_Login.this, "Logged in successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(A_Login.this, "Email and/or password is incorrect", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });
    }

    private void setEvents()
    {
        btnSignIn = findViewById(R.id.imgBtn_signIn);
        imgViewRBoxEmail = findViewById(R.id.imgView_email);
        imgViewRBoxPass = findViewById(R.id.imgView_password);
        eTxtEmail = findViewById(R.id.eTxt_email);
        eTxtPass = findViewById(R.id.eTxt_password);

        btnSignIn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    btnSignIn.setImageAlpha(128);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    btnSignIn.setImageAlpha(255);
                }
                return false;
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });
    }

}
