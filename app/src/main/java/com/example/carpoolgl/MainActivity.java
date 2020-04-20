package com.example.carpoolgl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSONObject;
import com.example.carpoolgl.Static.STATIC_USERINFO;
import com.example.carpoolgl.bean.User;
import com.example.carpoolgl.dataBase.MydbHelper;
import com.example.carpoolgl.main.mainPresenter;
import com.example.carpoolgl.main.mainView;
import com.example.carpoolgl.nowLoc.nowLocPresenter;
import com.example.carpoolgl.nowLoc.nowLocView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import org.xutils.view.annotation.ViewInject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, nowLocView, mainView {
    private static final String TAG="MainActivity";

    private TextView getOnTv;
    private TextView getOffTv;
    private ImageButton personalInfo;
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

    //个人用户数据控件
    private TextView PhoneNum_Navheader_tv;
    private TextView regDate_Navheader_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitDrawer();
        initController();
        //创建数据库button test
//        cre_db_Test = findViewById(R.id.cre_db);
//        cre_db_Test.setOnClickListener(this);
        findLocInfo_();
        mainPresenter mp = new mainPresenter(MainActivity.this);
        mp.attachView(this);
        mp.findLocUInfo();
        mp.detachView();
        nowLocP = new nowLocPresenter(getApplicationContext());
        nowLocP.attachView(this);
    }

    public void initController(){
        getOnTv = findViewById(R.id.getOn_TV);
        getOnTv.setOnClickListener(this);
        getOffTv = findViewById(R.id.getOff_TV);
        getOffTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        boolean flag = false;   //false代表getonTv编辑，true代表getoffTv编辑
        Intent intent;
        switch (v.getId()){
            case R.id.getOn_TV:
                if(!STATIC_USERINFO.getCon().equals(0)){
                    flag = false;
                    intent = new Intent(MainActivity.this,SearchActivity.class);
                    intent.putExtra("Edit_select",flag);
                    intent.putExtra("nowLocation",nowLocation);
                    intent.putExtra("nowLatLong",mlatLong);
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this,"请先登录账号",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.getOff_TV:
                if(!STATIC_USERINFO.getCon().equals(0)){
                    flag = true;
                    intent = new Intent(MainActivity.this,SearchActivity.class);
                    intent.putExtra("Edit_select",flag);
                    intent.putExtra("nowLocation",nowLocation);
                    intent.putExtra("nowLatLong",mlatLong);
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this,"请先登录账号",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.PersonalInfo:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;

            case R.id.quit:
//                intent = new Intent(MainActivity.this, PhoneActivity.class);
//                startActivity(intent);
                quitDialog();
                break;

//            case R.id.cre_db:
////                mydbHelper.getWritableDatabase();
//                insert();
//                break;
        }
    }

    //主页面点击退出登录时的弹出窗口，用于再次确认
    public void quitDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("确认退出");
        dialog.setMessage("退出后，您的本地消息将被清空");
        dialog.setCancelable(false);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deShareData();
                Intent intent = new Intent(MainActivity.this, PhoneActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass(MainActivity.this,PhoneActivity.class);
                startActivity(intent);
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
    }
    //退出登录后，没有登录，本地用户登录数据覆盖
    public void deShareData(){
        STATIC_USERINFO.setCon(0);
        SharedPreferences.Editor shaEdit = getSharedPreferences("userinfo",MODE_PRIVATE).edit();
        shaEdit.putInt("conCode",0);
        shaEdit.putString("phone","未登录");
        shaEdit.putString("registerDate","");
        shaEdit.putString("sequence","");
        shaEdit.apply();

    }

    //测试用，后删
    public void insert(){
        mydbHelper = new MydbHelper(this,"user.db",null,1);
        String str = "{\"loginResult\":1,\"userInfo\":\"{\\\"Seq\\\":\\\"PA00000001\\\",\\\"Phone\\\":\\\"15277336128\\\",\\\"Password\\\":\\\"1234\\\",\\\"LoginWays\\\":1,\\\"RegisterDate\\\":\\\"Feb 10, 2020 12:00:00 AM\\\",\\\"Sex\\\":\\\"m\\\",\\\"Identity\\\":\\\"学生\\\",\\\"EcyPhone\\\":\\\"18877346041\\\",\\\"Name\\\":\\\"燕明\\\",\\\"IdentityId\\\":\\\"1600300333\\\"}\"}";
        JSONObject jsonObject = JSONObject.parseObject(str);
        JSONObject jsonUser = jsonObject.getJSONObject("userInfo");
        Integer loginResult = jsonObject.getInteger("loginResult");
        Gson gson = new Gson();
        User user = gson.fromJson(jsonUser.toString(), User.class);
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

    public void findLocInfo_(){
        PhoneNum_Navheader_tv.setText(STATIC_USERINFO.getPhone()+"");
//        PhoneNum_Navheader_tv.setText("aksdjhfadsh");
        regDate_Navheader_tv.setText("注册日期："+STATIC_USERINFO.getRegisterDate());
//        regDate_Navheader_tv.setText("ahdsufhiasufd");
    }

    //DrawerLayout滑动窗口
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

        View nav_header = mNavigation.inflateHeaderView(R.layout.nav_header_main);
//        View nav_header = mNavigation.getHeaderView(0);
        Log.i(TAG,nav_header+"");
        LinearLayout layout = nav_header.findViewById(R.id.header_layout);
        PhoneNum_Navheader_tv = layout.findViewById(R.id.PhoneNum_Navheader_tv);
        PhoneNum_Navheader_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"点击",Toast.LENGTH_SHORT).show();
            }
        });
        Log.i(TAG,PhoneNum_Navheader_tv+"");
        regDate_Navheader_tv = layout.findViewById(R.id.regDate_Navheader_tv);
        regDate_Navheader_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"点击",Toast.LENGTH_SHORT).show();
            }
        });
        Log.i(TAG,regDate_Navheader_tv+"");
        PhoneNum_Navheader_tv.setText("dsahfashdfo");
        Log.i(TAG,PhoneNum_Navheader_tv.getText().toString());
        regDate_Navheader_tv.setText("9283r98e79q");
        Log.i(TAG,regDate_Navheader_tv.getText().toString());
    }

    //设置全局静态
    @Override
    public void SetUInfo() {
        PhoneNum_Navheader_tv.setText(STATIC_USERINFO.getPhone());
        regDate_Navheader_tv.setText("注册日期："+STATIC_USERINFO.getRegisterDate());
    }

    //toast当前中文地址
    @Override
    public void setGetonText(String addr) {
        nowLocation = addr;
        getOnTv.setText(addr);
        Toast.makeText(this,addr,Toast.LENGTH_LONG).show();
    }
    //读取当前经纬度
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
