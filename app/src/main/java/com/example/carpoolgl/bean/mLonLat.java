package com.example.carpoolgl.bean;


import android.os.Parcel;
import android.os.Parcelable;

/*
* 经纬度类，用于存储精度，纬度
* */
public class mLonLat implements Parcelable {
    private double lon;
    private double lat;

    public mLonLat(){

    }

    public mLonLat(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    protected mLonLat(Parcel in) {
        lon = in.readDouble();
        lat = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(lon);
        dest.writeDouble(lat);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<mLonLat> CREATOR = new Creator<mLonLat>() {
        @Override
        public mLonLat createFromParcel(Parcel in) {
            return new mLonLat(in);
        }

        @Override
        public mLonLat[] newArray(int size) {
            return new mLonLat[size];
        }
    };

    public double getLongitude() {
        return this.lon;
    }

    public void setLongitude(double var1) {
        this.lon = var1;
    }

    public double getLatitude() {
        return this.lat;
    }

    public void setLatitude(double var1) {
        this.lat = var1;
    }
}
