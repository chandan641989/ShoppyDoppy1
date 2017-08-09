package com.kartnap.chandan.shoppydoppy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class InternetConnectionFailed extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_connection_failed);
        textView = (TextView)findViewById(R.id.text);

    }
}
