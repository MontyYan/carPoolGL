package com.example.carpoolgl.dataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MydbHelper extends SQLiteOpenHelper {
    public static final String CREATE_TABLE="create table user_info ("
            +"phone char(11) primary key,"
            +"sequence char(10),"
            +"password nvarchar(11),"
            +"login_ways Integer,"
            +"register_date date,"
            +"sex char(2),"
            +"identity nvarchar(4),"
            +"emergency_phone nvarchar(11),"
            +"name nvarchar(18),"
            +"identity_id nvarchar(10)"
            +")";

    private Context mContext;

    public MydbHelper(Context context,String name,SQLiteDatabase.CursorFactory factory,int version){
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
        db.execSQL("drop table if exists user_info");
        onCreate(db);
    }
}
