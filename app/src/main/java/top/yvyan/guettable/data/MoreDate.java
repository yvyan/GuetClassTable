package top.yvyan.guettable.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import top.yvyan.guettable.Gson.CET;
import top.yvyan.guettable.bean.CETBean;
import top.yvyan.guettable.bean.ExamBean;
import top.yvyan.guettable.util.SerializeUtil;

public class MoreDate {
    private static MoreDate moreDate;
    private static final String SHP_NAME = "MoreData";
    private static final String EXAM_STRING = "examString";
    private static final String CET_STRING = "CET_STRING";
    private SharedPreferences sharedPreferences;

    private List<ExamBean> examBeans;
    private List<CETBean> cetBeans;

    private MoreDate(Activity activity) {
        sharedPreferences = activity.getSharedPreferences(SHP_NAME, Context.MODE_PRIVATE);
        load();
    }

    private void load() {
        ExamBean[] examBeans1 = null;
        CETBean[] cetBeans1 = null;
        String examString = sharedPreferences.getString(EXAM_STRING, null);
        String cetString = sharedPreferences.getString(CET_STRING, null);
        if (examString != null) {
            try {
                examBeans1 = (ExamBean[]) SerializeUtil.serializeToObject(examString);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            if (examBeans1 != null) {
                examBeans = Arrays.asList(examBeans1);
            }
        }
        if (cetString != null) {
            try {
                cetBeans1 = (CETBean[]) SerializeUtil.serializeToObject(cetString);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            if (cetBeans1 != null) {
                cetBeans = Arrays.asList(cetBeans1);
            }
        }
    }

    public static MoreDate newInstance(Activity activity) {
        if (moreDate == null) {
            moreDate = new MoreDate(activity);
        }
        return moreDate;
    }

    public List<ExamBean> getExamBeans() {
        if (examBeans == null) {
            examBeans = new ArrayList<>();
        }
        return examBeans;
    }

    public void setExamBeans(List<ExamBean> examBeans) {
        this.examBeans = examBeans;
        saveExamBeans();
    }

    private void saveExamBeans() {
        String examString = null;
        ExamBean[] examBeans1 = new ExamBean[examBeans.size()];
        examBeans.toArray(examBeans1);
        try {
            examString = SerializeUtil.serialize(examBeans1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (examString != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(EXAM_STRING, examString);
            editor.apply();
        }

    }

    public List<CETBean> getCetBeans() {
        if (cetBeans == null) {
            cetBeans = new ArrayList<>();
        }
        return cetBeans;
    }

    public void setCetBeans(List<CETBean> cetBeans) {
        this.cetBeans = cetBeans;
        saveCETBeans();
    }

    private void saveCETBeans() {
        String cetString = null;
        CETBean[] cetBeans1 = new CETBean[cetBeans.size()];
        cetBeans.toArray(cetBeans1);
        try {
            cetString = SerializeUtil.serialize(cetBeans1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (cetString != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(CET_STRING, cetString);
            editor.apply();
        }
    }
}
