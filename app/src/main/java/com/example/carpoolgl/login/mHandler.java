package com.example.carpoolgl.login;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.example.carpoolgl.Static.StaticClass;
import com.example.carpoolgl.bean.User;
import com.example.carpoolgl.dataBase.MydbHelper;
import com.google.gson.Gson;

public class mHandler extends Handler {
    private static final int UPDATA_TV = 0;
    private static final int UPDATA_DB = 1;

    TextView LoginResult_tv;

    JSONObject jsonObject;
    Context context;

    public mHandler(){

    }

    public mHandler(Context context){
        this.context = context;
    }

    public mHandler(TextView textView){
        this.LoginResult_tv = textView;
    }

    @Override
    public void handleMessage(Message msg){
        switch (msg.what){
            case UPDATA_TV:
                String text = (String)msg.obj;
                LoginResult_tv.setText("登录: "+text);
                break;
            case UPDATA_DB:
                User user = (User)msg.obj;
                insert(user);
                break;
        }
    }

    //更新数据库
    public void insert(User user){
        Log.i("LoginActivity",user.toString());
        //获取数据库操作对象,开始version为1，每次登录后都加1，用于覆盖原有的user_Info表
        MydbHelper mydbHelper = new MydbHelper(context,"user.db",null, StaticClass.getVersion());
        StaticClass.setVersion(StaticClass.getVersion()+1);
        SQLiteDatabase db=null;
        try{
            db = mydbHelper.getWritableDatabase();
            db.beginTransaction();
            String insertSql = "insert into user_info(phone,sequence,password,login_ways,register_date,sex,identity,emergency_phone,name,identity_id)"
                    +" values (?,?,?,?,?,?,?,?,?,?)";
            db.execSQL(insertSql,
                    new Object[]{user.getPhone(),user.getUserSeq(),user.getPassword(),user.getLoginWays(),user.getRegisterDate(),user.getSex(),user.getIdentity(),user.getEcyPhone(),user.getName(),user.getIdentityId()});
            Log.i("LoginActivity",user.getUserSeq());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
        }

    }

}
