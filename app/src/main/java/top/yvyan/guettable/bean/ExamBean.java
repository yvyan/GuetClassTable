package top.yvyan.guettable.bean;

import com.zhuangfei.timetable.model.Schedule;
import com.zhuangfei.timetable.model.ScheduleEnable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExamBean implements Serializable, ScheduleEnable {
    public static String NUMBER = "number";
    public static String TIME = "time";
    public static String DATE = "date";
    public static String TYPE = "type";

    //课号
    private String number;
    //课程名称
    private String name;
    //老师
    private String teacher;
    //周次
    private int week;
    //星期
    private int day;
    //节数
    private int classNum;
    //考试时间
    private String time;
    //日期
    private Date date;
    //教室
    private String room;

    public ExamBean() {}

    public ExamBean(ExamBean examBean) {
        this.number = examBean.getNumber();
        this.name = examBean.getName();
        this.teacher = examBean.getTeacher();
        this.week = examBean.getWeek();
        this.day = examBean.getDay();
        this.classNum = examBean.getClassNum();
        this.time = examBean.getTime();
        this.date = examBean.getDate();
        this.room = examBean.getRoom();
    }

    public ExamBean(String number, String name, String teacher, int week, int day, int classNum, String time, Date date, String room) {
        this.number = number;
        this.name = name;
        this.teacher = teacher;
        this.week = week;
        this.day = day;
        this.classNum = classNum;
        this.time = time;
        this.date = date;
        this.room = room;
    }

    public void setFromSchedule(Schedule schedule) {
        number = (String) schedule.getExtras().get(NUMBER);
        day = schedule.getDay();
        name = schedule.getName();
        room = schedule.getRoom();
        classNum = (schedule.getStart() + 1) / 2;
        teacher = schedule.getTeacher();
        week = schedule.getWeekList().get(0);
        time = (String) schedule.getExtras().get(TIME);
        date = (Date) schedule.getExtras().get(DATE);
    }

    @Override
    public Schedule getSchedule() {
        Schedule schedule=new Schedule();
        schedule.setDay(getDay());
        schedule.setName(getName());
        schedule.setRoom(getRoom());
        schedule.setStart(getClassNum() * 2 - 1);
        schedule.setStep(2);
        schedule.setTeacher(getTeacher());
        List<Integer> weekList = new ArrayList<>();
        weekList.add(getWeek());
        schedule.setWeekList(weekList);
        schedule.setColorRandom(2);

        schedule.putExtras(TIME, time);
        schedule.putExtras(DATE, date);
        schedule.putExtras(NUMBER, number);
        //类型: 0:理论课; 1:实验课; 2:考试安排
        schedule.putExtras(TYPE, 2);
        return schedule;
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

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getClassNum() {
        return classNum;
    }

    public void setClassNum(int classNum) {
        this.classNum = classNum;
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

    @Override
    public String toString() {
        return "ExamBean{" +
                "number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", teacher='" + teacher + '\'' +
                ", week=" + week +
                ", day=" + day +
                ", classNum=" + classNum +
                ", time='" + time + '\'' +
                ", date=" + date +
                ", room='" + room + '\'' +
                '}';
    }
}
