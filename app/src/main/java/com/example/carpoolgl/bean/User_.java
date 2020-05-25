package com.example.carpoolgl.bean;


public class User_ {
    private Integer id;
    private String phoneNum;
    private String password;

    public User_(String phoneNum, String password) {
        this.phoneNum = phoneNum;
        this.password = password;
    }

    public User_(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
