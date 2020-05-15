package com.example.carpoolgl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText phone_et;
    private Button next_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        phone_et = findViewById(R.id.phone_et);
        next_bt = findViewById(R.id.next_bt);
        next_bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.next_bt:
//                IsPhoneNum(phone_et.getText().toString());
                Intent intent = new Intent(PhoneActivity.this,LoginActivity.class);
                if(IsPhoneNum(phone_et.getText().toString())){
                    intent.putExtra("phoneNum",phone_et.getText().toString());
                    Log.i("PhoneAactivity",phone_et.getText().toString());
                    startActivity(intent);
                }
                break;

        }
    }

    public boolean IsPhoneNum(String str){
        final Drawable dr = getResources().getDrawable(R.drawable.dir5);
        dr.setBounds(0, 0, 10, 10); //必须设置大小，否则不显示

        Pattern pattern = Pattern.compile("[^0-9]+");
        Matcher matcher = pattern.matcher(phone_et.getText().toString());
        if(matcher.find()){
            phone_et.setError("手机号码必须为数字",dr);
            return false;
        }
        if(str.length()!=11){
            phone_et.setError("手机号码只能为11位",dr);
            return false;
        }
        return true;
    }

}
