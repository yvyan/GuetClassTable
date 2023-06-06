package top.yvyan.guettable.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.mmkv.MMKV;

import java.util.List;

import top.yvyan.guettable.bean.CETBean;
import top.yvyan.guettable.bean.ExamScoreBean;
import top.yvyan.guettable.bean.ExperimentScoreBean;
import top.yvyan.guettable.bean.PlannedCourseBean;
import top.yvyan.guettable.bean.ResitBean;
import top.yvyan.guettable.bean.SelectedCourseBean;
import top.yvyan.guettable.bean.TermBean;

public class MoreData extends BaseData {
    private static final String MAIN_KEY = "MoreData";
    private static final String CET_STRING = MAIN_KEY + "CET_STRING";
    private static final String EXAM_SCORE_STRING = MAIN_KEY + "EXAM_SCORE_STRING";
    private static final String RESIT_STRING = MAIN_KEY + "resitString";
    private static final String EXPERIMENT_SCORE_STRING = MAIN_KEY + "EXPERIMENT_SCORE_STRING";
    private static final String PLANNED_COURSE_STRING = MAIN_KEY + "plannedCourseString";
    private static final String GRADES_STRING = MAIN_KEY + "gradesString";
    private static final String SELECTED_COURSE = MAIN_KEY + "selectedCourses";
    private static final String ALL_TERM = MAIN_KEY + "allTerms";

    //CET成绩
    public static List<CETBean> getCetBeans() {
        return get(CET_STRING, new TypeToken<List<CETBean>>() {
        }.getType());
    }

    public static void setCetBeans(List<CETBean> cetBeans) {
        set(CET_STRING, cetBeans);
    }

    //考试成绩
    public static List<ExamScoreBean> getExamScoreBeans() {
        return get(EXAM_SCORE_STRING, new TypeToken<List<ExamScoreBean>>() {
        }.getType());
    }

    public static void setExamScoreBeans(List<ExamScoreBean> examScoreBeans) {
        set(EXAM_SCORE_STRING, examScoreBeans);
    }

    //补考成绩
    public static List<ResitBean> getResitBeans() {
        return get(RESIT_STRING, new TypeToken<List<ResitBean>>() {
        }.getType());
    }

    public static void setResitBeans(List<ResitBean> resitBeans) {
        set(RESIT_STRING, resitBeans);
    }

    //实验成绩
    public static List<ExperimentScoreBean> getExperimentScoreBeans() {
        return get(EXPERIMENT_SCORE_STRING, new TypeToken<List<ExperimentScoreBean>>() {
        }.getType());
    }

    public static void setExperimentScoreBeans(List<ExperimentScoreBean> experimentScoreBeans) {
        set(EXPERIMENT_SCORE_STRING, experimentScoreBeans);
    }

    //计划课程
    public static List<PlannedCourseBean> getPlannedCourseBeans() {
        return get(PLANNED_COURSE_STRING, new TypeToken<List<PlannedCourseBean>>() {
        }.getType());
    }

    public static void setPlannedCourseBeans(List<PlannedCourseBean> plannedCourseBeans) {
        set(PLANNED_COURSE_STRING, plannedCourseBeans);
    }

    //已选课程
    public static List<SelectedCourseBean> getSelectedCourseBeans() {
        return get(SELECTED_COURSE, new TypeToken<List<SelectedCourseBean>>() {
        }.getType());
    }

    public static void setSelectedCoursesBeans(List<SelectedCourseBean> selectedCoursesBeans) {
        set(SELECTED_COURSE, selectedCoursesBeans);
    }

    //学期列表
    public static List<TermBean> getTermBeans() {
        return get(ALL_TERM, new TypeToken<List<TermBean>>() {
        }.getType());
    }

    public static void setTermBeans(List<TermBean> termBeans) {
        set(ALL_TERM, termBeans);
    }

    //学分绩
    public static float[] getGrades() {
        float[] grades = null;
        MMKV mmkv = MMKV.defaultMMKV();
        try {
            String str = mmkv.decodeString(GRADES_STRING);
            grades = new Gson().fromJson(str, new TypeToken<float[]>() {
            }.getType());
        } catch (Exception e) {
            mmkv.remove(GRADES_STRING);
        }
        if (grades == null) {
            grades = new float[]{100, 100, 100, 100, 100, 100, 100};
        }
        return grades;
    }

    public static void setGrades(float[] grades) {
        String str = new Gson().toJson(grades);
        MMKV mmkv = MMKV.defaultMMKV();
        mmkv.encode(GRADES_STRING, str);
    }
}
