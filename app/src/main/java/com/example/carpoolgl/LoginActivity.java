package com.example.carpoolgl;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carpoolgl.bean.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button login_bt;
    private EditText phone_num_tv;
    private TextView res_tv;

    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    public void init(){
        login_bt = findViewById(R.id.login_bt);
        login_bt.setOnClickListener(this);
        phone_num_tv = findViewById(R.id.phone_num_et);
        res_tv = findViewById(R.id.res_tv);
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    User u = new User(phone_num_tv.getText().toString());
//                    User u = new User("8008208820","123");
                    Gson gson = new Gson();
                    RequestBody requestBody = RequestBody.create(JSON,gson.toJson(u));
                    Request request = new Request.Builder()
                            .url("http://192.168.0.107:8080/login")
                            .post(requestBody)
                            .build();

                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.i("LoginActivity",responseData);
//                    showResonse(responseData);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void showRes(final String data){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                res_tv.setText(data);
            }
        });
    }

    public void showResonse(String data){
//        res_tv.setText(data);
        Log.i("LoginActivity","show:"+data);
        Gson gson = new Gson();
        List<User> list = gson.fromJson(data, new TypeToken<List<User>>(){}.getType());
        Log.i("LoginActivity",list+"");
        for(User u:list){
            Log.i("LoginActivity",u.getId()+"");
            Log.i("LoginActivity",u.getPhoneNum());
            Log.i("LoginActivity",u.getPassword());
        }
    }
}
