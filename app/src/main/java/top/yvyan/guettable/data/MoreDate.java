package top.yvyan.guettable.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import top.yvyan.guettable.bean.ExamBean;
import top.yvyan.guettable.util.SerializeUtil;

public class MoreDate {
    private static MoreDate moreDate;
    private static final String SHP_NAME = "MoreData";
    private static final String EXAM_STRING = "examString";
    private SharedPreferences sharedPreferences;

    private List<ExamBean> examBeans;

    private MoreDate(Activity activity) {
        sharedPreferences = activity.getSharedPreferences(SHP_NAME, Context.MODE_PRIVATE);
        load();
    }

    private void load() {
        ExamBean[] examBeans1 = null;
        String examString = sharedPreferences.getString(EXAM_STRING, null);
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
}
