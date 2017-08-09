package com.kartnap.chandan.shoppydoppy;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class Splash extends AppCompatActivity {
    /** Duration of wait (4 Sec)**/
    private final int SPLASH_DISPLAY_LENGTH = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    NetworkInfo activeNetwork = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))
                            .getActiveNetworkInfo();

                    if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {

                        // Load Categories
                        Intent intent = new Intent();
                        intent.setClass(Splash.this, LoginActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        Splash.this.finish();
                    }else {
                        Intent intent = new Intent();
                        intent.setClass(Splash.this, InternetConnectionFailed.class);
                        startActivity(intent);
                        Splash.this.finish();
                    }

                }
            }, SPLASH_DISPLAY_LENGTH);

    }

}
