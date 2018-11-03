import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerThread extends Thread {

    Socket client;
    BufferedReader in;
    BufferedWriter out;
    Vector<ServerThread> connectList;
    int count = 0;
    public ServerThread(Vector<ServerThread> connectList, Socket socket) {
        this.connectList = connectList;
        this.client = socket;
        try {
            in = new BufferedReader(new InputStreamReader((client.getInputStream())));
            out = new BufferedWriter(new OutputStreamWriter((client.getOutputStream())));
        } catch (IOException e) {
//            System.out.println("3");
//            e.printStackTrace();
        }
    }

    public void run(){
        try {
            while (true) {
//                if(in.readLine().equals("q")){
//                    client.close();
//                }
                String msg = listen();
                send(msg);
                count++;
                
                if(count>100){
                    client.close();
                    System.out.println(client.isClosed());
                    break;
                }
            }
        } catch (Exception e) {
//            System.out.println("4");
        }
    }
     public static void close(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
//                System.out.println("5");
                e.printStackTrace();
            }
        }
    }

    //메시지 청취
    public String listen() {
        String msg = "";
        try {
            msg = in.readLine();
            if(msg.equals(null)){
                
            }
         
            System.out.println("message:" + msg);
        } catch (IOException e) {
//            System.out.println("6");
            
        } catch (NullPointerException e1){
//            System.out.println("7");
        }
        return msg;
    }

    //메시지 전송
    public void send(String msg) {
        try {
            for (int i = 0; i < connectList.size(); i++) {
                ServerThread st = connectList.get(i);
                st.out.write(msg + "\n");
                st.out.flush();
            }
        } catch (IOException e) {
//            System.out.println("8");
        }
    }
}
