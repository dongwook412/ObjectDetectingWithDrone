package com.example.yang.third;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class SubActivity extends AppCompatActivity {
    TextView test;
    String data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        test = (TextView) findViewById(R.id.test);
        Intent intent = getIntent();
        data = intent.getStringExtra("message");
        test.setText("가나다");
//        test.setText(data);
    }
}
