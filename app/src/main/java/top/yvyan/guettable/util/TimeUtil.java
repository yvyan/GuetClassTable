package top.yvyan.guettable.util;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

    /**
     * 计算天数相差 (date2 - date1)
     *
     * @param date1 减日期
     * @param date2 被减日期
     * @return      天数差
     */
    public static int calcDayOffset(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);
        long stamp1=cal1.getTime().getTime();
        return (int)((date2.getTime()-stamp1)/(86400*1000));
    }

    /**
     * 计算相差周数，以周一作为一周的开始日
     * @param startTime
     * @param endTime
     * @return weekOffset
     */
    public static int calcWeekOffset(Date startTime, Date endTime) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(startTime);
        cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);
        int weekday = cal1.get(Calendar.DAY_OF_WEEK) - 1;
        long stamp1=cal1.getTime().getTime() - ((weekday + 6) % 7) * 86400000;
        return (int)((endTime.getTime()-stamp1)/(86400*1000*7));
    }

    /**
     * 返回今天周几
     * @return 星期几，0：周一，1：周二，依次类推..周日：6
     */
    public static int getDay() {
        final Calendar calendar = Calendar.getInstance();
        int n = calendar.get(Calendar.DAY_OF_WEEK);
        if (n == 1) {
            return 6;
        } else {
            return n - 2;
        }
    }

    /**
     * 返回明天周几
     * @return 星期几，0：周一，1：周二，依次类推..周日：6
     */
    public static int getNextDay() {
        int n = getDay();
        return (n + 1) % 7;
    }

    /**
     * 返回明天是第几周
     * @param week 今天的周数
     * @return     明天的周数
     */
    public static int getNextDayWeek(int week) {
        int n = getDay();
        if (n == 6) {
            week++;
        }
        return week;
    }

    /**
     * 数字转汉字
     * @param number 星期几 数字
     * @return       "星期几"
     */
    public static String whichDay(int number){
        String s = "";
        switch (number){
            case 1:
                s = "星期一";
                break;
            case 2:
                s = "星期二";
                break;
            case 3:
                s = "星期三";
                break;
            case 4:
                s = "星期四";
                break;
            case 5:
                s = "星期五";
                break;
            case 6:
                s = "星期六";
                break;
            case 7:
                s = "星期日";
                break;
        }
        return s;
    }

    /**
     * 将日期类型转化为字符串
     *
     * @param date date
     * @return 字符串(yyyy年MM月dd日)
     */
    public static String timeFormat(Date date) {
        @SuppressLint("SimpleDateFormat") DateFormat fmt = new SimpleDateFormat("yyyy年MM月dd日");
        if (date == null) {
            return "";
        }
        return fmt.format(date); // 转换成 X年X月X日
    }

    /**
     * 将日期类型转化为字符串
     *
     * @param date date
     * @return 字符串(yyyy年MM月dd日)
     */
    public static String timeFormat3339(Date date) {
        @SuppressLint("SimpleDateFormat") DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        if (date == null) {
            return "";
        }
        return fmt.format(date); // 转换成 X年X月X日
    }
}
