package com.example.carpoolgl.login;

import android.app.Activity;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.carpoolgl.base.activity.basePresenter;
import com.example.carpoolgl.bean.User;

public class loginPresenter extends basePresenter<loginView> {

    private loginModel model;

    public loginPresenter(){
        this.model = new loginModel();
    }

//    public void login(String phoneNum, String password, TextView loginResult){
//        this.model.login_(phoneNum,
//                password,
//                getView(),
//                loginResult
//        );
//    }

    public void login(String phoneNum, String password, TextView loginResult, ProgressBar Login_progress, Button Login_button){
        this.model.login_test(phoneNum,
                password,
                getView(),
                loginResult,
                Login_progress,
                Login_button
        );
    }


    public void register(Activity activity, User user){
        this.model.register(activity,user,getView());
    }

}
