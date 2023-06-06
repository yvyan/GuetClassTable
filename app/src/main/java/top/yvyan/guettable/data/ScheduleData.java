package top.yvyan.guettable.data;

import com.google.gson.reflect.TypeToken;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;

import top.yvyan.guettable.bean.CourseBean;
import top.yvyan.guettable.bean.ExamBean;

public class ScheduleData extends BaseData {
    private static final String MAIN_KEY = "ScheduleData.";
    private static final String CLASS_STRING = MAIN_KEY + "classString";
    private static final String LIB_STRING = MAIN_KEY + "libString";
    private static final String EXAM_STRING = MAIN_KEY + "examString";
    private static final String USER_COURSE_NO = MAIN_KEY + "userCourseNo";
    private static final String USER_COURSE_BEANS = MAIN_KEY + "userCourseBeans";

    private static boolean isUpdate = false; //用于同步数据后周课表继续刷新

    //课程信息
    public static List<CourseBean> getCourseBeans() {
        return get(CLASS_STRING, new TypeToken<List<CourseBean>>() {
        }.getType());
    }

    public static void setCourseBeans(List<CourseBean> courseBeans) {
        set(CLASS_STRING, courseBeans);
    }

    //实验信息
    public static List<CourseBean> getLibBeans() {
        return get(LIB_STRING, new TypeToken<List<CourseBean>>() {
        }.getType());
    }

    public static void setLibBeans(List<CourseBean> libBeans) {
        set(LIB_STRING, libBeans);
    }

    //考试安排
    public static List<ExamBean> getExamBeans() {
        return get(EXAM_STRING, new TypeToken<List<ExamBean>>() {
        }.getType());
    }

    public static void setExamBeans(List<ExamBean> examBeans) {
        set(EXAM_STRING, examBeans);
    }

    //用户自定义课程
    public static List<CourseBean> getUserCourseBeans() {
        return get(USER_COURSE_BEANS, new TypeToken<List<CourseBean>>() {
        }.getType());
    }

    public static void setUserCourseBeans(List<CourseBean> userCourseBeans) {
        set(USER_COURSE_BEANS, userCourseBeans);
    }

    public static void deleteInputCourse() {
        setCourseBeans(new ArrayList<>());
        setLibBeans(new ArrayList<>());
        setExamBeans(new ArrayList<>());
    }

    public static void deleteUserCourse() {
        setUserCourseNo(1);
        setUserCourseBeans(new ArrayList<>());
    }

    public static long getUserCourseNo() {
        MMKV mmkv = MMKV.defaultMMKV();
        long userCourseNo = mmkv.decodeLong(USER_COURSE_NO, 1);
        setUserCourseNo(userCourseNo + 1);
        return userCourseNo;
    }

    public static void setUserCourseNo(long userCourseNo) {
        MMKV mmkv = MMKV.defaultMMKV();
        mmkv.encode(USER_COURSE_NO, userCourseNo);
    }

    public static void deleteUserCourse(long id) {
        List<CourseBean> userCourseBeans = getUserCourseBeans();
        for (int i = 0; i < userCourseBeans.size(); i++) {
            if (userCourseBeans.get(i).getId() == id) {
                userCourseBeans.remove(i);
                setUserCourseBeans(userCourseBeans);
                break;
            }
        }
    }

    /**
     * 通过现有课程数据计算最大周数
     *
     * @return 最大周数
     */
    public static int getMaxWeek() {
        int maxWeek = 0;
        List<CourseBean> courseBeans = getCourseBeans();
        List<CourseBean> libBeans = getLibBeans();
        List<CourseBean> userCourseBeans = getUserCourseBeans();
        List<ExamBean> examBeans = getExamBeans();
        try {
            for (CourseBean courseBean : courseBeans) {
                if (courseBean.getWeekEnd() > maxWeek) {
                    maxWeek = courseBean.getWeekEnd();
                }
            }
            for (CourseBean courseBean : libBeans) {
                if (courseBean.getWeekEnd() > maxWeek) {
                    maxWeek = courseBean.getWeekEnd();
                }
            }
            for (CourseBean courseBean : userCourseBeans) {
                if (courseBean.getWeekEnd() > maxWeek) {
                    maxWeek = courseBean.getWeekEnd();
                }
            }
            for (ExamBean examBean : examBeans) {
                if (examBean.getWeek() > maxWeek) {
                    maxWeek = examBean.getWeek();
                }
            }
            if (maxWeek > 25) {
                maxWeek = 25;
            }
            return maxWeek;
        } catch (Exception e) {
            return 0;
        }
    }

    public static boolean isUpdate() {
        return isUpdate;
    }

    public static void setUpdate(boolean update) {
        isUpdate = update;
    }
}
