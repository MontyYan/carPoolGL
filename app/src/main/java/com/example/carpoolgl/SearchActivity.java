package com.example.carpoolgl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.MapView;
import com.amap.api.services.core.AMapException;
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
        Inputtips.InputtipsListener

{

    private View bottomSheet;
    private BottomSheetBehavior mBoSheetBehavior;
    private EditText getOn_Edit;
    private EditText getOff_Edit;
    private Button ensure_Loc_bt;
    private ImageView back_img_view;
    private TextView choose_Tv;
    private MapView mapView;
    private chooseLocPresenter chooseLocP;

    private boolean editSelect;
    private String nowlocation;

    private RecyclerView recyc;
    private ArrayList<String> datas;//初步数据集合，后续替代掉
    private SearchRecycAdapter recycAdapter;

    //search
    private String city = "苏州";
    private ListView minputlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initFvb();
        Intent intent = getIntent();
        editSelect = intent.getBooleanExtra("Edit_select",false);
        nowlocation = intent.getStringExtra("nowLocation");
        getOn_Edit.setText(nowlocation);
        mBoSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBoSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        mBoSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                bottomSheet.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int action = MotionEventCompat.getActionMasked(event);
                        switch (action){
                            case MotionEvent.ACTION_DOWN:
                                return false;
                                default:
                                    return true;
                        }
                    }
                });
            }
        });

        mapView.onCreate(savedInstanceState);
        chooseLocP = new chooseLocPresenter(getApplicationContext(),mapView,getScreenWH());
        chooseLocP.attachView(this);
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
                        map.put("name", tipList.get(i).getName());
                        map.put("address", tipList.get(i).getDistrict());
                        listString.add(map);
                        Log.i("map_name_add",tipList.get(i).getName()+" "+tipList.get(i).getDistrict());
                    }
                }

                recycAdapter = new SearchRecycAdapter(SearchActivity.this,listString);

//                SimpleAdapter aAdapter = new SimpleAdapter(getApplicationContext(), listString, R.layout.item_search_recyc,
//                        new String[]{"name", "address"}, new int[]{R.id.tv_title, R.id.tv_content});

                recyc.setAdapter(recycAdapter);
                recyc.setLayoutManager(new LinearLayoutManager(
                        SearchActivity.this,
                        LinearLayoutManager.VERTICAL,
                        false
                ));
                recycAdapter.setOnItemClickListener(new SearchRecycAdapter.OnItemClickListener() {
                    @Override
                    public void OnItemclick(View view, String data) {
                        Toast.makeText(SearchActivity.this,"data="+data,Toast.LENGTH_SHORT).show();
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
            public void OnItemclick(View view, String data) {
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
        getOn_Edit.setOnClickListener(this);
        getOn_Edit.addTextChangedListener(this);
        getOff_Edit = findViewById(R.id.getOff_edit);
        getOff_Edit.setOnClickListener(this);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.getOn_edit:
                mBoSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                editSelect = false;
                break;
            case R.id.getOff_edit:
                mBoSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                editSelect = true;
                break;
            case R.id.drawer_loc:
                mBoSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.ensure_loc_bt:
                mBoSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                if(editSelect){
                    Intent intent = new Intent(SearchActivity.this,DetailActivity.class);
                    startActivity(intent);
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
        }else{
            edit = getOn_Edit;
        }
        textShowWays(s,edit);
        Log.e("LocInfo_SetText",s);
    }
    //在Edit中，信息显示方式，长度超过18的部分用...代替
    public void textShowWays(String s,EditText edit){
        if(s.length()>18){
            edit.setText(s.substring(0,18)+"...");
        }else{
            edit.setText(s);
        }
    }

}
