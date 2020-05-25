package com.example.carpoolgl.bean;

public class PayOrder {
    private String payOrderSeq;
    private String PassenSeq;
    private String DriverSeq;
    private Integer Payment;
    private String Password;

    public PayOrder(){
    }

    public PayOrder(String var1,String var2,String var3,Integer var4,String var5){
        this.payOrderSeq = var1;
        this.PassenSeq = var2;
        this.DriverSeq = var3;
        this.Payment = var4;
        this.Password = var5;
    }

    public String getOrderSeq() {
        return payOrderSeq;
    }

    public void setOrderSeq(String orderSeq) {
        payOrderSeq = orderSeq;
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

    public Integer getPayment() {
        return Payment;
    }

    public void setPayment(Integer payment) {
        this.Payment = payment;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    @Override
    public String toString() {
        return "PayOrder{" +
                "payOrderSeq='" + payOrderSeq + '\'' +
                ", PassenSeq='" + PassenSeq + '\'' +
                ", DriverSeq='" + DriverSeq + '\'' +
                ", Payment=" + Payment +
                ", Password='" + Password + '\'' +
                '}';
    }
}
