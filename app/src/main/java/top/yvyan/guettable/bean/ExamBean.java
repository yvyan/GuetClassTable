package top.yvyan.guettable.bean;

import androidx.annotation.Nullable;

import com.zhuangfei.timetable.model.Schedule;
import com.zhuangfei.timetable.model.ScheduleEnable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExamBean implements Serializable, ScheduleEnable, BeanAttribute {
    private static final long serialVersionUID = -193709177883443178L;
    public static String NUMBER = "number";
    public static String TIME = "time";
    public static String DATE = "date";
    public static String TYPE = "type";
    public static String COMM = "comm";

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
    //备注
    private String comm;

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null)
            return false;
        if (obj.getClass() != this.getClass())
            return false;
        ExamBean bean = (ExamBean) obj;
        if (room == null && bean.room != null || room != null && bean.room == null) {
            return false;
        }
        if (room == null && bean.room == null || room != null && room.equals(bean.room)) {
            return number.equals(bean.number)
                    && week == bean.week
                    && day == bean.day
                    && time.equals(bean.time)
                    && comm.equals(bean.comm);
        } else {
            return false;
        }
    }

    public ExamBean() {
    }

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
        this.comm = examBean.getComm();
    }

    public ExamBean(String number, String name, String teacher, int week, int day, int classNum, String time, Date date, String room, String comm) {
        this.number = number;
        this.name = name;
        this.teacher = teacher;
        this.week = week;
        this.day = day;
        this.classNum = classNum;
        this.time = time;
        this.date = date;
        this.room = room;
        if (comm == null) {
            comm = "";
        }
        this.comm = comm;
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
        comm = (String) schedule.getExtras().get(COMM);
        if (comm == null) {
            comm = "";
        }
    }

    @Override
    public Schedule getSchedule() {
        Schedule schedule = new Schedule();
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
        schedule.putExtras(COMM, comm);
        return schedule;
    }

    public String getNumber() {
        return number;
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

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getDay() {
        return day;
    }

    public int getClassNum() {
        return classNum;
    }

    public String getTime() {
        return time;
    }

    public Date getDate() {
        return date;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getComm() {
        if (comm == null) {
            comm = "";
        }
        return comm;
    }

    public void setComm(String comm) {
        this.comm = comm;
    }

    @Override
    public String getTerm() {
        return null;
    }

    @Override
    public long getOrder() {
        return week * 7 + day;
    }
}
