package com.kartnap.chandan.shoppydoppy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ProductsList extends AppCompatActivity {
    private String subCat;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);
        subCat = getIntent().getStringExtra("title");
        textView = (TextView)findViewById(R.id.textView);
        textView.setText(subCat);
        setTitle(subCat);
    }
}
