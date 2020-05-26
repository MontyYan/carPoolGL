package com.example.carpoolgl.manage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.carpoolgl.R;
import com.example.carpoolgl.base.activity.baseActivity;
import com.example.carpoolgl.login.loginPresenter;
import com.example.carpoolgl.login.loginView;
import com.rengwuxian.materialedittext.MaterialEditText;

public class ManageLoginActivity extends baseActivity<loginView, loginPresenter> implements View.OnClickListener,loginView{

    private MaterialEditText manage_number_et;
    private MaterialEditText manage_password_et;
    private Button manage_ensure_bt;

    private String num;
    private String pass;

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
        setContentView(R.layout.activity_manage_login);




    }

    public void initController(){
        manage_number_et = findViewById(R.id.manage_number_et);
        manage_password_et = findViewById(R.id.manage_password_et);
        manage_ensure_bt = findViewById(R.id.manage_ensure_bt);

        setOnClick();
    }

    public void setOnClick(){
        manage_ensure_bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    public void setManageInfo(){
        num = manage_number_et.getHelperText();
        pass = manage_password_et.getHelperText();
    }

    public void manageLoginRequest(){

        getPresenter().manage(this,num,pass);
    }


    @Override
    public void onResult(Integer result, String info) {

    }

    @Override
    public void onRegisteResult(Integer result, String info) {

    }

    @Override
    public void onMsgResult(Integer result, String info) {

    }
}
