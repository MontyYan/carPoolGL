package com.example.carpoolgl.bean;

/*
* 发布_乘客信息，数据存储目标表[发布乘客表：rel_pass_mems]
* 表用于存储，某发布订单中的乘客信息，自成一张表单独存储
* 待订单完成后，该表中相应订单的信息存入乘客表中
* */
public class RelPassMems {
    private String RePaMemSeq;      //成员表编号，无用，自增
    private String ReOrSeq;         //发布订单编号
    private String PaSeq;           //乘客编号
    private String Name;           //乘客姓名
    private String Phone;           //乘客手机
    private Integer AprMoney;       //感谢费,想加就加，默认为0
    private Integer TogetherNum;     //单个拼车用户随同人数

    public RelPassMems(String reOrSeq, String paSeq, String name, String phone, Integer aprMoney) {
        ReOrSeq = reOrSeq;
        PaSeq = paSeq;
        Name = name;
        Phone = phone;
        AprMoney = aprMoney;
    }

    public Integer getTogetherNum() {
        return TogetherNum;
    }

    public void setTogetherNum(Integer togetherNum) {
        TogetherNum = togetherNum;
    }

    public RelPassMems() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }


    public String getReOrSeq() {
        return ReOrSeq;
    }

    public void setReOrSeq(String reOrSeq) {
        ReOrSeq = reOrSeq;
    }

    public String getPaSeq() {
        return PaSeq;
    }

    public void setPaSeq(String paSeq) {
        PaSeq = paSeq;
    }

    public Integer getAprMoney() {
        return AprMoney;
    }

    public void setAprMoney(Integer aprMoney) {
        AprMoney = aprMoney;
    }

    @Override
    public String toString() {
        return "RelPassMems{" +
                "RePaMemSeq='" + RePaMemSeq + '\'' +
                ", ReOrSeq='" + ReOrSeq + '\'' +
                ", PaSeq='" + PaSeq + '\'' +
                ", Name='" + Name + '\'' +
                ", Phone='" + Phone + '\'' +
                ", AprMoney=" + AprMoney +
                ", TogetherNum=" + TogetherNum +
                '}';
    }
}
