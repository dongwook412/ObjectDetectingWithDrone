
import java.net.*;
import java.io.*;
import java.util.*;

class Server {

    int port = 9999;
    ServerSocket server = null;
    Socket child = null;

    HashMap<String, PrintWriter> hm;

    public Server() {

        ServerThread sr;
        Thread t;

        try {
            server = new ServerSocket(port); //서버소켓을 생성

            System.out.println("Server Open");
            System.out.println("Waiting Client...");

            hm = new HashMap<String, PrintWriter>();

            while (true) {
                child = server.accept(); //클라이언트의 소켓을 연결받음.
                if (child != null) { //클라이언트 소켓과 연결시
                    sr = new ServerThread(child, hm); //스레드를 생성
                    t = new Thread(sr); //스레드 시작
                    t.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server();
    }

}

class ServerThread implements Runnable {

    Socket child;
    BufferedReader ois;
    PrintWriter oos;

    String user_id;
    HashMap<String, PrintWriter> hm;
    InetAddress ip;
    String msg;

    public ServerThread(Socket s, HashMap<String, PrintWriter> h) {
        child = s;
        hm = h;

        try {
            ois = new BufferedReader(new InputStreamReader(child.getInputStream()));
            oos = new PrintWriter(child.getOutputStream());

            user_id = ois.readLine();
            ip = child.getInetAddress();
            System.out.println(ip + "로부터 " + user_id + " 접속");
            broadcast(user_id + " 접속");

            synchronized (hm) {
                hm.put(user_id, oos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void run() {
        String receiveData;

        try {
            while ((receiveData = ois.readLine()) != null) {

                if (receiveData.equals("/quit")) {
                    synchronized (hm) {
                        hm.remove(user_id);
                    }

                    break;
                } else {
                    System.out.println(user_id + " >> " + receiveData);
                    broadcast(receiveData);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            synchronized (hm) {
                hm.remove(user_id);
            }

            try {
                if (child != null) {
                    ois.close();
                    oos.close();
                    child.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public void broadcast(String message) {
        synchronized (hm) {
            try {
                for (PrintWriter oos : hm.values()) {
                    oos.println(message);
                    oos.flush();
                }
            } catch (Exception e) {
            }
        }
    }

}
