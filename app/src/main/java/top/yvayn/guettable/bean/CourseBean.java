package top.yvayn.guettable.bean;

import com.zhuangfei.timetable.model.Schedule;
import com.zhuangfei.timetable.model.ScheduleEnable;

import java.util.List;

public class CourseBean implements ScheduleEnable {

    //id
    private int id;
    //是否为实验
    private boolean isLab;
    //课号
    private String number;
    //课程名称
    private String name;
    //（实验）名称
    private String libName;
    //教室
    private String room;
    //周次集合
    private List<Integer> weekList;
    //周几
    private int week;
    //第几节课
    private int time;
    //老师
    private String teacher;
    //（实验）备注
    private String remarks;

    //一个随机数，用于对应课程的颜色
    private int colorRandom = 0;

    public CourseBean() {
    }

    //设置为理论课
    public void setCourse(String number, String name, String room, List<Integer> weekList, int week, int time, String teacher) {
        this.isLab = false;
        this.name = name;
        this.room = room;
        this.weekList = weekList;
        this.week = week;
        this.time = time;
        this.teacher = teacher;
        this.number = number;
    }

    //设置为实验课
    public void setLab(String name, String libName, String room, List<Integer> weekList, int week, int time, String remarks) {
        this.isLab = true;
        this.name = name;
        this.libName = libName;
        this.room = room;
        this.weekList = weekList;
        this.week = week;
        this.time = time;
        this.remarks = remarks;
    }

    @Override
    public Schedule getSchedule() {
        Schedule schedule=new Schedule();
        schedule.setDay(getWeek());
        schedule.setName(getName());
        schedule.setRoom(getRoom());
        schedule.setStart(getTime() * 2 - 1);
        schedule.setStep(2);
        schedule.setTeacher(getTeacher());
        schedule.setWeekList(getWeekList());
        schedule.setColorRandom(2);
        return schedule;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isLab() {
        return isLab;
    }

    public void setLab(boolean lab) {
        isLab = lab;
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

    public String getLibName() {
        return libName;
    }

    public void setLibName(String libName) {
        this.libName = libName;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getColorRandom() {
        return colorRandom;
    }

    public void setColorRandom(int colorRandom) {
        this.colorRandom = colorRandom;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public List<Integer> getWeekList() {
        return weekList;
    }

    public void setWeekList(List<Integer> weekList) {
        this.weekList = weekList;
    }

    @Override
    public String toString() {
        return "CourseBean{" +
                "id=" + id +
                ", isLab=" + isLab +
                ", number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", libName='" + libName + '\'' +
                ", room='" + room + '\'' +
                ", weekList=" + weekList +
                ", week=" + week +
                ", time=" + time +
                ", teacher='" + teacher + '\'' +
                ", remarks='" + remarks + '\'' +
                ", colorRandom=" + colorRandom +
                '}';
    }
}
