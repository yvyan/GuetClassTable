package top.yvyan.guettable.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import top.yvyan.guettable.bean.CETBean;
import top.yvyan.guettable.bean.ExamScoreBean;
import top.yvyan.guettable.bean.ExperimentScoreBean;
import top.yvyan.guettable.bean.PlannedCourseBean;
import top.yvyan.guettable.bean.ResitBean;
import top.yvyan.guettable.bean.SelectedCourseBean;
import top.yvyan.guettable.util.SerializeUtil;

public class MoreDate {
    private static MoreDate moreDate;
    private static final String SHP_NAME = "MoreData";
    private static final String RESIT_STRING = "resitString";
    private static final String CET_STRING = "CET_STRING";
    private static final String EXAM_SCORE_STRING = "EXAM_SCORE_STRING";
    private static final String EXPERIMENT_SCORE_STRING = "EXPERIMENT_SCORE_STRING";
    private static final String PLANNED_COURSE_STRING = "plannedCourseString";
    private static final String GRADES_STRING = "gradesString";
    private static final String SELECTED_COURSE = "SELECTED_COURSE";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private List<ResitBean> resitBeans;
    private List<CETBean> cetBeans;
    private List<ExamScoreBean> examScoreBeans;
    private List<ExperimentScoreBean> experimentScoreBeans;
    private List<PlannedCourseBean> plannedCourseBeans;
    private List<SelectedCourseBean> selectedCoursesBeans;
    private float[] grades;

