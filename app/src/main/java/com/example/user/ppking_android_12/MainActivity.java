package com.example.user.ppking_android_12;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.nio.Buffer;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private UIHandler uiHandler;
    private ImageView imageView;
    private Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView)findViewById(R.id.imageView);
        textView = (TextView)findViewById(R.id.textview);
        uiHandler = new UIHandler();

    }
    public void test1(View v){

        new Thread(){
            @Override
            public void run() {
                byte[] buf = "Hello , ppking".getBytes();
                try {
                    DatagramSocket socket = new DatagramSocket();
                    DatagramPacket packet = new DatagramPacket(buf, buf.length,
                            InetAddress.getByName("10.0.3.2"), 8888);
                    socket.send(packet);
                    socket.close();
                    Log.v("ppking", "UDP Send OK");
                }catch (Exception e) {
                    Log.v("ppking", e.toString());
                }
            }
        }.start();
    }
    public void test2(View v){
        new Thread(){
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(InetAddress.getByName("10.0.3.2"), 9999);
                    socket.close();
                    Log.v("brad", "TCP Client OK");
                }catch (Exception e){
                    Log.v("brad", e.toString());
                }
            }
        }.start();
    }
    public void test3(View v){
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL("http://www.iii.org.tw/");
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.connect();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    String line;
                    StringBuilder sb = new StringBuilder();
                    while ((line=reader.readLine())!=null){
                        sb.append(line + "\n");
                    }
                    reader.close();
                    Message mesg = new Message();
                    Bundle data = new Bundle();
                    data.putCharSequence("data",sb);
                    mesg.setData(data);
                    mesg.what=0;
                    uiHandler.sendMessage(mesg);

                } catch (Exception e) {
                    Log.v("ppking",e.toString());
                }
            }
        }.start();
    }
    public void test4(View v){
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL(
                            "https://www.android.com/static/2016/img/hero-carousel/android-nougat.png"
                    );
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.connect();

                    Bitmap bmp = BitmapFactory.decodeStream(conn.getInputStream());


                    Message mesg = new Message();
                    Bundle data = new Bundle();
                    data.putParcelable("data", bmp);
                    mesg.setData(data);
                    mesg.what = 1;
                    uiHandler.sendMessage(mesg);


                }catch (Exception e){

                }
            }
        }.start();
    }
    public void test5(View v){
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL(
                            "http://4.bp.blogspot.com/-v9_Z2ikPBBY/Uf40h_z720I/AAAAAAAAAUQ/GLsEW1IxIzs/s1600/Android+tutorials.jpg");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();

                    bmp = BitmapFactory.decodeStream(conn.getInputStream());
                    uiHandler.sendEmptyMessage(2);

                }catch (Exception e){
                    Log.v("ppking", e.toString());
                }
            }
        }.start();
    }
    private class UIHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0 :
                    textView.setText(msg.getData().getCharSequence("data"));
                    break;
                case 1:
                    imageView.setImageBitmap((Bitmap)msg.getData().getParcelable("data"));
                    break;
                case 2:
                    imageView.setImageBitmap(bmp);
                    break;

            }
        }
    }

}
