package com.example.carpoolgl.bean;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * @author: MontyYan
 * @date: 2020/4/5
 * @description:
 */

public class User {


    @SerializedName(value = "seq",alternate="userSeq")
    private String UserSeq;             //PA00000001    用户编号
    @SerializedName(value = "phone",alternate = "Phone")
    private String Phone;               //15200006128   用户手机号码
    @SerializedName(value = "password",alternate = "Password")
    private String Password;            //1234
//    @SerializedName(value = "LoginWays")
    private Integer LoginWays;          //1
//    @SerializedName(value = "RegisterDate")
    private Date RegisterDate;          //2020-2-10  注册日期
//    @SerializedName(value = "Sex")
    private char Sex;                   //f/m   性别
//    @SerializedName(value = "Identity")
    private String Identity;            //学生  身份
//    @SerializedName(value = "phone")
    private String EcyPhone;            //18800006041    紧急电话
//    @SerializedName(value = "phone")
    private String Name;                //*明    姓名
//    @SerializedName(value = "phone")
    private String IdentityId;          //1600300333    学号/工号

    public User(String phone, String password, Integer loginWays) {
        this.Phone = phone;
        this.Password = password;
        this.LoginWays = loginWays;
    }

    public String getUserSeq() {
        return UserSeq;
    }

    public void setUserSeq(String userSeq) {
        UserSeq = userSeq;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        phone = phone;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        password = password;
    }

    public Integer getLoginWays() {
        return LoginWays;
    }

    public void setLoginWays(Integer loginWays) {
        LoginWays = loginWays;
    }

    public Date getRegisterDate() {
        return RegisterDate;
    }

    public void setRegisterDate(Date registerDate) {
        RegisterDate = registerDate;
    }

    public char getSex() {
        return Sex;
    }

    public void setSex(char sex) {
        Sex = sex;
    }

    public String getIdentity() {
        return Identity;
    }

    public void setIdentity(String identity) {
        Identity = identity;
    }

    public String getEcyPhone() {
        return EcyPhone;
    }

    public void setEcyPhone(String ecyPhone) {
        EcyPhone = ecyPhone;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getIdentityId() {
        return IdentityId;
    }

    public void setIdentityId(String identityId) {
        IdentityId = identityId;
    }

    @Override
    public String toString() {
        return "User{" +
                "Seq='" + UserSeq + '\'' +
                ", Phone='" + Phone + '\'' +
                ", Password='" + Password + '\'' +
                ", LoginWays=" + LoginWays +
                ", RegisterDate=" + RegisterDate +
                ", Sex=" + Sex +
                ", Identity='" + Identity + '\'' +
                ", EcyPhone='" + EcyPhone + '\'' +
                ", Name='" + Name + '\'' +
                ", IdentityId='" + IdentityId + '\'' +
                '}';
    }
}
