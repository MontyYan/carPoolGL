package com.example.carpoolgl.NaviDriving;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.amap.api.maps.AMap;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;
import com.amap.api.services.core.LatLonPoint;
import com.example.carpoolgl.R;
import com.example.carpoolgl.util.RouteUtil;

import java.util.ArrayList;
import java.util.List;

public class GPSNaviActivity extends BaseActivity {

    protected final List<NaviLatLng> startList = new ArrayList<NaviLatLng>();
    protected final List<NaviLatLng> endList = new ArrayList<NaviLatLng>();

    private AMap aMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpsnavi);

        mAMapNaviView = findViewById(R.id.navi_view);
        getRouteInfo();
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);

        AMapNaviViewOptions options = new AMapNaviViewOptions();
        options.setScreenAlwaysBright(false);
        options.setAutoDrawRoute(false);
        mAMapNaviView.setViewOptions(options);

    }

    public void getRouteInfo(){
        Intent intent = getIntent();
        LatLonPoint spoint  = intent.getParcelableExtra("mStartPoint");
        LatLonPoint epoint  = intent.getParcelableExtra("mEndPoint");
        startList.add(RouteUtil.PointToNaviLL(spoint));
        endList.add(RouteUtil.PointToNaviLL(epoint));

    }

    @Override
    public void onInitNaviSuccess() {
        super.onInitNaviSuccess();
        /**
         * 方法: int strategy=mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, multipleroute); 参数:
         *
         * @congestion 躲避拥堵
         * @avoidhightspeed 不走高速
         * @cost 避免收费
         * @hightspeed 高速优先
         * @multipleroute 多路径
         *
         *  说明: 以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
         *  注意: 不走高速与高速优先不能同时为true 高速优先与避免收费不能同时为true
         */
        int strategy = 0;
        try {
            //再次强调，最后一个参数为true时代表多路径，否则代表单路径
            strategy = mAMapNavi.strategyConvert(true, false, false, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAMapNavi.setCarNumber("桂", "DFZ588");
//        mAMapNavi.calculateDriveRoute(sList, eList, mWayPointList, strategy);
        mAMapNavi.calculateDriveRoute(startList, endList, null, strategy);

    }

    @Override
    public void onCalculateRouteSuccess(int[] ids) {
        //开启实时导航
        super.onCalculateRouteSuccess(ids);
        //开启导航
//        mAMapNavi.startNavi(NaviType.GPS);
    }

    @Override
    public void onCalculateRouteSuccess(AMapCalcRouteResult aMapCalcRouteResult) {
        aMap = mAMapNaviView.getMap();

        int[] routIds = aMapCalcRouteResult.getRouteid();
        int routeId = routIds[0];

        AMapNaviPath aMapNaviPath = mAMapNavi.getNaviPaths().get(routeId);

        RouteOverLay routeOverLay = new RouteOverLay(aMap, aMapNaviPath, this);

        routeOverLay.addToMap();

        mAMapNavi.startNavi(NaviType.GPS);
    }
}
