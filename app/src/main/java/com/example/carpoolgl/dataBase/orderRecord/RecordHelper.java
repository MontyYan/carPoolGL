package com.example.carpoolgl.dataBase.orderRecord;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
* 本地订单记录数据库
* */

public class RecordHelper extends SQLiteOpenHelper {
    public static final String CREATE_TABLE="create table record_info ("

            +"id Integer primary key AUTOINCREMENT,"
            +"seq char(14) ,"
            +"startLoc nvarchar(50) ,"
            +"endLoc nvarchar(50) ,"
            +"time date,"
            +"money Integer,"
            +"startLatLon text,"
            +"endLatLon text,"
            +"routeJson text,"
            +"special text,"
            +"identity Integer "
            +")";

    private Context mContext;

    public RecordHelper(Context context,String name,SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
//        Toast.makeText(mContext,"Create successder",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists record_info");
        onCreate(db);
    }
}
