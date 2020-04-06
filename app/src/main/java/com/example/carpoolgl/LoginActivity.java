package com.example.carpoolgl;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.carpoolgl.base.baseActivity;
import com.example.carpoolgl.bean.User_;
import com.example.carpoolgl.login.loginPresenter;
import com.example.carpoolgl.login.loginView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;

import okhttp3.MediaType;

public class LoginActivity extends baseActivity<loginView,loginPresenter>
        implements View.OnClickListener,loginView {

    private Button login_bt;

    private MaterialEditText phone_num_et;
    private MaterialEditText password_et;
    private loginPresenter loginP;
    private TextView login_result;//测试用


    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final int LOGIN_TOAST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
        phone_num_et = findViewById(R.id.phone_num_et);
        password_et = findViewById(R.id.password_et);
        login_result = findViewById(R.id.login_result);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_bt:
                login();
                break;
        }
    }

    public void login(){
//        loginP = new loginPresenter();
//        loginP.attachView(this);
        getPresenter().login(phone_num_et.getText().toString(),password_et.getHelperText(),login_result);
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

    @Override
    public void onloginResult(String result) {

    }
}
