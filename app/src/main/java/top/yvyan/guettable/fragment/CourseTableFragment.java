package top.yvyan.guettable.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import com.zhuangfei.timetable.TimetableView;
import com.zhuangfei.timetable.listener.ISchedule;
import com.zhuangfei.timetable.listener.OnItemBuildAdapter;
import com.zhuangfei.timetable.model.Schedule;
import com.zhuangfei.timetable.view.WeekView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import top.yvyan.guettable.R;
import top.yvyan.guettable.activity.AddCourseActivity;
import top.yvyan.guettable.activity.DetailActivity;
import top.yvyan.guettable.bean.CourseBean;
import top.yvyan.guettable.bean.ExamBean;
import top.yvyan.guettable.data.DetailClassData;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.ScheduleData;
import top.yvyan.guettable.data.SettingData;
import top.yvyan.guettable.data.SingleSettingData;
import top.yvyan.guettable.service.table.MyOperator;
import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.BackgroundUtil;
import top.yvyan.guettable.util.DensityUtil;
import top.yvyan.guettable.util.ExamUtil;
import top.yvyan.guettable.util.ToastUtil;

public class CourseTableFragment extends Fragment implements View.OnClickListener {

    @SuppressLint("StaticFieldLeak")
    private static CourseTableFragment courseTableFragment;
    //控件
    private TimetableView mTimetableView;
    private WeekView mWeekView;
    private ImageView moreButton;
    private TextView titleTextView;
    private ImageView deltaImg;
    private View view;

    private GeneralData generalData;
    private ScheduleData scheduleData;
    private SingleSettingData singleSettingData;
    private DetailClassData detailClassData;
    private SettingData settingData;
    //记录切换的周次，不一定是当前周
    int target;

    public static CourseTableFragment newInstance() {
        if (courseTableFragment == null) {
            courseTableFragment = new CourseTableFragment();
        }
        return courseTableFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        courseTableFragment = this;
        view = inflater.inflate(R.layout.fragment_table, container, false);
        moreButton = view.findViewById(R.id.id_more);
        moreButton.setOnClickListener(view -> showPopMenu());

        View addStatus = view.findViewById(R.id.add_status);
        ViewGroup.LayoutParams lp = addStatus.getLayoutParams();
        lp.height = lp.height + AppUtil.getStatusBarHeight(Objects.requireNonNull(getContext()));
        addStatus.setLayoutParams(lp);

        initData();
        target = generalData.getWeek();
        titleTextView = view.findViewById(R.id.id_title);
        LinearLayout linearLayout = view.findViewById(R.id.id_class_layout);
        linearLayout.setOnClickListener(this);
        mWeekView = view.findViewById(R.id.id_weekview);
        mTimetableView = view.findViewById(R.id.id_timetableView);
        setBackground(BackgroundUtil.isSetBackground(getContext()));
        initTimetableView();
        return view;
    }

    private void initData() {
        generalData = GeneralData.newInstance(getActivity());
        scheduleData = ScheduleData.newInstance(getActivity());
        singleSettingData = SingleSettingData.newInstance(getActivity());
        detailClassData = DetailClassData.newInstance();
        settingData = SettingData.newInstance(getActivity());
    }

