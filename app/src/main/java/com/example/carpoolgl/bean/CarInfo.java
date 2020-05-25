package com.example.carpoolgl.bean;

import androidx.cardview.widget.CardView;

public class CarInfo {
    private String UserSeq;
    private String CarNuM;
    private String CarBrand;
    private String CarColor;
    private Integer CarSeatNum;

    public CarInfo(){
    }

    public String getUserSeq() {
        return UserSeq;
    }

    public void setUserSeq(String userSeq) {
        UserSeq = userSeq;
    }

    public String getCarNuM() {
        return CarNuM;
    }

    public void setCarNuM(String carNuM) {
        CarNuM = carNuM;
    }

    public String getCarBrand() {
        return CarBrand;
    }

    public void setCarBrand(String carBrand) {
        CarBrand = carBrand;
    }

    public String getCarColor() {
        return CarColor;
    }

    public void setCarColor(String carColor) {
        CarColor = carColor;
    }

    public Integer getCarSeatNum() {
        return CarSeatNum;
    }

    public void setCarSeatNum(Integer carSeatNum) {
        CarSeatNum = carSeatNum;
    }

    @Override
    public String toString() {
        return "CarInfo{" +
                "CarNuM='" + CarNuM + '\'' +
                ", CarBrand='" + CarBrand + '\'' +
                ", CarColor='" + CarColor + '\'' +
                ", CarSeatNum='" + CarSeatNum + '\'' +
                '}';
    }
}
