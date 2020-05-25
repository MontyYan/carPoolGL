package com.example.carpoolgl.login;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.carpoolgl.LoginActivity;
import com.example.carpoolgl.MainActivity;
import com.example.carpoolgl.Static.STATIC_CLASS;
import com.example.carpoolgl.Static.activityList;
import com.example.carpoolgl.bean.RelOrder;
import com.example.carpoolgl.bean.User;
import com.example.carpoolgl.dataBase.MydbHelper;

import org.w3c.dom.Text;

public class mHandler extends Handler {
    private static final int UPDATA_TV = 0;
    private static final int UPDATA_SP = 1;
    private static final int STATRT_ACT = 5;

//    private static final int LOGIN_PROGRESS = 2;

    private TextView LoginResult_tv;
    private ProgressBar Login_progress;
    private Button Login_button;
    private JSONObject jsonObject;
    private Context context;

    private TextView cutdown_tv;

    public mHandler(){

    }

    //打开软件，检测是否有用户信息
    public mHandler(Context context){
        this.context = context;
    }
    //登录界面，登录状态（旧，已不用）
    public mHandler(TextView textView){
        this.LoginResult_tv = textView;
    }

    public mHandler(ProgressBar Login_progress, Button Login_button){
        this.Login_progress = Login_progress;
        this.Login_button = Login_button;
    }

    //登录界面，登录过程中UI
    public mHandler(TextView textView,ProgressBar Login_progress, Button Login_button){
        this.LoginResult_tv = textView;
        this.Login_progress = Login_progress;
        this.Login_button = Login_button;
    }

    @Override
    public void handleMessage(Message msg){
        switch (msg.what){
            case UPDATA_TV:         //更新UI，登录过程界面UI变化
                String text = (String)msg.obj;
                LoginResult_tv.setText("登录: "+text);
                JSONObject jsonObject = JSONObject.parseObject(text);
                Integer loginCode = jsonObject.getInteger("loginResult");
                if(loginCode.equals(0)){
                    Login_button.setVisibility(View.VISIBLE);
                    Login_progress.setVisibility(View.GONE);
                }else if(loginCode.equals(1)){
                    Login_progress.setVisibility(View.GONE);
                }
                break;
            case UPDATA_SP:         //更新SharedPreferences
                User user = (User)msg.obj;
                saveInfo(user);
//                insert(user);
                break;
            case STATRT_ACT:
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass(context,MainActivity.class);
                activityList.DestoryActivity("phoneActivity");
                context.startActivity(intent);
                break;
//            case LOGIN_PROGRESS:
//
//                break;

        }
    }

    public void saveInfo(User user){
        Log.i("LoginActivity",user.toString());
        SharedPreferences.Editor shaEdit = context.getSharedPreferences("userinfo",Context.MODE_PRIVATE).edit();
        shaEdit.putInt("conCode",1);
        shaEdit.putString("phone",user.getPhone());
        shaEdit.putString("name",user.getName());
        shaEdit.putString("registerDate",user.getRegisterDate()+"");
        shaEdit.putString("sequence",user.getUserSeq());

        shaEdit.putString("carSeq",user.getCarSeq());

        shaEdit.apply();
    }

    //更新数据库
    public void insert(User user){
        Log.i("LoginActivity",user.toString());
        //获取数据库操作对象,开始version为1，每次登录后都加1，用于覆盖原有的user_Info表
        MydbHelper mydbHelper = new MydbHelper(context,"user.db",null, STATIC_CLASS.getVersion());
        STATIC_CLASS.setVersion(STATIC_CLASS.getVersion()+1);
        Log.i("LoginActivity",STATIC_CLASS.getVersion()+" handler");
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
