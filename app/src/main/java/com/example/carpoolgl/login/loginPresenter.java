package com.example.carpoolgl.login;

import android.widget.TextView;
import com.example.carpoolgl.base.basePresenter;

public class loginPresenter extends basePresenter<loginView> {

    private loginModel loginModel;

    public loginPresenter(){
        this.loginModel = new loginModel();
    }

    public void login(String phoneNum, String password, TextView loginResult){
        this.loginModel.login_(phoneNum,
                password,
                getView(),
                loginResult
        );
    }
}
