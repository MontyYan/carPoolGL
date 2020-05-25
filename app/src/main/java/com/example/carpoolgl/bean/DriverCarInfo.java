package com.example.carpoolgl.bean;

/**
 * @author: MontyYan
 * @date: 2020/5/14
 * @description:
 */

public class DriverCarInfo {
    private String OrderSeq;        //订单编号
    private Integer SingleMoney;    //订单单价
    private Integer passenNum;  //乘客数量
    private Integer SeatNum;    //座位数量
    private String DriverSeq;    //座位数量
    private String DriverName;  //司机姓名
    private String DriverPhone; //司机手机号
    private String CarBrand;    //车辆型号
    private String CarColor;    //车辆颜色
    private String CarNum;      //车量座位数量
    private Integer OrderCon;   //订单状态
    private Integer AppreMoney; //感谢费用
    private String PassenSeq;   //乘客编号

    public String getDriverSeq() {
        return DriverSeq;
    }

    public void setDriverSeq(String driverSeq) {
        DriverSeq = driverSeq;
    }

    public String getOrderSeq() {
        return OrderSeq;
    }

    public void setOrderSeq(String orderSeq) {
        OrderSeq = orderSeq;
    }

    public Integer getAppreMoney() {
        return AppreMoney;
    }

    public void setAppreMoney(Integer appreMoney) {
        AppreMoney = appreMoney;
    }

    public String getPassenSeq() {
        return PassenSeq;
    }

    public void setPassenSeq(String passenSeq) {
        PassenSeq = passenSeq;
    }

    public Integer getSingleMoney() {
        return SingleMoney;
    }

    public void setSingleMoney(Integer singleMoney) {
        SingleMoney = singleMoney;
    }

    public Integer getPassenNum() {
        return passenNum;
    }

    public void setPassenNum(Integer passenNum) {
        this.passenNum = passenNum;
    }

    public Integer getSeatNum() {
        return SeatNum;
    }

    public void setSeatNum(Integer seatNum) {
        SeatNum = seatNum;
    }

    public String getDriverName() {
        return DriverName;
    }

    public void setDriverName(String driverName) {
        DriverName = driverName;
    }

    public String getDriverPhone() {
        return DriverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        DriverPhone = driverPhone;
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

    public String getCarNum() {
        return CarNum;
    }

    public void setCarNum(String carNum) {
        CarNum = carNum;
    }

    public Integer getOrderCon() {
        return OrderCon;
    }

    public void setOrderCon(Integer orderCon) {
        OrderCon = orderCon;
    }

    @Override
    public String toString() {
        return "DriverCarInfo{" +
                "OrderSeq='" + OrderSeq + '\'' +
                ", SingleMoney=" + SingleMoney +
                ", passenNum=" + passenNum +
                ", SeatNum=" + SeatNum +
                ", DriverName='" + DriverName + '\'' +
                ", DriverPhone='" + DriverPhone + '\'' +
                ", CarBrand='" + CarBrand + '\'' +
                ", CarColor='" + CarColor + '\'' +
                ", CarNum='" + CarNum + '\'' +
                ", OrderCon=" + OrderCon +
                ", AppreMoney=" + AppreMoney +
                ", PassenSeq='" + PassenSeq + '\'' +
                '}';
    }
}
