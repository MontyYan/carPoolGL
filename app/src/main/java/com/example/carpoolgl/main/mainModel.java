package com.example.carpoolgl.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.example.carpoolgl.Static.STATIC_CLASS;
import com.example.carpoolgl.Static.STATIC_USERINFO;
import com.example.carpoolgl.base.activity.baseModel;
import com.example.carpoolgl.bean.RelOrder;
import com.example.carpoolgl.dataBase.MydbHelper;
/*
* 每次打开软件时，对用户信息进行初始化
*   若已经登录过，则显示登录后的用户信息
*   若没有登录，则显示，默认未登录信息
* */
public class mainModel extends baseModel {

    /*
    * 判断用户是否已经登录过
    * */
    public void findUInfo(Context context,mainView mainV){
        SharedPreferences shapre = context.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
        STATIC_USERINFO.setCon(shapre.getInt("conCode",0)); //0：没有用户信息---用户没有登录
        if(STATIC_USERINFO.getCon().equals(1)){                             //1：用户已经登录
            STATIC_USERINFO.setPhone(shapre.getString("phone",""));
            STATIC_USERINFO.setRegisterDate(shapre.getString("registerDate","2000-1-1"));
            STATIC_USERINFO.setUserSeq(shapre.getString("sequence",""));
            STATIC_USERINFO.setName(shapre.getString("sequence",""));
            STATIC_USERINFO.carSeq = shapre.getString("carSeq","");
            Log.i("MainActivity",STATIC_USERINFO.getCon()+"");
            Log.i("MainActivity",STATIC_USERINFO.getPhone());
            Log.i("MainActivity",STATIC_USERINFO.getRegisterDate());
            Log.i("SetUInfo",STATIC_USERINFO.getUserSeq());
            mainV.SetUInfo();
        }
    }

    /*
     * 查找用户是否已经发布订单，
     *   如果已经发布订单，则将订单信息显示在主页，且不允许再次发布订单，直到订单完成
     * */
//    public void findOrder(Context context,mainView mainV){
//        SharedPreferences shapre = context.getSharedPreferences("orderinfo",Context.MODE_PRIVATE);
//        Integer result = shapre.getInt("orderConCode",0);
//        if(result.equals(1)){
//            String orderJson = shapre.getString("orderJson","").replaceAll("&quot;","");
//            RelOrder order = JSONObject.parseObject(orderJson,RelOrder.class);//relorder Json格式映射
//            mainV.SetOrder(order);
//        }
//    }

    public void findOrder(Context context, mainView mainV){
        SharedPreferences shapre = context.getSharedPreferences("dr_orderinfo",Context.MODE_PRIVATE);
        Integer DrResult = shapre.getInt("orderConCode",0);
        shapre = context.getSharedPreferences("pa_orderinfo",Context.MODE_PRIVATE);
        Integer PaResult = shapre.getInt("orderConCode",0);
        String ExistDrseq = shapre.getString("carSeq","null");

        if(DrResult.equals(0)&&PaResult.equals(1)){//乘客身份发布订单，司机身份没有发布订单
            mainV.SetCon(1);
        }else if(DrResult.equals(1)&&PaResult.equals(0)){//司机身份发布订单，乘客身份没有发布订单
            mainV.SetCon(2);
        }else{
            if(ExistDrseq.equals("null")){  //证明没有加入司机身份
                mainV.SetCon(4);//
            }else{
                /*
                 * 1.两个身份都没有发布订单，可以直接切换身份
                 * 2.两个身份都发布了订单，这种情况不存在，测试的时候用toast提示
                 *
                 * 默认显示在乘客界面
                 * */
                mainV.SetCon(3);
            }


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
