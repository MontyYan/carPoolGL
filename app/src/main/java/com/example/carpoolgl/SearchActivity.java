package com.example.carpoolgl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.MapView;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.example.carpoolgl.dragLoc.chooseLocPresenter;
import com.example.carpoolgl.dragLoc.chooseLocView;
import com.example.carpoolgl.recyclerView.SearchRecycAdapter;
import com.example.carpoolgl.util.ToastUtil;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements
        View.OnClickListener,
        chooseLocView,
        TextWatcher,
        Inputtips.InputtipsListener,
        View.OnFocusChangeListener

{
    private static final String TAG="SearchActivity";
    private View bottomSheet;
    private BottomSheetBehavior mBoSheetBehavior;
    private EditText getOn_Edit;
    private EditText getOff_Edit;
    private Button ensure_Loc_bt;
    private ImageView back_img_view;
    private TextView choose_Tv;
    private MapView mapView;
    private chooseLocPresenter chooseLocP=null;

    private boolean editSelect;
    private String nowlocation;
    private String startAddress;
    private String endAddress;

    private RecyclerView recyc;
    private ArrayList<String> datas;//初步数据集合，后续替代掉
    private SearchRecycAdapter recycAdapter;

    //search
    private String city = "苏州";
    private LatLonPoint mStartPoint = new LatLonPoint(39.995576,116.481288);
    private LatLonPoint mEndPoint = new LatLonPoint(39.995576,116.481288);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initFvb();
        Intent intent = getIntent();
        editSelect = intent.getBooleanExtra("Edit_select",false);
        nowlocation = intent.getStringExtra("nowLocation");
        mStartPoint = intent.getParcelableExtra("nowLatLon");
        Log.i(TAG,mStartPoint.toString());
        //startAddress保存从MainActivity传来的地址数据，用于传给DetailActivity
        startAddress = nowlocation;
        getOn_Edit.setText(nowlocation);
        mBoSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBoSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        mBoSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        mapView.onCreate(savedInstanceState);
        chooseLocP = new chooseLocPresenter(mapView);
//        chooseLocP = new chooseLocPresenter(getApplicationContext(),mapView,getScreenWH());
//        chooseLocP.attachView(this);
        //initDatas();
        //initRecyclerview();
    }

    //**TextWatcher*****************************************************************************************************************************
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String newText = s.toString().trim();
        InputtipsQuery inputquery = new InputtipsQuery(newText,city);
        inputquery.setCityLimit(true);
        Inputtips inputTips = new Inputtips(SearchActivity.this,inputquery);
        inputTips.setInputtipsListener(this);
        inputTips.requestInputtipsAsyn();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
    //**Inputtips.InputtipsListener****************************************************************************************************************
    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            Log.i("rCode?",rCode+"");
            List<HashMap<String, String>> listString = new ArrayList<HashMap<String, String>>();
            if(tipList != null) {
                Log.i("tipList=null?",tipList+"");
                int size = tipList.size();
                for (int i = 0; i < size; i++) {
                    Tip tip = tipList.get(i);
                    if(tip != null) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        String[] area = tipList.get(i).getDistrict().split("市");//姑苏区
                        String address = tipList.get(i).getName();//杨枝二村苏大家属区31幢
                        LatLonPoint mpoint = tipList.get(i).getPoint();
//                        map.put("name", tipList.get(i).getName());
                        map.put("name", area[1]);
//                        map.put("address", tipList.get(i).getDistrict());
                        map.put("address", address);
                        map.put("lat",mpoint.getLatitude()+"");
                        map.put("lon",mpoint.getLongitude()+"");
                        Log.i("getName",tipList.get(i).getName());//杨枝二村苏大家属区31幢
                        Log.i("getAddress",tipList.get(i).getAddress());//葑门西街东150米
                        Log.i("toString",tipList.get(i).toString());//杨枝二村苏大家属区23幢 district:江苏省苏州市姑苏区 adcode:320508
                        Log.i("getDistrict",tipList.get(i).getDistrict());//江苏省苏州市姑苏区
                        Log.i("latlon",mpoint.getLatitude()+" "+mpoint.getLongitude());//
                        listString.add(map);
//                        Log.i("map_name_add",tipList.get(i).getName()+" "+tipList.get(i).getDistrict());
                    }
                }
                recycAdapter = new SearchRecycAdapter(SearchActivity.this,listString);
                recyc.setAdapter(recycAdapter);
                recyc.setLayoutManager(new LinearLayoutManager(
                        SearchActivity.this,
                        LinearLayoutManager.VERTICAL,
                        false
                ));
                recycAdapter.setOnItemClickListener(new SearchRecycAdapter.OnItemClickListener() {
                    @Override
                    public void OnItemclick(View view, String data,double lat,double lon) {
                        String[] da = data.split(" ");
//                        startAddress = data;
                        LocInfo_SetText(data);
                        setLatLon(lon,lat);
                        if(editSelect){
                            startDetailActi();
                        }
                        Toast.makeText(SearchActivity.this,"data="+da[0]+" "+da[1],Toast.LENGTH_SHORT).show();
                    }
                });

                recycAdapter.notifyDataSetChanged();
            }
        } else {
            ToastUtil.showerror(this.getApplicationContext(), rCode);
        }
    }

    //**recyclerview****************************************************************************************************************************
    public void initRecyclerview(){
        recycAdapter = new SearchRecycAdapter(SearchActivity.this,datas);
        recyc.setAdapter(recycAdapter);
        //layoutManager
        recyc.setLayoutManager(new LinearLayoutManager(
                SearchActivity.this,
                LinearLayoutManager.VERTICAL, //垂直方向
                false             //非倒序
                ));
        recycAdapter.setOnItemClickListener(new SearchRecycAdapter.OnItemClickListener() {
            @Override
            public void OnItemclick(View view, String data,double lat,double lon) {
                Toast.makeText(SearchActivity.this,"data="+data,Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initDatas(){
        datas = new ArrayList<>();
        for(int i=0;i<100;i++){
            datas.add("content"+i);
        }
    }
    //*****************************************************************************************************************************************

    public void initFvb(){
        mapView = findViewById(R.id.map);
        getOn_Edit = findViewById(R.id.getOn_edit);
//        getOn_Edit.setOnClickListener(this);
        getOn_Edit.setOnFocusChangeListener(this);
        getOn_Edit.addTextChangedListener(this);
        getOff_Edit = findViewById(R.id.getOff_edit);
//        getOff_Edit.setOnClickListener(this);
        getOff_Edit.setOnFocusChangeListener(this);
        getOff_Edit.addTextChangedListener(this);
        ensure_Loc_bt = findViewById(R.id.ensure_loc_bt);
        ensure_Loc_bt.setOnClickListener(this);
        back_img_view = findViewById(R.id.back_img);
        back_img_view.setOnClickListener(this);
        choose_Tv = findViewById(R.id.drawer_loc);
        choose_Tv.setOnClickListener(this);
        bottomSheet = findViewById(R.id.bottom_sheet);
        mapView = findViewById(R.id.map);
        recyc = findViewById(R.id.searchRecycView);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()){
            case R.id.getOn_edit:
                if(hasFocus){
                    Log.i(TAG,"get_on取到焦点 ");
                    mBoSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    getOn_Edit.setText("");
                    editSelect = false;
                }

                break;
            case R.id.getOff_edit:
                if(hasFocus){
                    Log.i(TAG,"get_on取到焦点 ");
                    mBoSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    getOff_Edit.setText("");
                    editSelect = true;
                }
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.getOn_edit:
//                mBoSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                getOn_Edit.setText("");
//                editSelect = false;
//                break;
//            case R.id.getOff_edit:
//                mBoSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                getOff_Edit.setText("");
//                editSelect = true;
//                break;
            case R.id.drawer_loc:
                mBoSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                chooseLocP.initChooseLocPresenter(getApplicationContext(),getScreenWH());
                chooseLocP.attachView(this);
                break;
            case R.id.ensure_loc_bt:
                mBoSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                if(editSelect){
                    startDetailActi();
                }
                break;
            case R.id.back_img:
                finish();
                break;
        }
    }

    public int[] getScreenWH(){
        Display display = getWindowManager().getDefaultDisplay();
        Point outsize = new Point();
        display.getSize(outsize);
        int[] ScreenWh={outsize.x,outsize.y};
        return ScreenWh;
    }
    //销毁
    @Override
    protected void onDestroy() {
        super.onDestroy();
        chooseLocP.destory();
        chooseLocP.detachView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        chooseLocP.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        chooseLocP.pause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        chooseLocP.saveInstanceState(outState);
    }

    //选择地点信息指定的编辑框，起点/终点
    @Override
    public void LocInfo_SetText(String s) {
        EditText edit = null;
        if(editSelect){ //true为getoff
            edit = getOff_Edit;
            //拖动，赋值终点地址
            endAddress = s; //
        }else{
            edit = getOn_Edit;
            //拖动，地址发生变化，新地址覆盖从MainActivity传来的旧地址
            startAddress = s;
        }
        textShowWays(s,edit);
        Log.e("LocInfo_SetText",s);
    }

    @Override
    public void setLatLon(double lon, double lat) {
        if(editSelect){
            mEndPoint.setLongitude(lon);
            mEndPoint.setLatitude(lat);
        }else {
            mStartPoint.setLongitude(lon);
            mStartPoint.setLatitude(lat);
        }
    }

    //在Edit中，信息显示方式，长度超过18的部分用...代替
    public void textShowWays(String s,EditText edit){
        if(s.length()>18){
            edit.setText(s.substring(0,18)+"...");
        }else{
            edit.setText(s);
        }
    }
    //进入DetailAactivity
    public void startDetailActi(){
        Intent intent = new Intent(SearchActivity.this,DetailActivity.class);
        intent.putExtra("startAddress",startAddress);
        intent.putExtra("endAddress",endAddress);
        intent.putExtra("startLatLon",mStartPoint);
        intent.putExtra("endLatLon",mEndPoint);
        startActivity(intent);
    }


}
