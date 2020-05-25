package com.example.carpoolgl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import com.example.carpoolgl.bean.RecordInfo;
import com.example.carpoolgl.dataBase.orderRecord.RecordHelper;
import com.example.carpoolgl.recyclerView.recordRecycAdapter;
import java.util.ArrayList;
import java.util.List;


public class RecordActivity extends AppCompatActivity {

    private static final String TAG="RecordActivity";

    private List<RecordInfo> recordList;

    private RecyclerView rec_recyc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        initController();
        setRecyclerview();

    }

    public void initController(){
        rec_recyc = findViewById(R.id.rec_orderRecycView);

    }

    public void setRecyclerview(){

        getRecordData();//初始化recordList
//        if(recordList==null){
//            recordList = new ArrayList<>();
//            recordList.add(getNullData());
//        }

        recordRecycAdapter adapter = new recordRecycAdapter(RecordActivity.this,recordList);
        rec_recyc.setAdapter(adapter);
        rec_recyc.setLayoutManager(new LinearLayoutManager(
                RecordActivity.this,
                LinearLayoutManager.VERTICAL, //垂直方向
                false             //非倒序
        ));

        adapter.setOnItemClickListener(new recordRecycAdapter.OrderClickLisener() {
            @Override
            public void OnRouteClick(RecordInfo record) {
                Intent intent = new Intent(RecordActivity.this,RecordDetailActivity.class);
                intent.putExtra("RecordInfo",record);
                startActivity(intent);
            }
        });

    }

    /*
    * 读取数据库数据
    * */
    public void getRecordData(){
        RecordHelper helper = new RecordHelper(this,"record.db",null,1);
        SQLiteDatabase db = helper.getWritableDatabase();

        try{
            db.beginTransaction();
            Cursor cursor = db.query("record_info",null,null,null,null,null,null);
            recordList = new ArrayList<>();
            if(cursor.moveToFirst()){
                do{
                    RecordInfo recordInfo = new RecordInfo();
                    String seq = cursor.getString(cursor.getColumnIndex("seq"));
                    String start = cursor.getString(cursor.getColumnIndex("startLoc"));
                    String end = cursor.getString(cursor.getColumnIndex("endLoc"));
                    String time = cursor.getString(cursor.getColumnIndex("time"));
                    Integer money = cursor.getInt(cursor.getColumnIndex("money"));
                    String startLatLon = cursor.getString(cursor.getColumnIndex("startLatLon"));
                    String endLatLon = cursor.getString(cursor.getColumnIndex("endLatLon"));
                    String routeJson = cursor.getString(cursor.getColumnIndex("routeJson"));
                    String SpecialJson = cursor.getString(cursor.getColumnIndex("special"));
                    Integer identity = cursor.getInt(cursor.getColumnIndex("identity"));

                    recordInfo.setOrderSeq(seq);
                    recordInfo.setStartLoc(start);
                    recordInfo.setEndLoc(end);
                    recordInfo.setTime(time);
                    recordInfo.setMoney(money);
                    recordInfo.setStartLatLon(startLatLon);
                    recordInfo.setEndLatLon(endLatLon);
                    recordInfo.setRouteJson(routeJson);
                    recordInfo.setSpecialJson(SpecialJson);
                    recordInfo.setIdentity(identity);

                    Log.i(TAG,recordInfo.toString());
                    recordList.add(recordInfo);
                }while (cursor.moveToNext());

            }
            cursor.close();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
        }

    }

    public RecordInfo getNullData(){
        RecordInfo Info = new RecordInfo();
        Info.setOrderSeq("null");
        Info.setStartLoc("null");
        Info.setEndLoc("null");
        Info.setTime("null");
        Info.setMoney(0);
        Info.setRouteJson("null");
        Info.setSpecialJson("null");
        Info.setIdentity(0);

       return Info;
    }
}
