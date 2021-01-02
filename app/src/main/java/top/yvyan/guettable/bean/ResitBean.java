package top.yvyan.guettable.bean;

import java.io.Serializable;
import java.util.Date;

public class ResitBean implements Serializable, BeanAttribute {

    //课号
    private String number;
    //课程名称
    private String name;
    //考试时间
    private String time;
    //日期
    private Date date;
    //教室
    private String room;

    public ResitBean() {}

    public ResitBean(String number, String name, String time, Date date, String room) {
        this.number = number;
        this.name = name;
        this.time = time;
        this.date = date;
        this.room = room;
    }


    @Override
    public String getTerm() {
        return null;
    }

    @Override
    public long getOrder() {
        return date.getTime();
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
