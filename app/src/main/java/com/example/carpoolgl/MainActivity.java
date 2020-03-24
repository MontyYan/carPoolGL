package com.example.carpoolgl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.LatLonPoint;
import com.example.carpoolgl.nowLoc.nowLocPresenter;
import com.example.carpoolgl.nowLoc.nowLocView;
import com.google.android.material.navigation.NavigationView;

import org.xutils.view.annotation.ViewInject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, nowLocView {

    private TextView getOnTv;
    private TextView getOffTv;
    private ImageButton personalInfo;
//    private TextView drawer_fog;
    private nowLocPresenter nowLocP;
    private String nowLocation;
    //drawelayout不覆盖
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigation;
    private double mlatLong[]=new double[2];//记录当前位置的经纬度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getOnTv = findViewById(R.id.getOn_TV);
        getOnTv.setOnClickListener(this);
        getOffTv = findViewById(R.id.getOff_TV);
        getOffTv.setOnClickListener(this);
        InitDrawer();
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
//                Log.e("getOn_Tv","点击getOn_Tv");
                intent = new Intent(MainActivity.this,SearchActivity.class);
                intent.putExtra("Edit_select",flag);
                intent.putExtra("nowLocation",nowLocation);
                intent.putExtra("nowLatLong",mlatLong);
                startActivity(intent);
                break;
            case R.id.getOff_TV:
                flag = true;
//                Log.e("getOff_Tv","点击getOff_Tv");
                intent = new Intent(MainActivity.this,SearchActivity.class);
                intent.putExtra("Edit_select",flag);
                intent.putExtra("nowLocation",nowLocation);
                intent.putExtra("nowLatLong",mlatLong);
                startActivity(intent);
                break;
            case R.id.PersonalInfo:

                mDrawerLayout.openDrawer(GravityCompat.START);
//                drawer_fog.setVisibility(View.VISIBLE);
                break;
//            case R.id.drawer_fog:
////                drawer_fog.setVisibility(View.INVISIBLE);
//                break;
        }
    }

    public void InitDrawer(){
        personalInfo = findViewById(R.id.PersonalInfo);
        personalInfo.setOnClickListener(this);
//        drawer_fog = findViewById(R.id.drawer_fog);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigation = findViewById(R.id.nav_view);

        mNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
//                drawer_fog.setVisibility(View.INVISIBLE);
                return true;
            }
        });
    }

    @Override
    public void setGetonText(String addr) {
        nowLocation = addr;
        getOnTv.setText(addr);
        Toast.makeText(this,addr,Toast.LENGTH_LONG).show();
    }

    @Override
    public void setLatLon(double lon, double lat) {
        mlatLong[0]=lon;
        mlatLong[1]=lat;
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
