package com.example.carpoolgl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.example.carpoolgl.util.ToastUtil;
import com.mob.MobSDK;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class msgLoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG="msgLoginActivity";

    private Button msglogin_bt;
    private PinEntryEditText msgCode_et;
    private TextView registerCon_tv;
    private TextView reg_phoneNum_tv;

    private String APPKEY = "2f4652a2938f9";
    private String APPSECRET = "605ed08f8af5cdd2eae05ffacbcae9b3";

    private int downTime = 30;
    private String phoneNum="15277336128";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_login);

        if(Build.VERSION.SDK_INT>=23){
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
        }
        initController();//控件注册
        getPhoneNum();//获取手机号码
        setOnClick();//点击监听
        startMsg();
    }

    public void getPhoneNum(){
        Intent intent = getIntent();
        phoneNum = intent.getStringExtra("phoneNum");
        reg_phoneNum_tv.setText("+86 "+phoneNum);
    }



    public void startMsg(){
        MobSDK.init(this,APPKEY,APPSECRET);
        EventHandler eventHandler = new EventHandler(){

            @Override
            public void afterEvent(int i, int i1, Object o) {
                Message msg = new Message();
                msg.arg1 = i;
                msg.arg2 = i1;
                msg.obj = o;
                handler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eventHandler);
    }

    public void initController(){
        msglogin_bt = findViewById(R.id.msglogin_bt);
        msgCode_et = findViewById(R.id.msgCode_et);
        registerCon_tv = findViewById(R.id.registerCon_tv);
        reg_phoneNum_tv = findViewById(R.id.reg_phoneNum_tv);
    }

    public void setOnClick(){
        msglogin_bt.setOnClickListener(this);
        msgCode_et.setOnClickListener(this);
        registerCon_tv.setOnClickListener(this);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            if(msg.what==-9){
                registerCon_tv.setBackgroundColor(Color.parseColor("#FF0000"));
                registerCon_tv.setText("重新发送("+downTime+")");
                registerCon_tv.setClickable(false);
            }else if(msg.what==-8){
                registerCon_tv.setText("获取验证码");
                registerCon_tv.setClickable(true);
                downTime = 30;
            }else{
                Log.i(TAG,"ELSE");
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                Log.i(TAG,data.toString());

                if(event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    if(result == SMSSDK.RESULT_COMPLETE){
                        Log.i(TAG,"i == SMSSDK.EVENT_GET_VOICE_VERIFICATION_CODE");
                        ToastUtil.show(msgLoginActivity.this,"正在获取验证码");
                    }else{
                        ToastUtil.show(msgLoginActivity.this,"获取验证码失败");
                    }
                }else if(event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                    if (result == SMSSDK.RESULT_COMPLETE) {


                        // TODO 处理验证码验证通过的结果
                        Log.i(TAG,"i == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE");
                        ToastUtil.show(msgLoginActivity.this,"验证成功");
                    } else {


                        // TODO 处理错误的结果
                        ToastUtil.show(msgLoginActivity.this,"验证失败");
                        ((Throwable) data).printStackTrace();
                    }
                }

            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerCon_tv:
                SMSSDK.getVerificationCode("86",phoneNum);
                registerCon_tv.setBackgroundColor(Color.parseColor("#4c90f9"));
                registerCon_tv.setText("重新发送("+downTime+")");
                registerCon_tv.setClickable(false);
                runThread();
                break;
            case R.id.msglogin_bt:
//                ToastUtil.show(this,msgCode_et.getText()+"");
                SMSSDK.submitVerificationCode("86",phoneNum,msgCode_et.getText()+"");

                break;
        }
    }

    public void runThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for ( ; downTime  > 0; downTime--) {
                    handler.sendEmptyMessage(-9);
                    if (downTime <= 0 ){
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                handler.sendEmptyMessage(-8);
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }
}
