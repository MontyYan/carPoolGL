package com.example.carpoolgl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carpoolgl.nowLoc.nowLocPresenter;
import com.example.carpoolgl.nowLoc.nowLocView;

import org.xutils.view.annotation.ViewInject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, nowLocView {

    private TextView getOnTv;
    private TextView getOffTv;
    private nowLocPresenter nowLocP;
    private String nowLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getOnTv = findViewById(R.id.getOn_TV);
        getOnTv.setOnClickListener(this);
        getOffTv = findViewById(R.id.getOff_TV);
        getOffTv.setOnClickListener(this);
        nowLocP = new nowLocPresenter(getApplicationContext());
        nowLocP.attachView(this);
    }

    @Override
    public void onClick(View v) {
        boolean flag = false;   //false代表getonTv编辑，true代表getoffTv编辑
        Intent intent;
        switch (v.getId()){
            case R.id.getOn_TV:
                flag = false;
                Log.e("getOn_Tv","点击getOn_Tv");
                intent = new Intent(MainActivity.this,SearchActivity.class);
                intent.putExtra("Edit_select",flag);
                intent.putExtra("nowLocation",nowLocation);
                startActivity(intent);
                break;
            case R.id.getOff_TV:
                flag = true;
                Log.e("getOff_Tv","点击getOff_Tv");
                intent = new Intent(MainActivity.this,SearchActivity.class);
                intent.putExtra("Edit_select",flag);
                intent.putExtra("nowLocation",nowLocation);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void setGetonText(String addr) {
        nowLocation = addr;
        getOnTv.setText(addr);
        Toast.makeText(this,addr,Toast.LENGTH_LONG).show();
    }

    //销毁
    @Override
    protected void onDestroy() {
        super.onDestroy();
        nowLocP.destory();
        nowLocP.detachView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}
