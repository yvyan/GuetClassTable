package top.yvyan.guettable.bean;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ResitBean implements Serializable, BeanAttribute {

    private static final long serialVersionUID = 311647067934662380L;
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

    public ResitBean(String number, String name, String time, String date, String room) {
        this.number = number;
        this.name = name;
        this.time = time;
        SimpleDateFormat simpleDateFormat;
        if (date.contains("-")) {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        } else {
            simpleDateFormat = new SimpleDateFormat("MM dd yyyy");
        }
        if (date != null) {
            try {
                this.date = simpleDateFormat.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
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