    private void setBackground(boolean setBackground) {
        View addStatus = view.findViewById(R.id.add_status);
        View titleBar = view.findViewById(R.id.title_bar);
        if (setBackground) {
            addStatus.setBackgroundColor(getResources().getColor(R.color.colorPrimaryTransparent));
            titleBar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryTransparent));
            mTimetableView.colorPool().setUselessColor(0xCCCCCC);
            mTimetableView.alpha(singleSettingData.getDateAlpha(), singleSettingData.getSlideAlpha(), singleSettingData.getItemAlpha());
        } else {
            addStatus.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            titleBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            mTimetableView.colorPool().setUselessColor(0xE0E0E0);
            mTimetableView.alpha(1, 1, 1);
        }
    }

    /**
     * 初始化课程控件
     */
    @SuppressLint("SetTextI18n")
    private void initTimetableView() {
        //获取控件
        deltaImg = view.findViewById(R.id.deltaIcon);
        ImageButton preToWeekButton = view.findViewById(R.id.pre_week);
        ImageButton nextToWeekButton = view.findViewById(R.id.next_week);
        //设置周次选择属性
        mWeekView.itemCount(generalData.getMaxWeek());
        mWeekView.curWeek(generalData.getWeek())
                .callback(week -> {
                    int cur = mTimetableView.curWeek();
                    target = week;
                    //更新切换后的日期，从当前周cur->切换的周week
                    mTimetableView.onDateBuildListener().onUpdateDate(cur, week);
                    mTimetableView.changeWeekOnly(week);
                })
                .callback(() -> {
                    if (generalData.getWeek() == target) {
                        ToastUtil.showToast(getActivity(), "选择其它周后点击此按钮可设置为当前周");
                    } else {
                        generalData.setWeek(target);
                        mWeekView.curWeek(target).updateView();
                        DayClassFragment.newInstance().updateView();
                        ToastUtil.showToast(getActivity(), "设置第" + target + "周为当前周");
                        mTimetableView.changeWeekForce(target);
                    }
                })
                .isShow(false)//设置隐藏，默认显示
                .showView();
        target = generalData.getWeek();
        mTimetableView.operater(new MyOperator())
                .curWeek(generalData.getWeek())
                .maxSlideItem(12)
                .monthWidthDp(18)
                .itemHeight(DensityUtil.dip2px(Objects.requireNonNull(getContext()), singleSettingData.getItemLength()))
                .callback(new OnItemBuildAdapter() {
                    @Override
                    public String getItemText(Schedule schedule, boolean isThisWeek) {
                        //考试安排
                        if ((int) schedule.getExtras().get(ExamBean.TYPE) == 2) {
                            return "(考试)" + schedule.getName() + "@" + schedule.getRoom();
                        } else { //理论课和课内实验
                            if (schedule.getRoom() != null) {
                                return schedule.getName() + "@" + schedule.getRoom();
                            } else {
                                return schedule.getName();
                            }
                        }
                    }
                })
                .callback((ISchedule.OnItemClickListener) (v, scheduleList) -> display(scheduleList))
                .callback(curWeek -> {
                    titleTextView.setText("第" + curWeek + "周");
                })
                .isShowNotCurWeek(!singleSettingData.isHideOtherWeek())
                .callback(new ISchedule.OnSpaceItemClickListener() {
                    @Override
                    public void onSpaceItemClick(int day, int start) {
                        addCourse(target, day + 1, (start + 1) / 2);
                    }

                    @Override
                    public void onInit(LinearLayout flagLayout, int monthWidth, int itemWidth, int itemHeight, int marTop, int marLeft) {
                    }
                })
                .showView();
        nextToWeekButton.setOnClickListener(view -> {
            int cur = mTimetableView.curWeek();
            target = target + 1;
            target = Math.min(target, generalData.getMaxWeek());
            mTimetableView.onDateBuildListener().onUpdateDate(cur, target);
            mTimetableView.changeWeekOnly(target);
        });
        preToWeekButton.setOnClickListener(view -> {
            int cur = mTimetableView.curWeek();
            target = target - 1;
            target = Math.max(target, 1);
            mTimetableView.onDateBuildListener().onUpdateDate(cur, target);
            mTimetableView.changeWeekOnly(target);
        });
        updateTable();
    }

    public void updateTable() {
        List<Schedule> schedules = new ArrayList<>();
        for (CourseBean courseBean : scheduleData.getCourseBeans()) {
            schedules.add(courseBean.getSchedule());
        }
        for (CourseBean courseBean : scheduleData.getUserCourseBeans()) {
            schedules.add(courseBean.getSchedule());
        }
        if (settingData.getShowLibOnTable()) {
            for (CourseBean courseBean : scheduleData.getLibBeans()) {
                schedules.add(courseBean.getSchedule());
            }
        }
        if (settingData.getShowExamOnTable()) {
            for (ExamBean examBean : ExamUtil.combineExam(scheduleData.getExamBeans())) {
                if (examBean != null && examBean.getWeek() != 0) {
                    schedules.add(examBean.getSchedule());
                }
            }
        }
        mWeekView.data(schedules).showView();
        mTimetableView.data(schedules).updateView();

        mTimetableView.onDateBuildListener().onUpdateDate(mTimetableView.curWeek(), target);
        mTimetableView.changeWeekOnly(target);
    }

    /**
     * 显示内容
     *
     * @param beans beans
     */
    protected void display(List<Schedule> beans) {
        detailClassData.setCourseBeans(beans);
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra("week", target);
        startActivityForResult(intent, DetailActivity.REQUEST_CODE);
    }

    private void addCourse(int week, int day, int start) {
        Intent intent = new Intent(getContext(), AddCourseActivity.class);
        intent.putExtra("week", week);
        intent.putExtra("day", day);
        intent.putExtra("start", start);
        startActivityForResult(intent, AddCourseActivity.REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AddCourseActivity.REQUEST_CODE && resultCode == AddCourseActivity.ADD) {
            updateTable();
        } else if (requestCode == DetailActivity.REQUEST_CODE && resultCode == DetailActivity.ALTER) {
            updateTable();
        }
    }

    /**
     * 显示弹出菜单
     */
    public void showPopMenu() {
        PopupMenu popup = new PopupMenu(Objects.requireNonNull(getActivity()), moreButton);
        popup.getMenuInflater().inflate(R.menu.course_table_popmenu, popup.getMenu());
        if (singleSettingData.isHideOtherWeek()) {
            popup.getMenu().findItem(R.id.course_tab_top1).setTitle("显示非本周课程");
        }
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.course_tab_top1) {
                if (singleSettingData.isHideOtherWeek()) {
                    singleSettingData.setHideOtherWeek(false);
                    popup.getMenu().findItem(R.id.course_tab_top1).setTitle("隐藏非本周课程");
                } else {
                    singleSettingData.setHideOtherWeek(true);
                    popup.getMenu().findItem(R.id.course_tab_top1).setTitle("显示非本周课程");
                }
                mTimetableView
                        .isShowNotCurWeek(!singleSettingData.isHideOtherWeek())
                        .updateView();
            }
            return true;
        });
        popup.show();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.id_class_layout) {//如果周次选择已经显示了，那么将它隐藏，更新课程、日期
            //否则，显示
            if (mWeekView.isShowing()) {
                mWeekView.isShow(false);
                deltaImg.setImageResource(R.drawable.delta);
                titleTextView.setTextColor(getResources().getColor(R.color.app_white));
                int cur = mTimetableView.curWeek();
                mTimetableView.onDateBuildListener()
                        .onUpdateDate(cur, cur);
                mTimetableView.changeWeekOnly(cur);
                target = cur;
            } else {
                deltaImg.setImageResource(R.drawable.delta_pressed);
                mWeekView.isShow(true);
                titleTextView.setTextColor(getResources().getColor(R.color.app_red));
            }
        }
    }
}