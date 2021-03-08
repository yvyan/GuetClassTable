package top.yvyan.guettable.bean;

import android.annotation.SuppressLint;

import com.umeng.umcrash.UMCrash;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import top.yvyan.guettable.util.BeanAttributeUtil;

public class ResitBean implements Serializable, BeanAttributeUtil.BeanAttribute {

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

    @SuppressLint("SimpleDateFormat")
    public ResitBean(String number, String name, String time, String date, String room) {
        this.number = number;
        this.name = name;
        this.time = time;
        SimpleDateFormat simpleDateFormat;
        if (date == null) {
            date = "";
        }
        if (date.contains("-")) {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        } else {
            simpleDateFormat = new SimpleDateFormat("MM dd yyyy");
        }
        try {
            this.date = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            UMCrash.generateCustomLog(e, "ResitBean");
        }
        this.room = room;
    }


    @Override
    public String getTerm() {
        return null;
    }

    @Override
    public long getOrder() {
        if (date == null) {
            return 0;
        }
        return date.getTime();
    }

    public String getNumber() {
        if (number == null) {
            number = "";
        }
        return number;
    }

    public String getName() {
        if (name == null) {
            name = "";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        if (time == null) {
            time = "";
        }
        return time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRoom() {
        if (room == null) {
            room = "";
        }
        return room;
    }
}
