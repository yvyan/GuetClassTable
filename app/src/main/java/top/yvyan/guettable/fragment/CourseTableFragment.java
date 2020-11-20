package top.yvyan.guettable.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import com.zhuangfei.timetable.TimetableView;
import com.zhuangfei.timetable.listener.ISchedule;
import com.zhuangfei.timetable.listener.OnItemBuildAdapter;
import com.zhuangfei.timetable.listener.OnSlideBuildAdapter;
import com.zhuangfei.timetable.model.Schedule;
import com.zhuangfei.timetable.view.WeekView;

import java.util.ArrayList;
import java.util.List;

import top.yvyan.guettable.DetailActivity;
import top.yvyan.guettable.R;
import top.yvyan.guettable.bean.CourseBean;
import top.yvyan.guettable.data.AccountData;
import top.yvyan.guettable.data.ClassData;
import top.yvyan.guettable.data.DayClassData;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.TableSettingData;
import top.yvyan.guettable.util.ToastUtil;

public class CourseTableFragment extends Fragment implements View.OnClickListener {

    private static CourseTableFragment courseTableFragment;
    private static final String TAG = "CourseTableFragment";

    //控件
    private TimetableView mTimetableView;
    private WeekView mWeekView;

    private ImageView moreButton;
    private LinearLayout linearLayout;
    private TextView titleTextView;

    private View view;

    private AccountData accountData;
    private GeneralData generalData;
    private ClassData classData;
    private TableSettingData tableSettingData;
    private DayClassData dayClassData;

    //记录切换的周次，不一定是当前周
    int target = -1;

    public static CourseTableFragment newInstance() {
        if (courseTableFragment == null) {
            CourseTableFragment fragment = new CourseTableFragment();
            courseTableFragment = fragment;
        }
        return courseTableFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "createCourseTableFragmentView");
        view = inflater.inflate(R.layout.fragment_base_func, container, false);
        moreButton = view.findViewById(R.id.id_more);
        moreButton.setOnClickListener(view -> showPopMenu());

