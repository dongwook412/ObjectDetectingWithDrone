package com.example.yang.third;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {    //메인 activity 시작!

    private Socket socket;  //소켓생성
    BufferedReader in;      //서버로부터 온 데이터를 읽는다.
    TextView output;        //화면구성
    String receiveData;            // 분석 결과 데이터
    String[] data; // 결과 데이터를 나누어서 저장
    String saveData = "";
    PrintWriter oos;//*
    static int oldTime; // 시작시간
    int time = 0; // 걸린시간
    Map<Integer, String> lb; // 분석 결과로 들어오는 라벨에 대한 값들
    String sendToSubData = ""; // SubActivity에 넘기는 데이터
    boolean state = false; // 넘길지 결정하는 변수
    int count = 0; // sendToSubData에 맨 처음 콤마를 쓰지 않기 위해 쓰이는 변수
    String sibal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {   //앱 시작시  초기화설정
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            Intent intent = getIntent();
            saveData = intent.getStringExtra("message");
        } catch (Exception e) {

        }
        lb = label();

        //gif 삽입
        ImageView rabbit = (ImageView) findViewById(R.id.gif_image);
        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(rabbit);
        Glide.with(this).load(R.drawable.ab).into(gifImage);


        //start
        output = (TextView) findViewById(R.id.output); // 글자출력칸을 찾는다.

        Thread worker = new Thread() {    //worker 를 Thread 로 생성
            public void run() { //스레드 실행구문
                try {
                    //소켓을 생성하고 입출력 스트립을 소켓에 연결한다.

                    socket = new Socket("122.43.26.132", 9999); //소켓생성
//                    socket = new Socket("172.16.92.199", 9999); //소켓생성
                    in = new BufferedReader(new InputStreamReader(
                            socket.getInputStream(), "euc-kr")); //데이터 수신시 stream을 받아들인다.
                    oos = new PrintWriter(socket.getOutputStream()); //*
                    oos.println("Application");//*
                    oos.flush();//*
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //소켓에서 데이터를 읽어서 화면에 표시한다.
                try {
                    oldTime = (int) System.currentTimeMillis() / 1000;
                    while (true) {
//                        time = (int) System.currentTimeMillis() / 1000 - oldTime;
//                        if (time >= 3 && saveData != null) { // 3초가 경과하면 자동으로 이전 경고를 사용
//                            FromMainToSub(saveData);
//                            break;
//                        }
                        try {
                            receiveData = in.readLine();

                            if(receiveData.contains("8") && receiveData.contains("12")){
                                receiveData = "8";
                            }
                            data = receiveData.split(","); // ','로 나누어서 String[]인 data에 저장
                            for (int i = 0; i < data.length; i++) {
                                int num = Integer.parseInt(data[i]);
                                if (num == 8 || num == 12 || num == 15) {
                                    if (count == 0)
                                        sendToSubData = lb.get(num);
                                    else {
                                        sendToSubData = sendToSubData + "," + lb.get(num);
                                    }
                                    state = true;
                                    count++;

                                }
                            }
                            if (state) {
                                FromMainToSub(sendToSubData);
                                break;

                            }
                        }catch(Exception e){

                        }
                        output.post(new Runnable() {
                            public void run() {
                                output.setText(String.valueOf(receiveData)); //글자출력칸에 서버가 보낸 메시지를 받는다.
                            }
                        });


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

    public Map label() {
        HashMap<Integer, String> lb = new HashMap<Integer, String>();
        lb.put(1, "비행기");
        lb.put(2, "자전거");
        lb.put(3, "새");
        lb.put(4, "배");
        lb.put(5, "병");
        lb.put(6, "버스");
        lb.put(7, "차");
        lb.put(8, "고양이");
        lb.put(9, "의자");
        lb.put(10, "소");
        lb.put(11, "테이블");
        lb.put(12, "강아지");
        lb.put(13, "말");
        lb.put(14, "오토바이");
        lb.put(15, "사람");
        lb.put(16, "식물");
        lb.put(17, "양");
        lb.put(18, "소파");
        lb.put(19, "기차");
        lb.put(20, "모니터");
        return lb;
    }

    public void FromMainToSub(String data) {

        Intent intent = new Intent(
                getApplicationContext(), // 현재 화면의 제어권자
                SubActivity.class); // 다음 넘어갈 클래스 지정
        intent.putExtra("message", data);
        startActivity(intent); // 다음 화면으로 넘어간다
        finish();

    }
}

