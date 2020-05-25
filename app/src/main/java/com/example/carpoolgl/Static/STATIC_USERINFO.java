package com.example.carpoolgl.Static;

import java.util.Date;

/*
* 从本地读取用户数据，以全局静态数据方式存储
* 若未登录，即本地无数据状态，显示默认数据
*
* */
public class STATIC_USERINFO {
    private static Integer conCode=0;//登录状态 0:未登录，1:已登录，作为判断标识
    private static String phone="未登录";
    private static String userSeq="";
    private static String registerDate="";
    private static String identity="未知";
    private static String emergencyPhone="";
    private static String name="";
    private static String identityId="";

    public static String carSeq="";

    public static Integer getCon() {
        return conCode;
    }

    public static void setCon(Integer con) {
        STATIC_USERINFO.conCode = con;
    }
    public static String getEmergencyPhone() {
        return emergencyPhone;
    }

    public static void setEmergencyPhone(String emergencyPhone) {
        STATIC_USERINFO.emergencyPhone = emergencyPhone;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        STATIC_USERINFO.name = name;
    }

    public static String getIdentityId() {
        return identityId;
    }

    public static void setIdentityId(String identityId) {
        STATIC_USERINFO.identityId = identityId;
    }

    public static String getPhone() {
        return phone;
    }

    public static void setPhone(String phone) {
        STATIC_USERINFO.phone = phone;
    }

    public static String getUserSeq() {
        return userSeq;
    }

    public static void setUserSeq(String userSeq) {
        STATIC_USERINFO.userSeq = userSeq;
    }

    public static String getRegisterDate() {
        return registerDate;
    }

    public static void setRegisterDate(String registerDate) {
        STATIC_USERINFO.registerDate = registerDate;
    }

    public static String getIdentity() {
        return identity;
    }

    public static void setIdentity(String identity) {
        STATIC_USERINFO.identity = identity;
    }
}
