package com.example.carpoolgl;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.carpoolgl.base.activity.baseActivity;
import com.example.carpoolgl.base.activity.basePresenter;
import com.example.carpoolgl.base.activity.baseView;
import com.example.carpoolgl.bean.User;
import com.example.carpoolgl.login.loginPresenter;
import com.example.carpoolgl.login.loginView;
import com.example.carpoolgl.util.ToastUtil;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends baseActivity<loginView, loginPresenter> implements View.OnClickListener,loginView{


    private ImageView register_back_iv;
//    private MaterialEditText register_phone_et;
    private String phoneNum;
    private String identity = "学生";

    private MaterialEditText register_password_et;
    private MaterialEditText register_ensure_et;
    private MaterialEditText register_name_et;

    private TextView register_student_tv;
    private TextView register_driver_tv;

    private MaterialEditText register_stuId_et;
    private Button register_ensure_bt;
    private ProgressBar register_pb;

    private TextView textView;
    private User user;

    private Integer downTime=30;

    @Override
    public loginPresenter createPresenter() {
        return new loginPresenter();
    }

    @Override
    public loginView createView() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

    }

    public void initController(){
        register_back_iv = findViewById(R.id.register_back_iv);
//        register_phone_et = findViewById(R.id.register_phone_et);
        register_password_et = findViewById(R.id.register_password_et);
        register_ensure_et = findViewById(R.id.register_ensure_et);
        register_name_et = findViewById(R.id.register_name_et);
        register_student_tv = findViewById(R.id.register_student_tv);
        register_driver_tv = findViewById(R.id.register_driver_tv);
        register_stuId_et = findViewById(R.id.register_stuId_et);
        register_ensure_bt = findViewById(R.id.register_ensure_bt);
        register_pb = findViewById(R.id.register_pb);

        setOnClick();
    }

    public void setOnClick(){
        register_student_tv.setOnClickListener(this);
        register_driver_tv.setOnClickListener(this);
        register_ensure_bt.setOnClickListener(this);
    }

    public void getPhone(){
        Intent intent = getIntent();
        phoneNum = intent.getStringExtra("phoneNum");
    }



    /*
    * 密码对比确认
    * */
    public Boolean contrastPassword(){
        String pass = register_password_et.getText().toString();
        String enpass = register_ensure_et.getText().toString();
        if(!pass.equals(enpass)){
            ToastUtil.show(RegisterActivity.this,"两次密码不一样，请重新输入");
            return false;
        }else{
            return true;
        }
    }

    /*
    * 判断输入框是否已经输入信息
    * */
    public Boolean judgeisEdit(){
        String v1,v2,v3;
        v1 = register_password_et.getText().toString();
        v2 = register_ensure_et.getText().toString();
        v3 = register_name_et.getText().toString();

        if(v1.equals("")||v2.equals("")||v3.equals("")){
            ToastUtil.show(RegisterActivity.this,"信息输入不全");
            return false;
        }
        return true;
    }

    public Boolean JudgeId(){
        String id = register_stuId_et.getText().toString();
        if(id.equals("")||id.length()!=10){
            ToastUtil.show(RegisterActivity.this,"学号格式出错");
            return false;
        }

        Pattern pattern = Pattern.compile("[^0-9]+");
        Matcher matcher = pattern.matcher(id);
        if(matcher.find()){
            ToastUtil.show(RegisterActivity.this,"学号格式出错");
            return false;
        }

        return true;

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_ensure_bt:
                judgeisEdit();
                contrastPassword();
                if(judgeisEdit()){
                    if(contrastPassword()){
                        //学生身份
                        if(textView==register_student_tv){

                            if(!JudgeId()){//学号没有输入
                                ToastUtil.show(RegisterActivity.this,"请输入学号");
                            }else{//信息填写完毕
                                setEditable_false();//设置所有的信息不可以再次编辑

                                setUserInfo();//User信息实例化
                                registerRequest();

                            }
                         //司机身份
                        }else if(textView==register_driver_tv){

                        }
                    }
                }

                break;
            case R.id.register_student_tv:
                identity = "学生";
                textView = register_student_tv;
                identityColorSet(register_student_tv);//tv颜色变化
                register_stuId_et.setVisibility(View.VISIBLE);

                break;
            case R.id.register_driver_tv:
                identity = "司机";
                register_student_tv = register_driver_tv;
                identityColorSet(register_driver_tv);//tv颜色变化
                register_stuId_et.setVisibility(View.GONE);

                break;

        }
    }


    public void identityColorSet(TextView ktv){
        GradientDrawable mdraw;

        if(ktv==register_student_tv){
            mdraw = (GradientDrawable)ktv.getBackground();
            mdraw.setColor(getResources().getColor(R.color.num_pitch));

        }else if(ktv==register_driver_tv){
            mdraw = (GradientDrawable)ktv.getBackground();
            mdraw.setColor(getResources().getColor(R.color.num_color));
        }

    }


    public void setUserInfo(){
        user = new User();
        user.setPhone(phoneNum);
        user.setPassword(register_password_et.getText().toString());
        user.setLoginWays(1);
        user.setName(register_name_et.getText().toString());
        user.setIdentity(identity);
        user.setIdentityId(register_stuId_et.getText().toString());

    }

    public void registerRequest(){
        getPresenter().register(this,user);

    }

    public void setEditable_false(){
        Boolean flag = false;
        register_password_et.setFocusable(flag);
        register_ensure_et.setFocusable(flag);
        register_name_et.setFocusable(flag);
        register_stuId_et.setFocusable(flag);
        register_pb.setVisibility(View.VISIBLE);
        register_ensure_bt.setVisibility(View.GONE);


    }

    public void setEditable_true(){
        Boolean flag = true;
        register_password_et.setFocusable(flag);
        register_ensure_et.setFocusable(flag);
        register_name_et.setFocusable(flag);
        register_stuId_et.setFocusable(flag);
        register_pb.setVisibility(View.GONE);
        register_ensure_bt.setVisibility(View.VISIBLE);

    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                setEditable_false();

            }
            else if(msg.what==2){

            }
        }
    };


    public void cutDown(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(;downTime>0;downTime--){
                    handler.sendEmptyMessage(1);
                    try{
                        Thread.sleep(1000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                if(downTime==0){
                    handler.sendEmptyMessage(2);
                }


            }
        }).start();

    }


    @Override   //回调登录方法  在此不重写
    public void onloginResult(String result) {
    }

    @Override   //model层回调注册方法 需重写
    public void onRegisteResult(Integer result, String info) {

    }

}
