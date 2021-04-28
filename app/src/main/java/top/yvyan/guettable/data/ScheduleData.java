package top.yvyan.guettable.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import top.yvyan.guettable.bean.CourseBean;
import top.yvyan.guettable.bean.ExamBean;
import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.SerializeUtil;

public class ScheduleData {
    private static ScheduleData scheduleData;
    private static final String SHP_NAME = "ClassData";
    private static final String CLASS_STRING = "classString";
    private static final String LIB_STRING = "libString";
    private static final String EXAM_STRING = "examString";
    private static final String USER_COURSE_NO = "userCourseNo";
    private static final String USER_COURSE_BEANS = "userCourseBeans";
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    private List<CourseBean> courseBeans;
    private List<CourseBean> libBeans;
    private List<ExamBean> examBeans;
    private long userCourseNo;
    private List<CourseBean> userCourseBeans;
    private boolean isUpdate = false; //用于同步数据后周课表继续刷新

    @SuppressLint("CommitPrefEdits")
    private ScheduleData(Context context) {
        sharedPreferences = context.getSharedPreferences(SHP_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        load();
    }

    private void load() {
        CourseBean[] courseBeans1 = null;
        CourseBean[] libBeans1 = null;
        ExamBean[] examBeans1 = null;
        CourseBean[] userCourseBeans1 = null;
        String classString = sharedPreferences.getString(CLASS_STRING, null);
        String libString = sharedPreferences.getString(LIB_STRING, null);
        String examString = sharedPreferences.getString(EXAM_STRING, null);
        String userCourseString = sharedPreferences.getString(USER_COURSE_BEANS, null);
        if (classString != null) {
            try {
                courseBeans1 = (CourseBean[]) SerializeUtil.serializeToObject(classString);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (courseBeans1 != null) {
                courseBeans = Arrays.asList(courseBeans1);
                courseBeans = new ArrayList<>(courseBeans);
            }
        }
        if (libString != null) {
            try {
                libBeans1 = (CourseBean[]) SerializeUtil.serializeToObject(libString);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (libBeans1 != null) {
                libBeans = Arrays.asList(libBeans1);
                libBeans = new ArrayList<>(libBeans);
            }
        }
        if (examString != null) {
            try {
                examBeans1 = (ExamBean[]) SerializeUtil.serializeToObject(examString);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (examBeans1 != null) {
                examBeans = Arrays.asList(examBeans1);
                examBeans = new ArrayList<>(examBeans);
            }
        }
        if (userCourseString != null) {
            try {
                userCourseBeans1 = (CourseBean[]) SerializeUtil.serializeToObject(userCourseString);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (userCourseBeans1 != null) {
                userCourseBeans = Arrays.asList(userCourseBeans1);
                userCourseBeans = new ArrayList<>(userCourseBeans);
            }
        }
        userCourseNo = sharedPreferences.getLong(USER_COURSE_NO, 1);
    }

    public static ScheduleData newInstance(Context context) {
        if (scheduleData == null) {
            scheduleData = new ScheduleData(context);
        }
        return scheduleData;
    }

    //课程信息
    public List<CourseBean> getCourseBeans() {
        if (courseBeans == null) {
            courseBeans = new ArrayList<>();
        }
        return courseBeans;
    }

    public void setCourseBeans(List<CourseBean> courseBeans) {
        this.courseBeans = courseBeans;
        String classString = null;
        CourseBean[] courseBeans1 = new CourseBean[courseBeans.size()];
        courseBeans.toArray(courseBeans1);
        try {
            classString = SerializeUtil.serialize(courseBeans1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (classString != null) {
            editor.putString(CLASS_STRING, classString);
            editor.apply();
        }
    }

    //实验信息
    public List<CourseBean> getLibBeans() {
        if (libBeans == null) {
            libBeans = new ArrayList<>();
        }
        return libBeans;
    }

    public void setLibBeans(List<CourseBean> libBeans) {
        this.libBeans = libBeans;
        String libString = null;
        CourseBean[] libBean1 = new CourseBean[libBeans.size()];
        libBeans.toArray(libBean1);
        try {
            libString = SerializeUtil.serialize(libBean1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (libString != null) {
            editor.putString(LIB_STRING, libString);
            editor.apply();
        }
    }

    //考试安排
    public List<ExamBean> getExamBeans() {
        if (examBeans == null) {
            examBeans = new ArrayList<>();
        }
        return examBeans;
    }

    public void setExamBeans(List<ExamBean> examBeans) {
        this.examBeans = examBeans;
        String examString = null;
        ExamBean[] examBeans1 = new ExamBean[examBeans.size()];
        examBeans.toArray(examBeans1);
        try {
            examString = SerializeUtil.serialize(examBeans1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (examString != null) {
            editor.putString(EXAM_STRING, examString);
            editor.apply();
        }
    }

    //用户自定义课程
    public List<CourseBean> getUserCourseBeans() {
        if (userCourseBeans == null) {
            userCourseBeans = new ArrayList<>();
        }
        return userCourseBeans;
    }

    public void setUserCourseBeans(List<CourseBean> userCourseBeans) {
        this.userCourseBeans = userCourseBeans;
        String userCourseString = null;
        CourseBean[] userCourseBeans1 = new CourseBean[userCourseBeans.size()];
        userCourseBeans.toArray(userCourseBeans1);
        try {
            userCourseString = SerializeUtil.serialize(userCourseBeans1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (userCourseString != null) {
            editor.putString(USER_COURSE_BEANS, userCourseString);
            editor.apply();
        }
    }

    public void deleteInputCourse() {
        setCourseBeans(new ArrayList<>());
        setLibBeans(new ArrayList<>());
        setExamBeans(new ArrayList<>());
    }

    public void deleteUserCourse() {
        setUserCourseNo(1);
        setUserCourseBeans(new ArrayList<>());
    }

    public long getUserCourseNo() {
        setUserCourseNo(userCourseNo + 1);
        return userCourseNo;
    }

    public void setUserCourseNo(long userCourseNo) {
        this.userCourseNo = userCourseNo;
        editor.putLong(USER_COURSE_NO, userCourseNo);
        editor.apply();
    }

    public void deleteUserCourse(long id) {
        for (int i = 0; i < userCourseBeans.size(); i++) {
            if (userCourseBeans.get(i).getId() == id) {
                AppUtil.Log(userCourseBeans.get(i).toString());
                userCourseBeans.remove(i);
                setUserCourseBeans(userCourseBeans);
                break;
            }
        }
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }
}
