package top.yvyan.guettable.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import top.yvyan.guettable.bean.CourseBean;
import top.yvyan.guettable.util.SerializeUtils;

public class ClassData {
    private static ClassData classData;
    private static final String SHP_NAME = "ClassData";
    private static final String CLASS_STRING = "classString";
    private SharedPreferences sharedPreferences;

    private List<CourseBean> courseBeans;

    private ClassData(Activity activity) {
        sharedPreferences = activity.getSharedPreferences(SHP_NAME, Context.MODE_PRIVATE);
        load();
    }

    private void load() {
        CourseBean[] courseBeans1 = null;
        String classString = sharedPreferences.getString(CLASS_STRING, null);
        if (classString != null) {
            try {
                courseBeans1 = (CourseBean[]) SerializeUtils.serializeToObject(classString);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            if (courseBeans1 != null) {
                courseBeans = Arrays.asList(courseBeans1);
            }
        }
    }

    public static ClassData newInstance(Activity activity) {
        if (classData == null) {
            classData = new ClassData(activity);
        }
        return classData;
    }

    public List<CourseBean> getCourseBeans() {
        if (courseBeans == null) {
            courseBeans = new ArrayList<>();
        }
        return courseBeans;
    }

    public void setCourseBeans(List<CourseBean> courseBeans) {
        this.courseBeans = courseBeans;
        saveCourseBeans();
    }

    private void saveCourseBeans() {
        String classString = null;
        CourseBean[] courseBeans1 = new CourseBean[courseBeans.size()];
        courseBeans.toArray(courseBeans1);
        try {
            classString = SerializeUtils.serialize(courseBeans1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (classString != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(CLASS_STRING, classString);
            editor.apply();
        }
    }
}
