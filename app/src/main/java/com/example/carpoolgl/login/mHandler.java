package com.example.carpoolgl.login;


import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class mHandler extends Handler {
    TextView showLoginResult;

    public mHandler(TextView textView){
        this.showLoginResult = textView;
    }

    @Override
    public void handleMessage(Message msg){
        String text = (String)msg.obj;
        showLoginResult.setText("登录: "+text);
    }
}
