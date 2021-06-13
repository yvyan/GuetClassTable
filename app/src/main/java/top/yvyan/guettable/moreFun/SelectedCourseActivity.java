package top.yvyan.guettable.moreFun;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.xui.widget.spinner.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import top.yvyan.guettable.R;
import top.yvyan.guettable.adapter.SelectedCourseAdapter;
import top.yvyan.guettable.bean.SelectedCourseBean;
import top.yvyan.guettable.bean.TermBean;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.MoreDate;
import top.yvyan.guettable.data.TokenData;
import top.yvyan.guettable.service.IMoreFun;
import top.yvyan.guettable.service.fetch.StaticService;
import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.BeanAttributeUtil;

public class SelectedCourseActivity extends BaseFuncActivity implements IMoreFun, MaterialSpinner.OnItemSelectedListener<String> {

    private GeneralData generalData;
    private TokenData tokenData;
    private MoreDate moreDate;
    private MaterialSpinner spinner;
    private SelectedCourseAdapter adapter;
    private List<String> terms;
    private String curTerm;
    private RecyclerView rv;

    @Override
    protected void childInit() {
        setTitle(getResources().getString(R.string.moreFun_selected_course));
        AppUtil.reportFunc(getApplicationContext(), getString(R.string.moreFun_selected_course));
        setShowMore(false);
        generalData = GeneralData.newInstance(this);
        tokenData = TokenData.newInstance(this);
        moreDate = MoreDate.newInstance(this);
        terms = new ArrayList<>();
        curTerm = generalData.getTerm();
    }

    @Override
    protected void showContent() {
        baseSetContentView(R.layout.activity_selected_course);
        List<SelectedCourseBean> selectedCourseBeans = moreDate.getSelectedCourseBeans();
        spinner = findViewById(R.id.spinner_course_term);
        rv = findViewById(R.id.rv_selected_course);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SelectedCourseAdapter(selectedCourseBeans);
        rv.setAdapter(adapter);

        initTerm();
        spinner.setItems(terms);
        spinner.setOnItemSelectedListener(this);
        for (int i = 0; i < terms.size(); i++) {
            if (terms.get(i).equals(generalData.getTerm())) {
                spinner.setSelectedIndex(i);
                break;
            }
        }

        findViewById(R.id.btn_term_reset).setOnClickListener(v -> {
            curTerm = generalData.getTerm();
            for (int i = 0; i < terms.size(); i++) {
                if (terms.get(i).equals(curTerm)) {
                    spinner.setSelectedIndex(i);
                }
            }
        });

        findViewById(R.id.btn_query_course).setOnClickListener(v -> reSelectTerm());
    }

    @Override
    public int updateData(String cookie) {
        List<SelectedCourseBean> selectedCourseBeans;
        selectedCourseBeans = StaticService.getSelectedCourse(this, cookie, generalData.getTerm());
        if (selectedCourseBeans != null) {
            BeanAttributeUtil beanAttributeUtil = new BeanAttributeUtil();
            Collections.sort(selectedCourseBeans, beanAttributeUtil);
            moreDate.setSelectedCoursesBeans(selectedCourseBeans);
            update = true;
            return 5;
        }
        return 1;
    }

    @Override
    public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
        curTerm = item;
    }

    // 查看其他学期的已选课程
    private void reSelectTerm() {
        if (curTerm == null) {
            return;
        }
        new Thread(() -> {
            List<SelectedCourseBean> selectedCourse = StaticService.getSelectedCourse(this, tokenData.getCookie(), curTerm);
            if (selectedCourse != null) {
                runOnUiThread(() -> {
                    BeanAttributeUtil beanAttributeUtil = new BeanAttributeUtil();
                    Collections.sort(selectedCourse, beanAttributeUtil);
                    adapter = new SelectedCourseAdapter(selectedCourse);
                    rv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                });
            }
        }).start();
    }

    // 初始化学期集合
    private void initTerm() {
        List<TermBean> termBeans = moreDate.getTermBeans();
        String grade = generalData.getGrade();
        if (grade == null) {
            return;
        }
        int gradeNum = Integer.parseInt(grade);
        if (termBeans.size() > 0) {
            terms.clear();
            for (int i = 0; i < termBeans.size(); i++) {
                String substring = termBeans.get(i).getTerm().substring(0, 4);
                if (Integer.parseInt(substring) >= gradeNum) {
                    terms.add(termBeans.get(i).getTerm());
                }
            }
        } else {
            new Thread(() -> {
                List<TermBean> allTerm = StaticService.getTerms(this, tokenData.getCookie());
                if (allTerm != null) {
                    moreDate.setTermBeans(allTerm);
                    terms.clear();
                    for (int i = 0; i < allTerm.size(); i++) {
                        String substring = allTerm.get(i).getTerm().substring(0, 4);
                        if (Integer.parseInt(substring) >= gradeNum) {
                            terms.add(allTerm.get(i).getTerm());
                        }
                    }
                    runOnUiThread(() -> {
                        if (terms.size() != spinner.getItems().size()) {
                            spinner.setItems(terms);
                            for (int i = 0; i < terms.size(); i++) {
                                if (terms.get(i).equals(generalData.getTerm())) {
                                    spinner.setSelectedIndex(i);
                                    break;
                                }
                            }
                        }
                    });
                }
            }).start();
        }
    }
}