        accountData = AccountData.newInstance(getActivity());
        generalData = GeneralData.newInstance(getActivity());
        classData = ClassData.newInstance(getActivity());
        tableSettingData = TableSettingData.newInstance(getActivity());
        dayClassData = DayClassData.newInstance();
        titleTextView = view.findViewById(R.id.id_title);
        linearLayout = view.findViewById(R.id.id_class_layout);
        linearLayout.setOnClickListener(this);
        initTimetableView();
        return view;
    }

    /**
     * 初始化课程控件
     */
    private void initTimetableView() {
        //获取控件
        mWeekView = view.findViewById(R.id.id_weekview);
        mTimetableView = view.findViewById(R.id.id_timetableView);

        //设置周次选择属性
        List<CourseBean> courseBeans;
        courseBeans = classData.getCourseBeans();
        if (courseBeans == null) {
            courseBeans = new ArrayList<>();
        }
        mWeekView.source(courseBeans)
                .curWeek(generalData.getWeek())
                .callback(week -> {
                    int cur = mTimetableView.curWeek();
                    target = week;
                    //更新切换后的日期，从当前周cur->切换的周week
                    mTimetableView.onDateBuildListener()
                            .onUpdateDate(cur, week);
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
        mTimetableView.source(courseBeans)
                .curWeek(generalData.getWeek())
                .curTerm("大三下学期")
                .maxSlideItem(10)
                .monthWidthDp(20)
                .itemHeight(160)
                .callback(new OnItemBuildAdapter() {
                    @Override
                    public String getItemText(Schedule schedule, boolean isThisWeek) {
                        if (schedule.getRoom() != null) {
                            return schedule.getName() + "@" + schedule.getRoom();
                        } else {
                            return schedule.getName();
                        }
                    }
                })
                .callback((ISchedule.OnItemClickListener) (v, scheduleList) -> display(scheduleList))
                .callback((ISchedule.OnItemLongClickListener) (v, day, start) -> Toast.makeText(getActivity(),
                        "长按:周" + day  + ",第" + start + "节",
                        Toast.LENGTH_SHORT).show())
                .callback(curWeek -> {
                    titleTextView.setText("第" + curWeek + "周");
                })
                //隐藏旗标布局
                .isShowFlaglayout(false)
                .showView();
        if (tableSettingData.isHideOtherWeek()) {
            hideNonThisWeek();
        }
    }

    /**
     * 更新一下，防止因程序在后台时间过长（超过一天）而导致的日期或高亮不准确问题。
     */
    @Override
    public void onStart() {
        super.onStart();
        mTimetableView.onDateBuildListener()
                .onHighLight();
    }

    public void updateTable(List<CourseBean> courseBeans) {
        mWeekView.source(courseBeans)
                .showView();
        mTimetableView.source(courseBeans)
                .updateView();
    }

    /**
     * 显示内容
     *
     * @param beans
     */
    protected void display(List<Schedule> beans) {
        List<CourseBean> courseBeans = new ArrayList<>();
        for (Schedule schedule : beans) {
            CourseBean courseBean = new CourseBean();
            courseBean.setFromSchedule(schedule);
            courseBeans.add(courseBean);
        }
        dayClassData.setCourseBeans(courseBeans);
        Intent intent = new Intent(getContext(), DetailActivity.class);
        startActivity(intent);
    }

    /**
     * 显示弹出菜单
     */
    public void showPopMenu() {
        PopupMenu popup = new PopupMenu(getActivity(), moreButton);
        popup.getMenuInflater().inflate(R.menu.popmenu, popup.getMenu());
        if (tableSettingData.isHideOtherWeek()) {
            popup.getMenu().findItem(R.id.top1).setTitle("显示非本周课程");
        }
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.top1:
                    if (tableSettingData.isHideOtherWeek()) {
                        tableSettingData.setHideOtherWeek(false);
                        showNonThisWeek();
                        popup.getMenu().findItem(R.id.top1).setTitle("隐藏非本周课程");
                    } else {
                        tableSettingData.setHideOtherWeek(true);
                        hideNonThisWeek();
                        popup.getMenu().findItem(R.id.top1).setTitle("显示非本周课程");
                    }
                    break;
                default:
                    break;
            }
            return true;
        });
        popup.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_class_layout:
                //如果周次选择已经显示了，那么将它隐藏，更新课程、日期
                //否则，显示
                if (mWeekView.isShowing()) {
                    mWeekView.isShow(false);
                    titleTextView.setTextColor(getResources().getColor(R.color.app_white));
                    int cur = mTimetableView.curWeek();
                    mTimetableView.onDateBuildListener()
                            .onUpdateDate(cur, cur);
                    mTimetableView.changeWeekOnly(cur);
                } else {
                    mWeekView.isShow(true);
                    titleTextView.setTextColor(getResources().getColor(R.color.app_red));
                }
                break;
        }
    }

    /**
     * 删除课程
     * 内部使用集合维护课程数据，操作集合的方法来操作它即可
     * 最后更新一下视图（全局更新）
     */
    protected void deleteSubject() {
        int size = mTimetableView.dataSource().size();
        int pos = (int) (Math.random() * size);
        if (size > 0) {
            mTimetableView.dataSource().remove(pos);
            mTimetableView.updateView();
        }
    }

    /**
     * 添加课程
     * 内部使用集合维护课程数据，操作集合的方法来操作它即可
     * 最后更新一下视图（全局更新）
     */
    protected void addSubject() {
        List<Schedule> dataSource = mTimetableView.dataSource();
        int size = dataSource.size();
        if (size > 0) {
            Schedule schedule = dataSource.get(0);
            dataSource.add(schedule);
            mTimetableView.updateView();
        }
    }

    /**
     * 隐藏非本周课程
     * 修改了内容的显示，所以必须更新全部（性能不高）
     * 建议：在初始化时设置该属性
     * <p>
     * updateView()被调用后，会重新构建课程，课程会回到当前周
     */
    protected void hideNonThisWeek() {
        mTimetableView.isShowNotCurWeek(false).updateView();
    }

    /**
     * 显示非本周课程
     * 修改了内容的显示，所以必须更新全部（性能不高）
     * 建议：在初始化时设置该属性
     */
    protected void showNonThisWeek() {
        mTimetableView.isShowNotCurWeek(true).updateView();
    }

    /**
     * 设置侧边栏最大节次，只影响侧边栏的绘制，对课程内容无影响
     *
     * @param num
     */
    protected void setMaxItem(int num) {
        mTimetableView.maxSlideItem(num).updateSlideView();
    }

    /**
     * 显示时间
     * 设置侧边栏构建监听，TimeSlideAdapter是控件实现的可显示时间的侧边栏
     */
    protected void showTime() {
        String[] times = new String[]{
                "8:00", "9:00", "10:10", "11:00",
                "15:00", "16:00", "17:00", "18:00",
                "19:30", "20:30", "21:30", "22:30"
        };
        OnSlideBuildAdapter listener = (OnSlideBuildAdapter) mTimetableView.onSlideBuildListener();
        listener.setTimes(times)
                .setTimeTextColor(Color.BLACK);
        mTimetableView.updateSlideView();
    }

    /**
     * 隐藏时间
     * 将侧边栏监听置Null后，会默认使用默认的构建方法，即不显示时间
     * 只修改了侧边栏的属性，所以只更新侧边栏即可（性能高），没有必要更新全部（性能低）
     */
    protected void hideTime() {
        mTimetableView.callback((ISchedule.OnSlideBuildListener) null);
        mTimetableView.updateSlideView();
    }

    /**
     * 显示WeekView
     */
    protected void showWeekView() {
        mWeekView.isShow(true);
    }

    /**
     * 隐藏WeekView
     */
    protected void hideWeekView() {
        mWeekView.isShow(false);
    }

    /**
     * 设置月份宽度
     */
    private void setMonthWidth() {
        mTimetableView.monthWidthDp(50).updateView();
    }

    /**
     * 设置月份宽度,默认40dp
     */
    private void resetMonthWidth() {
        mTimetableView.monthWidthDp(40).updateView();
    }

    /**
     * 隐藏周末
     */
    private void hideWeekends() {
        mTimetableView.isShowWeekends(false).updateView();
    }

    /**
     * 显示周末
     */
    private void showWeekends() {
        mTimetableView.isShowWeekends(true).updateView();
    }
}