package com.example.p000.tracker;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class A_Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a__splash);
        splashScreen();

    }

    private void splashScreen() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 5 seconds
                Intent openLoginPage = new Intent(A_Splash.this, A_Login.class);
                startActivity(openLoginPage);

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);  //slide animation
                finish();
            }
        }, 3000);

    }
}
