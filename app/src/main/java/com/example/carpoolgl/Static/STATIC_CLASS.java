package com.example.carpoolgl.Static;

public class STATIC_CLASS {
    private static Integer version=1;

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        STATIC_CLASS.url = url;
    }

//    private static String url = "http://192.168.31.203:8080";
//    private static String url = "http://10.34.24.142:8080";
    private static String url = "http://10.34.33.222:8080";

    public static Integer getVersion() {
        return version;
    }

    public static void setVersion(Integer version) {
        STATIC_CLASS.version = version;
    }

    private static Integer SingleMoney = 10;

    public static Integer getSingleMoney() {
        return SingleMoney;
    }

    public static void setSingleMoney(Integer singleMoney) {
        SingleMoney = singleMoney;
    }


    public Boolean PayOrderSeqExist = false;


}
