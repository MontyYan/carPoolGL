package com.example.carpoolgl.bean;


import android.os.Parcel;
import android.os.Parcelable;

/*
* 用于存储订单记录信息
*   将数据库信息读取出进行还原的Bean
* */
public class RecordInfo implements Parcelable {
    private String orderSeq;//订单编号
    private String startLoc;//起点地址
    private String endLoc;//终点地址
    private String time;//订单时间
    private Integer money;//价格， 对于司机来是总收入，对于乘客来说是支付金额
    private String startLatLon;//起点经纬度
    private String endLatLon;//终点经纬度
    private String routeJson;//线路json，可以还原成driverPath
    private String SpecialJson;//司机与乘客的各自信息部分
    private Integer identity;//0：乘客，1：司机

    public RecordInfo(){
    }


    protected RecordInfo(Parcel in) {
        orderSeq = in.readString();
        startLoc = in.readString();
        endLoc = in.readString();
        time = in.readString();
        if (in.readByte() == 0) {
            money = null;
        } else {
            money = in.readInt();
        }
        startLatLon = in.readString();
        endLatLon = in.readString();
        routeJson = in.readString();
        SpecialJson = in.readString();
        if (in.readByte() == 0) {
            identity = null;
        } else {
            identity = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orderSeq);
        dest.writeString(startLoc);
        dest.writeString(endLoc);
        dest.writeString(time);
        if (money == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(money);
        }
        dest.writeString(startLatLon);
        dest.writeString(endLatLon);
        dest.writeString(routeJson);
        dest.writeString(SpecialJson);
        if (identity == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(identity);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RecordInfo> CREATOR = new Creator<RecordInfo>() {
        @Override
        public RecordInfo createFromParcel(Parcel in) {
            return new RecordInfo(in);
        }

        @Override
        public RecordInfo[] newArray(int size) {
            return new RecordInfo[size];
        }
    };

    public String getStartLatLon() {
        return startLatLon;
    }

    public void setStartLatLon(String startLatLon) {
        this.startLatLon = startLatLon;
    }

    public String getEndLatLon() {
        return endLatLon;
    }

    public void setEndLatLon(String endLatLon) {
        this.endLatLon = endLatLon;
    }

    public Integer getIdentity() {
        return identity;
    }

    public void setIdentity(Integer identity) {
        this.identity = identity;
    }

    public String getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(String orderSeq) {
        this.orderSeq = orderSeq;
    }

    public String getStartLoc() {
        return startLoc;
    }

    public void setStartLoc(String startLoc) {
        this.startLoc = startLoc;
    }

    public String getEndLoc() {
        return endLoc;
    }

    public void setEndLoc(String endLoc) {
        this.endLoc = endLoc;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public String getRouteJson() {
        return routeJson;
    }

    public void setRouteJson(String routeJson) {
        this.routeJson = routeJson;
    }

    public String getSpecialJson() {
        return SpecialJson;
    }

    public void setSpecialJson(String specialJson) {
        SpecialJson = specialJson;
    }

    @Override
    public String toString() {
        return "RecordInfo{" +
                "orderSeq='" + orderSeq + '\'' +
                ", startLoc='" + startLoc + '\'' +
                ", endLoc='" + endLoc + '\'' +
                ", time='" + time + '\'' +
                ", money=" + money +
                ", routeJson='" + "此处为routeJson" + '\'' +
                ", SpecialJson='" + SpecialJson + '\'' +
                '}';
    }
}
