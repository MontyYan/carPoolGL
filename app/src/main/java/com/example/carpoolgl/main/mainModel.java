package com.example.carpoolgl.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.carpoolgl.Static.STATIC_CLASS;
import com.example.carpoolgl.Static.STATIC_USERINFO;
import com.example.carpoolgl.dataBase.MydbHelper;

public class mainModel {

    public void findUInfo(Context context,mainView mainV){
        SharedPreferences shapre = context.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
        STATIC_USERINFO.setCon(shapre.getInt("conCode",0));
        if(STATIC_USERINFO.getCon().equals(1)){
            STATIC_USERINFO.setPhone(shapre.getString("phone",""));
            STATIC_USERINFO.setRegisterDate(shapre.getString("registerDate","2000-1-1"));
            STATIC_USERINFO.setUserSeq(shapre.getString("sequence",""));
            Log.i("MainActivity",STATIC_USERINFO.getCon()+"");
            Log.i("MainActivity",STATIC_USERINFO.getPhone());
            Log.i("MainActivity",STATIC_USERINFO.getRegisterDate());
            Log.i("MainActivity",STATIC_USERINFO.getUserSeq());
            mainV.SetUInfo();
        }
    }

    public void _findUInfo(Context context,mainView mainV){
        MydbHelper mydbHelper = new MydbHelper(context,"user.db",null, STATIC_CLASS.getVersion()+1);
        STATIC_CLASS.setVersion(STATIC_CLASS.getVersion()+1);
        Log.i("LoginActivity",STATIC_CLASS.getVersion()+" model");
        SQLiteDatabase db = mydbHelper.getWritableDatabase();
        try{
//            db = mydbHelper.getWritableDatabase();
            db.beginTransaction();
            Cursor cursor = db.query("user_info",null,null,null,null,null,null);
            if(cursor.moveToFirst()){
                String seq = cursor.getString(cursor.getColumnIndex("sequence"));
                Log.i("MainActivity",seq);
                STATIC_USERINFO.setUserSeq(seq);
                if(!seq.equals(null)){
                    STATIC_USERINFO.setCon(1);//代表已经登录过
                    STATIC_USERINFO.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
                    STATIC_USERINFO.setRegisterDate(cursor.getString(cursor.getColumnIndex("sequence")));
//                    STATIC_USERINFO.setIdentity(cursor.getString(cursor.getColumnIndex("identity")));
                    mainV.SetUInfo();
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
        }
    }
}
