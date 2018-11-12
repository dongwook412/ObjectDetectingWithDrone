package com.example.yang.third;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;

import android.view.Window;
import android.widget.TextView;

import static android.speech.tts.TextToSpeech.ERROR;

import java.util.Locale;
import java.util.Map;

public class SubActivity extends AppCompatActivity {
    TextView test;
    String data;
    TextToSpeech tts;
    String speech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sub);

        // 경고할 text 지정
        test = (TextView) findViewById(R.id.test);
        Intent intent = getIntent();
        data = intent.getStringExtra("message");
        test.setText(data+" 출현");
        speech = "경고 전방에 " + data+" 출현, 속도를 줄이세요";

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != ERROR) {
                    // 언어를 선택한다.
                    tts.setLanguage(Locale.KOREAN);
                    tts.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                }
            }

        });


        // 일정 시간 후 화면 전환
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Intent i = new Intent(SubActivity.this, MainActivity.class);
                i.putExtra("message", data);
                startActivity(i);
                finish();

            }
        }, 4750); // 1초당 1000


    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }



}

