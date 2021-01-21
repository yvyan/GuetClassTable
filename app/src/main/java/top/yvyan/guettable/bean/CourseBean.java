package top.yvyan.guettable.bean;

import com.zhuangfei.timetable.model.Schedule;
import com.zhuangfei.timetable.model.ScheduleEnable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CourseBean implements ScheduleEnable, Serializable {
    public static String IS_LAB = "isLab";
    public static String LIB_NAME = "libName";
    public static String REMARKS = "remarks";
    public static String NUMBER = "number";
    public static String WEEK_START = "weekStart";
    public static String WEEK_END = "weekEnd";
    public static String TYPE = "type";

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
    //开始周次
    private int weekStart;
    //结束周次
    private int weekEnd;
    //周次集合
    private List<Integer> weekList;
    //周几
    private int day;
    //第几节课
    private int time;
    //老师
    private String teacher;
    //备注
    private String remarks;
    //实验id
    private long labId;

    //一个随机数，用于对应课程的颜色
    private int colorRandom = 0;

    public CourseBean() {
    }

    //设置为理论课
    public void setCourse(String number, String name, String room, int weekStart, int weekEnd, int day, int time, String teacher, String remarks) {
        this.isLab = false;
        this.name = name;
        this.room = room;
        this.weekList=new ArrayList<>();
        for(int k = weekStart; k <= weekEnd; k++){
            weekList.add(k);
        }
        this.weekStart = weekStart;
        this.weekEnd = weekEnd;
        this.day = day;
        this.time = time;
        this.teacher = teacher;
        this.number = number;
        this.remarks = remarks;
    }

    //设置为实验课
    public void setLab(String name, String libName, int batch, String room, int weekStart, int day, int time, String teacher, String remarks, long labId) {
        this.isLab = true;
        this.name = "(实验)" + name;
        this.libName = libName + "(" + batch + "批次)";
        this.room = room;
        this.weekList = new ArrayList<>();
        this.weekList.add(weekStart);
        this.weekStart = weekStart;
        this.weekEnd = weekStart;
        this.day = day;
        this.time = time;
        this.teacher = teacher;
        this.remarks = remarks;
        this.labId = labId;
    }

    public void setFromSchedule(Schedule schedule) {
        isLab = (boolean)schedule.getExtras().get(IS_LAB);
        weekStart = (int) schedule.getExtras().get(WEEK_START);
        weekEnd = (int) schedule.getExtras().get(WEEK_END);
        remarks = (String) schedule.getExtras().get(REMARKS);
        if (isLab) {
            libName = (String) schedule.getExtras().get(LIB_NAME);
        }

        number = (String) schedule.getExtras().get(NUMBER);
        day = schedule.getDay();
        name = schedule.getName();
        room = schedule.getRoom();
        time = (schedule.getStart() + 1) / 2;
        teacher = schedule.getTeacher();
        weekList = schedule.getWeekList();
    }

    @Override
    public Schedule getSchedule() {
        Schedule schedule=new Schedule();
        schedule.setDay(getDay());
        schedule.setName(getName());
        schedule.setRoom(getRoom());
        schedule.setStart(getTime() * 2 - 1);
        schedule.setStep(2);
        schedule.setTeacher(getTeacher());
        schedule.setWeekList(getWeekList());
        schedule.setColorRandom(2);

        schedule.putExtras(IS_LAB, isLab);
        schedule.putExtras(LIB_NAME, libName);
        schedule.putExtras(REMARKS, remarks);
        schedule.putExtras(NUMBER, number);
        schedule.putExtras(WEEK_START, weekStart);
        schedule.putExtras(WEEK_END, weekEnd);
        //类型: 0:理论课; 1:实验课; 2:考试安排
        schedule.putExtras(TYPE, isLab? 1 : 0);
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

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public List<Integer> getWeekList() {
        return weekList;
    }

    public void setWeekList(List<Integer> weekList) {
        this.weekList = weekList;
    }

    public int getWeekStart() {
        return weekStart;
    }

    public void setWeekStart(int weekStart) {
        this.weekStart = weekStart;
    }

    public int getWeekEnd() {
        return weekEnd;
    }

    public void setWeekEnd(int weekEnd) {
        this.weekEnd = weekEnd;
    }

    public long getLabId() {
        return labId;
    }

    public void setLabId(long labId) {
        this.labId = labId;
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
                ", weekStart=" + weekStart +
                ", weekEnd=" + weekEnd +
                ", weekList=" + weekList +
                ", day=" + day +
                ", time=" + time +
                ", teacher='" + teacher + '\'' +
                ", remarks='" + remarks + '\'' +
                ", colorRandom=" + colorRandom +
                '}';
    }
}
