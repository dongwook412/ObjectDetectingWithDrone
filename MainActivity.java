package com.example.yang.third;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MainActivity extends Activity {    //메인 activity 시작!

    private Socket socket;  //소켓생성
    BufferedReader in;      //서버로부터 온 데이터를 읽는다.
    TextView output;        //화면구성
    String data;            //

    @Override
    protected void onCreate(Bundle savedInstanceState) {   //앱 시작시  초기화설정
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView rabbit = (ImageView) findViewById(R.id.gif_image);
        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(rabbit);
        Glide.with(this).load(R.drawable.ab).into(gifImage);


        //start
        output = (TextView) findViewById(R.id.output); // 글자출력칸을 찾는다.

        Thread worker = new Thread() {    //worker 를 Thread 로 생성
            public void run() { //스레드 실행구문
                try {
                    //소켓을 생성하고 입출력 스트립을 소켓에 연결한다.
                    socket = new Socket("172.31.216.145", 9999); //소켓생성
                    in = new BufferedReader(new InputStreamReader(
                            socket.getInputStream(), "euc-kr")); //데이터 수신시 stream을 받아들인다.

                } catch (IOException e) {
                    e.printStackTrace();
                }

                //소켓에서 데이터를 읽어서 화면에 표시한다.
                try {
                    while (true) {
                        data = in.readLine(); // in으로 받은 데이타를 String 형태로 읽어 data 에 저장
                            output.post(new Runnable() {
                                public void run() {
                                    output.setText(data); //글자출력칸에 서버가 보낸 메시지를 받는다.
                                }
                            });
                        if (data.equals("111")) {
                            Intent intent = new Intent(
                            getApplicationContext(), // 현재 화면의 제어권자
                                SubActivity.class); // 다음 넘어갈 클래스 지정
                            intent.putExtra("message", data);
                            startActivity(intent); // 다음 화면으로 넘어간다
                            break;

                        }
                    }
                } catch (Exception e) {
                }
            }
        };
//        Button b = (Button)findViewById(R.id.button1);
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //소켓생성
//                try {
//                    socket = new Socket("172.31.216.145", 9999);
//                }catch(Exception e){
//
//                }
//            }
//        });


        worker.start();  //onResume()에서 실행.
    }

    @Override
    protected void onStop() {  //앱 종료시
        super.onStop();
        try {
            socket.close(); //소켓을 닫는다.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

