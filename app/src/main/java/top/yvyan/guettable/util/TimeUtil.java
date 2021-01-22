package top.yvyan.guettable.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
    private static final long DAY = (1000 * 60 * 60 * 24);

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

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2) {  //不同年
            int timeDistance = 0;
            if (year1 < year2) {
                for (int i = year1; i < year2; i++) {
                    if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {  //闰年
                        timeDistance += 366;
                    } else {  //不是闰年
                        timeDistance += 365;
                    }
                }
                return timeDistance + (day2 - day1);
            } else {
                for (int i = year2; i < year1; i++) {
                    if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {  //闰年
                        timeDistance -= 366;
                    } else {  //不是闰年
                        timeDistance -= 365;
                    }
                }
                return timeDistance + (day2 - day1);
            }
        } else { //同一年
            return day2 - day1;
        }
    }


    public static int calcWeekOffset(Date startTime, Date endTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startTime);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        dayOfWeek = dayOfWeek - 1;
        if (dayOfWeek == 0) dayOfWeek = 7;

        int dayOffset = calcDayOffset(startTime, endTime);

        int weekOffset = dayOffset / 7;
        int a;
        if (dayOffset > 0) {
            a = (dayOffset % 7 + dayOfWeek > 7) ? 1 : 0;
        } else {
            a = (dayOfWeek + dayOffset % 7 < 1) ? -1 : 0;
        }
        weekOffset = weekOffset + a;
        return weekOffset;
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
        String s = new String();
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

    public static String timeFormat(Date date) {
        DateFormat fmt = new SimpleDateFormat("yyyy年MM月dd日");
        if (date == null) {
            return "";
        }
        return fmt.format(date); // 转换成 X年X月X日
    }
}
