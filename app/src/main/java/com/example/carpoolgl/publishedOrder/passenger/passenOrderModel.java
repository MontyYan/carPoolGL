package com.example.carpoolgl.publishedOrder.passenger;

import android.app.Activity;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.carpoolgl.Static.STATIC_CLASS;
import com.example.carpoolgl.base.activity.baseModel;
import com.example.carpoolgl.bean.DriverCarInfo;
import com.example.carpoolgl.bean.PayOrder;
import com.example.carpoolgl.bean.payRequestInfo;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class passenOrderModel extends baseModel {
    private static final String TAG="passenOrderModel";
    public static final MediaType JSON_=MediaType.parse("application/json; charset=utf-8");
    public static final String findUrl= STATIC_CLASS.getUrl()+"/findNewOrderInfo";
    public static final String requestUrl= STATIC_CLASS.getUrl()+"/payRequest";


    public void getNewOrder(final Activity activity, final String seq, final String passenSeq,final passenOrderView passenOrderV){
        Log.i(TAG,"seq");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    Map<String,String> map  = new HashMap<>();
                    map.put("orderseq",seq);
                    map.put("passenseq",passenSeq);
                    String seqJson = JSONObject.toJSONString(map);
                    Log.i("PostRes",seqJson);
//                    JSONObject jsonObject = PostResponse(findUrl,seqJson,TAG);

                    Log.i(TAG,seqJson);
                    RequestBody requestBody = RequestBody.create(JSON_,seqJson);
                    Request request = new Request.Builder()
                            .url(findUrl)
                            .post(requestBody)
                            .build();

                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string(); //获取返回值
                    Log.i(TAG+"1",responseData);
                    JSONObject jsonObject = JSONObject.parseObject(responseData);
                    Integer result = 0;
                    DriverCarInfo driverCarInfo = new DriverCarInfo();
                    String promMesg = "获取信息失败";

                    if(!jsonObject.isEmpty()){
                        result = jsonObject.getInteger("result");
                        if(result.equals(-1)){
                            promMesg = "获取信息失败";
                        }else if(result.equals(0)){
                            promMesg = "尚未有司机接单";
                        }else if(result.equals(1)){
                            promMesg = "已被成功接单";
                            driverCarInfo = JSONObject.parseObject(jsonObject.getString("driverCarInfo"),DriverCarInfo.class);
                            Log.i(TAG,driverCarInfo.toString());
                        }else if(result.equals(2)){
                            promMesg = "订单已经完成";
                            driverCarInfo = JSONObject.parseObject(jsonObject.getString("driverCarInfo"),DriverCarInfo.class);
                        }
                    }

                    final DriverCarInfo tempDCinfo = driverCarInfo;
                    final Integer tempResult = result;
                    final String tempMesg = promMesg;

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            passenOrderV.setPaOrderInfo(tempDCinfo,tempMesg,tempResult);
                        }
                    });
                }catch (Exception e){
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String resultMes = "获取订单信息失败";
                            passenOrderV.setPaOrderInfo(null,resultMes,0);
                        }
                    });

                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static final String REQUTAG="payInfo";
    /*
    * 支付请求
    * */
    public void payRequest(final Activity activity, final payRequestInfo payReInfo, final passenOrderView passenOrderV){
        /*  [Log.i]  */Log.i("PaOrder--12","进入请求Model层");

        Log.i("payReInfo",payReInfo.toString());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
//                    Map<String,String> map  = new HashMap<>();
//                    map.put("Seq",Seq);
                    payReInfo.setCheckSeq("");
                    payReInfo.setPayCon("");
                    String seqJson = JSON.toJSONString(payReInfo);
                    Log.i(REQUTAG,seqJson);
                    RequestBody requestBody = RequestBody.create(JSON_,seqJson);
                    Request request = new Request.Builder()
                            .url(requestUrl)
                            .post(requestBody)
                            .build();

                    Integer result = 0;
                    String paySeq = "";
                    Response response = client.newCall(request).execute();
                    final String responseData = response.body().string(); //获取返回值
                    /*  [Log.i]  */Log.i("PaOrder--13","服务端返回数据："+responseData);
                    Log.i(REQUTAG,responseData);
                    JSONObject jsonObject = JSONObject.parseObject(responseData);
                    if(!jsonObject.isEmpty()){
                        result = jsonObject.getInteger("result");
                        paySeq = jsonObject.getString("paySeq");
                    }
                    final Integer tempResult = result;
                    final String tempPaySeq = paySeq;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            passenOrderV.setPayDialog(tempResult,tempPaySeq);
                        }
                    });
                }catch (Exception e){
                    //出错
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            passenOrderV.setPayDialog(0,null);
                        }
                    });
                }
            }
        }).start();
    }

    private static final String PAYTAG="payInfo";
    public static final String payUrl= STATIC_CLASS.getUrl()+"/payInfo";
    /*
    * 支付信息
    * */
    public void payInfo(final Activity activity, final PayOrder porder, final passenOrderView passenOrderV){
        /*  [Log.i]  */Log.i("PaOrder--17","密码输入完毕");
           new Thread(new Runnable() {
               @Override
               public void run() {
                   try{
                       OkHttpClient client = new OkHttpClient();
//                       Map<String,String> map  = new HashMap<>();
                       String PayJson = JSONObject.toJSONString(porder);
                       Log.i(PAYTAG,PayJson);
                       RequestBody requestBody = RequestBody.create(JSON_,PayJson);
                       Request request = new Request.Builder()
                               .url(payUrl)
                               .post(requestBody)
                               .build();

                       Response response = client.newCall(request).execute();
                       String responseData = response.body().string();
                       /*  [Log.i]  */Log.i("PaOrder--18","获取服务端返回数据"+responseData);
                       Log.i(PAYTAG,responseData);
                       JSONObject jsonObject = JSONObject.parseObject(responseData);
                       Integer result = 0;
                       String resultTips = "支付失败";
                       if(!jsonObject.isEmpty()){
                           result = jsonObject.getInteger("result");
                           resultTips = jsonObject.getString("resultInfo");
                       }
                       final Integer tempResult = result;
                       final String tempTips = resultTips;

                       activity.runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               passenOrderV.PayingDialog(tempResult,tempTips);
                           }
                       });

                   }catch (Exception e){
                       /*  [Log.i]  */Log.i("PaOrder--19","catch 出错！！！");
                       activity.runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               passenOrderV.PayingDialog(-2,"支付出错");
                           }
                       });
                        e.printStackTrace();
                   }
               }
           }).start();
    }
}
