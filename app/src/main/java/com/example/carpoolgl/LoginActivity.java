package com.example.carpoolgl;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.LogPrinter;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carpoolgl.Static.STATIC_USERINFO;
import com.example.carpoolgl.base.baseActivity;
import com.example.carpoolgl.bean.User;
import com.example.carpoolgl.bean.User_;
import com.example.carpoolgl.dataBase.MydbHelper;
import com.example.carpoolgl.login.loginPresenter;
import com.example.carpoolgl.login.loginView;
import com.example.carpoolgl.login.mHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rengwuxian.materialedittext.MaterialEditText;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;

public class LoginActivity extends baseActivity<loginView,loginPresenter>
        implements View.OnClickListener,loginView {

    //作为Message.what标识
    private static final int UPDATA_DB = 1;

    private Button login_bt;

//    private MaterialEditText phone_num_et;
    private MaterialEditText password_et;
    private loginPresenter loginP;
    private TextView forget_tv;
    private TextView register_tv;
    private TextView msgLogin_tv;
    private ProgressBar login_progress;
    private TextView cutDown_tv;
    private String phoneNum;
    private TextView login_result;//测试用

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final int LOGIN_TOAST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent = getIntent();
        phoneNum = intent.getStringExtra("phone");
        init();
    }

    @Override
    public loginPresenter createPresenter() {
        return new loginPresenter();
    }

    @Override
    public loginView createView() {
        return this;
    }

    public void init(){
        login_bt = findViewById(R.id.login_bt);
        login_bt.setOnClickListener(this);
//        phone_num_et = findViewById(R.id.phone_num_et);
        password_et = findViewById(R.id.password_et);
        login_result = findViewById(R.id.login_result);
        forget_tv = findViewById(R.id.forget_tv);
        forget_tv.setOnClickListener(this);
        register_tv = findViewById(R.id.register_tv);
        register_tv.setOnClickListener(this);
        msgLogin_tv = findViewById(R.id.msgLogin_tv);
        msgLogin_tv.setOnClickListener(this);
        login_progress = findViewById(R.id.login_progress);
        cutDown_tv = findViewById(R.id.cutDown_tv);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.login_bt:
                if(IsPassword(password_et.getText().toString())){
                    login();
                    setVisible(0);
                    cutDown();
                }
                break;
            case R.id.forget_tv:
//                intent = new Intent(LoginActivity.this,)
                break;
            case R.id.register_tv:
                intent = new Intent(LoginActivity.this,RegisterActivity.class);
                intent.putExtra("phone",phoneNum);
                startActivity(intent);
                break;
            case R.id.msgLogin_tv:
                intent = new Intent(LoginActivity.this,msgLoginActivity.class);
                intent.putExtra("phone",phoneNum);
                startActivity(intent);
                break;
        }
    }

    //接入P层接口
    public void login(){
//        loginP = new loginPresenter();
//        loginP.attachView(this);
//        getPresenter().login(phone_num_et.getText().toString(),password_et.getHelperText(),login_result);
        Log.i("user",phoneNum+" "+password_et.getHelperText());
//        getPresenter().login(phoneNum,password_et.getText().toString(),login_result);
        getPresenter().login(phoneNum,password_et.getText().toString(),login_result,login_progress,login_bt);
    }

    //登录时间倒计时
    public void cutDown(){
        final CountDownTimer timer = new CountDownTimer(15000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                cutDown_tv.setText(millisUntilFinished/1000+"s");
                if(STATIC_USERINFO.getCon().equals(1)){
                    setVisible(1);
                    cancel();
                }
            }

            @Override
            public void onFinish() {
                setVisible(1);
                Toast.makeText(LoginActivity.this,"登录超时",Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    //设置倒计时textView、progress、button、editText可见情况
    public void setVisible(int s){
        switch (s){
            case 0: //登录中状态
                login_bt.setVisibility(View.GONE);              //登录确认bt消失
                cutDown_tv.setVisibility(View.VISIBLE);         //倒计时progress显示
                login_progress.setVisibility(View.VISIBLE);     //倒计时progress显示
                password_et.setFocusable(false);                //登录编辑editext不可编辑
                password_et.setFocusableInTouchMode(false);
                password_et.setOnClickListener(null);
                break;

            case 1: //登录结束状态
                cutDown_tv.setVisibility(View.GONE);            //倒计时tv消失
                login_progress.setVisibility(View.GONE);        //倒计时progress消失
                login_bt.setVisibility(View.VISIBLE);           //登录确认bt显示
                password_et.setFocusable(true);                 //登录编辑editext可编辑
                password_et.setFocusableInTouchMode(true);
                password_et.setOnClickListener(LoginActivity.this);
                break;
        }
    }

    //判断密码格式是否正确
    public boolean IsPassword(String str){
        final Drawable dr = getResources().getDrawable(R.drawable.dir5);
        dr.setBounds(0, 0, 10, 10); //必须设置大小，否则不显示
        if(str.length()<4){
            password_et.setError("密码长度低于四位",dr);
            return false;
        }
        return true;
    }

    public void showResonse(String data){
//        res_tv.setText(data);
        Log.i("LoginActivity","show:"+data);
        Gson gson = new Gson();
        List<User_> list = gson.fromJson(data, new TypeToken<List<User_>>(){}.getType());
        Log.i("LoginActivity",list+"");
        for(User_ u:list){
            Log.i("LoginActivity",u.getId()+"");
            Log.i("LoginActivity",u.getPhoneNum());
            Log.i("LoginActivity",u.getPassword());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if(this.loginP!=null){
//            this.loginP.detachView();
//        }
    }

    /*
    * 得到登录请求结果
    * {loginResult:0/1 ,
    *  userInfo:...}
    * */
    @Override
    public void onloginResult(final String result) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = JSONObject.parseObject(result);
                //获取loginRsult
                Integer loginResult = jsonObject.getInteger("loginResult");
                STATIC_USERINFO.setCon(loginResult);
                /*loginResult==1：登录成功
                 * 解析userInfo，并将user信息存入本地数据库[user.db]
                 * */
                if(loginResult.equals(1)){
                    //获取userInfo
                    JSONObject jsonUser = jsonObject.getJSONObject("userInfo");
                    Gson gson = new Gson();
                    //使用Gson直接逆序列化
                    User user = gson.fromJson(jsonUser.toString(), User.class);
                    Looper.prepare();
                    mHandler handler = new mHandler(getApplicationContext());
                    Message msg = handler.obtainMessage();
                    msg.what = UPDATA_DB;
                    msg.obj = user;
                    Log.i("LoginActivity","jsonObject>>>>>"+jsonObject.toJSONString());
                    Log.i("LoginActivity","msg>>>>>"+msg.obj.toString());
                    handler.sendMessage(msg);
                    Looper.loop();
                }

            }
        }).start();

    }


}
