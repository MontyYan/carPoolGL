package com.example.carpoolgl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.LatLng;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,nowLocView{
    private TextView getOnTv;
    private nowLocPresenter nowLocP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getOnTv = findViewById(R.id.getOn_TV);
        getOnTv.setOnClickListener(this);
        nowLocP = new nowLocPresenter(getApplicationContext());
        nowLocP.attachView(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.getOn_TV:
                Log.e("getOn_Tv","点击");
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void setGetonText(String addr) {
        getOnTv.setText(addr);
        Toast.makeText(this,addr,Toast.LENGTH_LONG).show();
    }

    //销毁
    @Override
    protected void onDestroy() {
        super.onDestroy();
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
