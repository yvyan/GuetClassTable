package top.yvyan.guettable.service;

import com.zhuangfei.timetable.model.Schedule;
import com.zhuangfei.timetable.model.ScheduleSupport;
import com.zhuangfei.timetable.operater.SimpleOperater;

import java.util.List;

/**
 * com.github.zfman:TimetableView修复类，解决了部分情况下临时切换周后点击空白无法触发回调的bug
 * 复现：当前周某位置有课，临时切换到改位置无课的周，点击无法触发空白点击回调
 */
public class MyOperator extends SimpleOperater {

    protected int currWeek;

    /**
     * 切换周次
     *
     * @param week      周
     * @param isCurWeek 是否强制设置为当前周
     */
    @Override
    public void changeWeek(int week, boolean isCurWeek) {
        super.changeWeek(week, isCurWeek);
        currWeek = week;
    }

    /**
     * 判断位置是否有课
     *
     * @param day   天
     * @param start 节
     * @return true：有课，false：无课
     */
    @Override
    protected boolean checkPosition(int day, int start) {
        List<Schedule> list;
        if (mView.isShowNotCurWeek()) {
            list = ScheduleSupport.getAllSubjectsWithDay(mView.dataSource(), day);
        } else {
            list = ScheduleSupport.getHaveSubjectsWithDay(mView.dataSource(), currWeek, day);
        }
        boolean isHave = false;
        for (Schedule item : list) {
            if (start == item.getStart() || (start >= item.getStart() && start <= (item.getStart() + item.getStep() - 1))) {
                isHave = true;
            }
        }
        return isHave;
    }
}
