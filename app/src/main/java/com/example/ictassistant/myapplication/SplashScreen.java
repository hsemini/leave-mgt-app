package com.example.ictassistant.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * -------------------------------------------------------------------------------------------------
 * Created By   : Ms.K.A.H.Semini
 * Date         : 2017.10.09
 * Office       : IT Unit,Chief Secretary's Office, Galle
 * Purpose      : To symbol the loading page.
 * -------------------------------------------------------------------------------------------------
 * Maintain Details
 *
 * No.     Date            Name                        Details
 * (01)    2017.10.10      Ms.K.A.H.Semini             Created
 * (02)
 * -------------------------------------------------------------------------------------------------
 */

public class SplashScreen extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT =7000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
