package com.example.carpoolgl.bean;

public class payRequestInfo {
    private String CheckSeq;//支付订单编号   本地不能设置 服务端可以
    private String PassenSeq;//用户编号
    private String DriverSeq;//司机编号
    private Integer PayBal;//支付金额
    private String PayCon;//支付状态

    public payRequestInfo(String passenSeq, String driverSeq, Integer payBal) {
        PassenSeq = passenSeq;
        DriverSeq = driverSeq;
        PayBal = payBal;
    }

    public String getCheckSeq() {
        return CheckSeq;
    }

    public void setCheckSeq(String checkSeq) {
        CheckSeq = checkSeq;
    }

    public String getPayCon() {
        return PayCon;
    }

    public void setPayCon(String payCon) {
        PayCon = payCon;
    }

    public String getPassenSeq() {
        return PassenSeq;
    }

    public void setPassenSeq(String passenSeq) {
        PassenSeq = passenSeq;
    }

    public String getDriverSeq() {
        return DriverSeq;
    }

    public void setDriverSeq(String driverSeq) {
        DriverSeq = driverSeq;
    }

    public Integer getPayBal() {
        return PayBal;
    }

    public void setPayBal(Integer payBal) {
        PayBal = payBal;
    }

    @Override
    public String toString() {
        return "payRequestInfo{" +
                "PassenSeq='" + PassenSeq + '\'' +
                ", DriverSeq='" + DriverSeq + '\'' +
                ", PayBal=" + PayBal +
                '}';
    }
}
