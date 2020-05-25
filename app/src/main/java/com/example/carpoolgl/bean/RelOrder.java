package com.example.carpoolgl.bean;

import android.os.Parcel;
import android.os.Parcelable;


/*
* 发布订单，存储目标表为[发布订单表:rel_orders]
* 所有发布的信息都存在该表中
* */
public class RelOrder implements Parcelable {
    private String ReOrSeq;         //发布订单编号，订单完成后，该编号存入订单表，变成订单编号
    private String DrSeq;           //司机编号
    private String RePaSeq;         //发布者编号     -
    private String StartLoc;        //起点地址名称    -
    private String EndLoc;          //终点地址名称    -
//    private LatLonPoint StartLonLat;    //起点坐标       -
    private String StartLonLat;    //起点坐标       -
//    private LatLonPoint EndLonLat;      //终点坐标          -
    private String EndLonLat;      //终点坐标          -
//    private List<DriveStep> listSteps = new ArrayList<>();
    private String listSteps ;
    private String StartTime;         //出行时间          -
    private Integer PassNum;        //成员数量，指发布者方一共有几个人，（我发布，我和我老婆乘车，共两人）    -
    private Integer money;          //单价
    private Integer totalMoney;     //一共多少钱
    private Integer Condition;      //发布状态，0：等待加入，1：已经接单，-1：取消订单    -
    private String RelPassSeq;



    public RelOrder(){
    }

    public RelOrder(String seq){
        this.RePaSeq = seq;
    }



    protected RelOrder(Parcel in) {
        ReOrSeq = in.readString();
        DrSeq = in.readString();
        RePaSeq = in.readString();
        StartLoc = in.readString();
        EndLoc = in.readString();
        StartLonLat = in.readString();
        EndLonLat = in.readString();
        listSteps = in.readString();
        StartTime = in.readString();
        if (in.readByte() == 0) {
            PassNum = null;
        } else {
            PassNum = in.readInt();
        }
        if (in.readByte() == 0) {
            money = null;
        } else {
            money = in.readInt();
        }
        if (in.readByte() == 0) {
            Condition = null;
        } else {
            Condition = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ReOrSeq);
        dest.writeString(DrSeq);
        dest.writeString(RePaSeq);
        dest.writeString(StartLoc);
        dest.writeString(EndLoc);
        dest.writeString(StartLonLat);
        dest.writeString(EndLonLat);
        dest.writeString(listSteps);
        dest.writeString(StartTime);
        if (PassNum == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(PassNum);
        }
        if (money == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(money);
        }
        if (Condition == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(Condition);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RelOrder> CREATOR = new Creator<RelOrder>() {
        @Override
        public RelOrder createFromParcel(Parcel in) {
            return new RelOrder(in);
        }

        @Override
        public RelOrder[] newArray(int size) {
            return new RelOrder[size];
        }
    };

    public String getReOrSeq() {
        return ReOrSeq;
    }

    public void setReOrSeq(String reOrSeq) {
        ReOrSeq = reOrSeq;
    }
    public String getListSteps() {
        return listSteps;
    }

    public void setListSteps(String listSteps) {
        this.listSteps = listSteps;
    }

    public String getRePaSeq() {
        return RePaSeq;
    }

    public void setRePaSeq(String rePaSeq) {
        RePaSeq = rePaSeq;
    }

    public String getDrSeq() {
        return DrSeq;
    }


    public Integer getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Integer totalMoney) {
        this.totalMoney = totalMoney;
    }


    @Override
    public String toString() {
        return "RelOrder{" +
                "ReOrSeq='" + ReOrSeq + '\'' +
                ", DrSeq='" + DrSeq + '\'' +
                ", RePaSeq='" + RePaSeq + '\'' +
                ", StartLoc='" + StartLoc + '\'' +
                ", EndLoc='" + EndLoc + '\'' +
                ", StartLonLat='" + StartLonLat + '\'' +
                ", EndLonLat='" + EndLonLat + '\'' +
                //", listSteps='" + listSteps + '\'' +
                ", StartTime='" + StartTime + '\'' +
                ", PassNum=" + PassNum +
                ", money=" + money +
                ", totalMoney=" + totalMoney +
                ", Condition=" + Condition +
                ", RelPassSeq='" + RelPassSeq + '\'' +
                '}';
    }

    public void setDrSeq(String drSeq) {
        DrSeq = drSeq;
    }

    public String getStartLoc() {
        return StartLoc;
    }

    public void setStartLoc(String startLoc) {
        StartLoc = startLoc;
    }

    public String getEndLoc() {
        return EndLoc;
    }

    public void setEndLoc(String endLoc) {
        EndLoc = endLoc;
    }

    public String getStartLonLat() {
        return StartLonLat;
    }

    public void setStartLonLat(String startLonLat) {
        StartLonLat = startLonLat;
    }

    public String getEndLonLat() {
        return EndLonLat;
    }

    public void setEndLonLat(String endLonLat) {
        EndLonLat = endLonLat;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public Integer getPassNum() {
        return PassNum;
    }

    public void setPassNum(Integer passNum) {
        PassNum = passNum;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Integer getCondition() {
        return Condition;
    }

    public void setCondition(Integer condition) {
        Condition = condition;
    }

    public String getRelPassSeq() {
        return RelPassSeq;
    }

    public void setRelPassSeq(String relPassSeq) {
        RelPassSeq = relPassSeq;
    }
}