    private MoreDate(Context context) {
        sharedPreferences = context.getSharedPreferences(SHP_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
        load();
    }

    private void load() {
        ResitBean[] resitBeans1 = null;
        CETBean[] cetBeans1 = null;
        ExamScoreBean[] examScoreBeans1 = null;
        ExperimentScoreBean[] experimentScoreBeans1 = null;
        PlannedCourseBean[] plannedCourseBeans1 = null;
        SelectedCourseBean[] selectedCourseBeans1 = null;


        String resitString = sharedPreferences.getString(RESIT_STRING, null);
        String cetString = sharedPreferences.getString(CET_STRING, null);
        String examScoreString = sharedPreferences.getString(EXAM_SCORE_STRING, null);
        String experimentScoreString = sharedPreferences.getString(EXPERIMENT_SCORE_STRING, null);
        String plannedCourseString = sharedPreferences.getString(PLANNED_COURSE_STRING, null);
        String gradesString = sharedPreferences.getString(GRADES_STRING, null);
        String selectedCourseString = sharedPreferences.getString(SELECTED_COURSE, null);

        if (resitString != null) {
            try {
                resitBeans1 = (ResitBean[]) SerializeUtil.serializeToObject(resitString);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (resitBeans1 != null) {
                resitBeans = Arrays.asList(resitBeans1);
            }
        }
        if (cetString != null) {
            try {
                cetBeans1 = (CETBean[]) SerializeUtil.serializeToObject(cetString);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (cetBeans1 != null) {
                cetBeans = Arrays.asList(cetBeans1);
            }
        }
        if (examScoreString != null) {
            try {
                examScoreBeans1 = (ExamScoreBean[]) SerializeUtil.serializeToObject(examScoreString);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (examScoreBeans1 != null) {
                examScoreBeans = Arrays.asList(examScoreBeans1);
            }
        }
        if (experimentScoreString != null) {
            try {
                experimentScoreBeans1 = (ExperimentScoreBean[]) SerializeUtil.serializeToObject(experimentScoreString);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (experimentScoreBeans1 != null) {
                experimentScoreBeans = Arrays.asList(experimentScoreBeans1);
            }
        }
        if (plannedCourseString != null) {
            try {
                plannedCourseBeans1 = (PlannedCourseBean[]) SerializeUtil.serializeToObject(plannedCourseString);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (plannedCourseBeans1 != null) {
                plannedCourseBeans = Arrays.asList(plannedCourseBeans1);
            }
        }
        if (gradesString != null) {
            try {
                grades = (float[]) SerializeUtil.serializeToObject(gradesString);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (selectedCourseString != null) {
            try {
                selectedCourseBeans1 = (SelectedCourseBean[]) SerializeUtil.serializeToObject(selectedCourseString);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (selectedCourseBeans1 != null) {
                selectedCoursesBeans = Arrays.asList(selectedCourseBeans1);
            }
        }
    }

    public static MoreDate newInstance(Context context) {
        if (moreDate == null) {
            moreDate = new MoreDate(context);
        }
        return moreDate;
    }

    public List<ResitBean> getResitBeans() {
        if (resitBeans == null) {
            resitBeans = new ArrayList<>();
        }
        return resitBeans;
    }

    public void setResitBeans(List<ResitBean> resitBeans) {
        this.resitBeans = resitBeans;
        String resitString = null;
        ResitBean[] resitBeans1 = new ResitBean[resitBeans.size()];
        resitBeans.toArray(resitBeans1);
        try {
            resitString = SerializeUtil.serialize(resitBeans1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (resitString != null) {
            editor.putString(RESIT_STRING, resitString);
            editor.apply();
        }
    }

    //CET成绩
    public List<CETBean> getCetBeans() {
        if (cetBeans == null) {
            cetBeans = new ArrayList<>();
        }
        return cetBeans;
    }

    public void setCetBeans(List<CETBean> cetBeans) {
        this.cetBeans = cetBeans;
        String cetString = null;
        CETBean[] cetBeans1 = new CETBean[cetBeans.size()];
        cetBeans.toArray(cetBeans1);
        try {
            cetString = SerializeUtil.serialize(cetBeans1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (cetString != null) {
            editor.putString(CET_STRING, cetString);
            editor.apply();
        }
    }

    //考试成绩
    public List<ExamScoreBean> getExamScoreBeans() {
        if (examScoreBeans == null) {
            examScoreBeans = new ArrayList<>();
        }
        return examScoreBeans;
    }

    public void setExamScoreBeans(List<ExamScoreBean> examScoreBeans) {
        this.examScoreBeans = examScoreBeans;
        String examScoreString = null;
        ExamScoreBean[] examScoreBeans1 = new ExamScoreBean[examScoreBeans.size()];
        examScoreBeans.toArray(examScoreBeans1);
        try {
            examScoreString = SerializeUtil.serialize(examScoreBeans1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (examScoreString != null) {
            editor.putString(EXAM_SCORE_STRING, examScoreString);
            editor.apply();
        }
    }

    //实验成绩
    public List<ExperimentScoreBean> getExperimentScoreBeans() {
        if (experimentScoreBeans == null) {
            experimentScoreBeans = new ArrayList<>();
        }
        return experimentScoreBeans;
    }

    public void setExperimentScoreBeans(List<ExperimentScoreBean> experimentScoreBeans) {
        this.experimentScoreBeans = experimentScoreBeans;
        String experimentScoreString = null;
        ExperimentScoreBean[] experimentScoreBeans1 = new ExperimentScoreBean[experimentScoreBeans.size()];
        experimentScoreBeans.toArray(experimentScoreBeans1);
        try {
            experimentScoreString = SerializeUtil.serialize(experimentScoreBeans1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (experimentScoreString != null) {
            editor.putString(EXPERIMENT_SCORE_STRING, experimentScoreString);
            editor.apply();
        }
    }

    public List<PlannedCourseBean> getPlannedCourseBeans() {
        if (plannedCourseBeans == null) {
            plannedCourseBeans = new ArrayList<>();
        }
        return plannedCourseBeans;
    }

    public List<SelectedCourseBean> getSelectedCourseBeans() {
        if (selectedCoursesBeans == null) {
            selectedCoursesBeans = new ArrayList<>();
        }
        return selectedCoursesBeans;
    }

    public void setSelectedCoursesBeans(List<SelectedCourseBean> selectedCoursesBeans) {
        this.selectedCoursesBeans = selectedCoursesBeans;
    }

    public void setPlannedCourseBeans(List<PlannedCourseBean> plannedCourseBeans) {
        this.plannedCourseBeans = plannedCourseBeans;
        String plannedCourseString = null;
        PlannedCourseBean[] plannedCourseBeans1 = new PlannedCourseBean[plannedCourseBeans.size()];
        plannedCourseBeans.toArray(plannedCourseBeans1);
        try {
            plannedCourseString = SerializeUtil.serialize(plannedCourseBeans1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (plannedCourseString != null) {
            editor.putString(PLANNED_COURSE_STRING, plannedCourseString);
            editor.apply();
        }
    }

    public float[] getGrades() {
        if (grades == null) {
            grades = new float[]{100, 100, 100, 100, 100, 100, 100};
        }
        return grades;
    }

    public void setGrades(float[] grades) {
        this.grades = grades;
        String gradesString = null;
        try {
            gradesString = SerializeUtil.serialize(grades);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (gradesString != null) {
            editor.putString(GRADES_STRING, gradesString);
            editor.apply();
        }
    }
}
