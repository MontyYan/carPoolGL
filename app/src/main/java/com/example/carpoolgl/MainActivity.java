package com.example.carpoolgl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.amap.api.services.core.LatLonPoint;
import com.example.carpoolgl.bean.User;
import com.example.carpoolgl.dataBase.MydbHelper;
import com.example.carpoolgl.nowLoc.nowLocPresenter;
import com.example.carpoolgl.nowLoc.nowLocView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

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
    private TextView QuitTv;
    private double mlatLong[]=new double[2];//记录当前位置的经纬度

    //创建数据库测试
    private Button cre_db_Test;
    private MydbHelper mydbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getOnTv = findViewById(R.id.getOn_TV);
        getOnTv.setOnClickListener(this);
        getOffTv = findViewById(R.id.getOff_TV);
        getOffTv.setOnClickListener(this);

        cre_db_Test = findViewById(R.id.cre_db);
        cre_db_Test.setOnClickListener(this);

        mydbHelper = new MydbHelper(this,"user.db",null,1);
//        QuitTv.findViewById(R.id.quit);
//        QuitTv.setOnClickListener(this);
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
                intent = new Intent(MainActivity.this,SearchActivity.class);
                intent.putExtra("Edit_select",flag);
                intent.putExtra("nowLocation",nowLocation);
                intent.putExtra("nowLatLong",mlatLong);
                startActivity(intent);
                break;
            case R.id.getOff_TV:
                flag = true;
                intent = new Intent(MainActivity.this,SearchActivity.class);
                intent.putExtra("Edit_select",flag);
                intent.putExtra("nowLocation",nowLocation);
                intent.putExtra("nowLatLong",mlatLong);
                startActivity(intent);
                break;
            case R.id.PersonalInfo:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;

            case R.id.quit:
                intent = new Intent(MainActivity.this, PhoneActivity.class);
                startActivity(intent);
                break;

            case R.id.cre_db:
//                mydbHelper.getWritableDatabase();
                insert();
                break;
        }
    }

    public void insert(){
        String str = "{\"loginResult\":1,\"userInfo\":\"{\\\"Seq\\\":\\\"PA00000001\\\",\\\"Phone\\\":\\\"15277336128\\\",\\\"Password\\\":\\\"1234\\\",\\\"LoginWays\\\":1,\\\"RegisterDate\\\":\\\"Feb 10, 2020 12:00:00 AM\\\",\\\"Sex\\\":\\\"m\\\",\\\"Identity\\\":\\\"学生\\\",\\\"EcyPhone\\\":\\\"18877346041\\\",\\\"Name\\\":\\\"燕明\\\",\\\"IdentityId\\\":\\\"1600300333\\\"}\"}";
        JSONObject jsonObject = JSONObject.parseObject(str);
        JSONObject jsonUser = jsonObject.getJSONObject("userInfo");
        Integer loginResult = jsonObject.getInteger("loginResult");
//        System.out.println(loginResult);
        Gson gson = new Gson();
        User user = gson.fromJson(jsonUser.toString(), User.class);
//        System.out.println(user.toString());

        SQLiteDatabase db=null;
        try{
            db = mydbHelper.getWritableDatabase();
            db.beginTransaction();

            String sql = "insert into user_info(phone,sequence,password,login_ways,register_date,sex,identity,emergency_phone,name,identity_id)"
                    +" values (?,?,?,?,?,?,?,?,?,?)";
            db.execSQL(sql,
                    new Object[]{user.getPhone(),user.getUserSeq(),user.getPassword(),user.getLoginWays(),user.getRegisterDate(),user.getSex(),user.getIdentity(),user.getEcyPhone(),user.getName(),user.getIdentityId()});

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
        }


    }

    public void InitDrawer(){
        personalInfo = findViewById(R.id.PersonalInfo);
        personalInfo.setOnClickListener(this);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigation = findViewById(R.id.nav_view);
        QuitTv = mNavigation.findViewById(R.id.quit);
        QuitTv.setOnClickListener(this);
        mNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                mDrawerLayout.closeDrawers();
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
