package top.yvyan.guettable.adapter;

import static java.lang.Math.max;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhuangfei.timetable.model.Schedule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import top.yvyan.guettable.R;
import top.yvyan.guettable.bean.CourseBean;
import top.yvyan.guettable.bean.ExamBean;
import top.yvyan.guettable.util.TimeUtil;

public class DayClassAdapter extends RecyclerView.Adapter<DayClassAdapter.ClassDetailViewHolder> {
    private final List<Schedule> todayList;
    private final List<Schedule> tomorrowList;
    private final Context context;
    private final int starting;

    public DayClassAdapter(Context context, List<Schedule> todayList, List<Schedule> tomorrowList) {
        this.context = context;
        this.todayList = todayList;
        this.tomorrowList = tomorrowList;
        this.starting = -1;
    }

    public DayClassAdapter(Context context, List<Schedule> todayList, List<Schedule> tomorrowList, int starting) {
        this.context = context;
        this.todayList = todayList;
        this.tomorrowList = tomorrowList;
        this.starting = starting;
    }

    @NonNull
    @Override
    public ClassDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.detail_cardview, parent, false);
        return new ClassDetailViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ClassDetailViewHolder holder, int position) {
        //标头显示
        if (position == 0 || position == todayList.size() + 1) {
            holder.textView2.setVisibility(View.GONE);
            holder.textView3.setVisibility(View.GONE);
            holder.textView4.setVisibility(View.GONE);
            holder.textView5.setVisibility(View.GONE);
            holder.textView6.setVisibility(View.GONE);
            holder.textView7.setVisibility(View.GONE);

            TypedValue tv = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.color_text, tv, true);
            holder.textView1.setTextColor(context.getResources().getColor(tv.resourceId));
            holder.textView1.setTextSize(20);
            int size;
            String text;
            if (position == 0) {
                size = todayList.size();
                if (size == 0) {
                    text = "今天没有课";
                } else {
                    text = "今天有" + size + "节课";
                }
            } else {
                size = tomorrowList.size();
                if (size == 0) {
                    text = "明天没有课";
                } else {
                    text = "明天有" + size + "节课";
                }
            }
            holder.textView1.setText(text);
            holder.textView1.setTypeface(Typeface.DEFAULT);
        } else {
            //课程显示
            Schedule schedule;
            if (position <= todayList.size()) {
                schedule = todayList.get(position - 1);
            } else {
                schedule = tomorrowList.get(position - todayList.size() - 2);
            }
            holder.textView2.setVisibility(View.VISIBLE);
            holder.textView3.setVisibility(View.VISIBLE);
            holder.textView4.setVisibility(View.VISIBLE);
            holder.textView5.setVisibility(View.VISIBLE);
            holder.textView6.setVisibility(View.VISIBLE);
            holder.textView7.setVisibility(View.VISIBLE);
            holder.textView1.setTextSize(18);
            holder.textView1.setTextColor(0xFF000000);
            if ((int) schedule.getExtras().get(ExamBean.TYPE) == 2) { //考试安排
                ExamBean nowExam = new ExamBean();
                nowExam.setFromSchedule(schedule);
                holder.textView1.setText((nowExam.examType != null && nowExam.examType.isEmpty()) ? "（考试）" : "(" + nowExam.examType + ")" + nowExam.getName());
                holder.textView2.setText("课号：" + nowExam.getNumber());
                holder.textView3.setText("教师：" + nowExam.getTeacher());
                SpannableStringBuilder builder = new SpannableStringBuilder();
                String roomPart[] = (nowExam.getRoom().isEmpty() ? "未公布-座位未公布" : nowExam.getRoom()).split("-");
                String examRoom = roomPart[0];
                String examSeat = roomPart.length == 2 ? roomPart[1] : "座位未公布";
                builder.append("教室：");
                SpannableString spannableString = new SpannableString(examRoom);
                spannableString.setSpan(new ForegroundColorSpan(context.getColor(R.color.color_room)),
                        0,
                        spannableString.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new StyleSpan(Typeface.BOLD),
                        0,
                        spannableString.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append(spannableString);
                builder.append("-");
                spannableString = new SpannableString(examSeat);
                spannableString.setSpan(new ForegroundColorSpan(context.getColor(R.color.color_seat)),
                        0,
                        spannableString.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new StyleSpan(Typeface.BOLD),
                        0,
                        spannableString.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append(spannableString);
                holder.textView4.setText(builder);
                spannableString = new SpannableString("时间：" + nowExam.getTime());
                spannableString.setSpan(new ForegroundColorSpan(context.getColor(R.color.color_clock)), 3, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new StyleSpan(Typeface.BOLD), 3, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.textView5.setText(spannableString);
                builder = new SpannableStringBuilder();
                builder.append("日期：");
                spannableString = new SpannableString((nowExam.getDate() == null ? nowExam.getDateString() : TimeUtil.timeFormat(nowExam.getDate())));
                spannableString.setSpan(new ForegroundColorSpan(context.getColor(R.color.color_date)),
                        0,
                        spannableString.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new StyleSpan(Typeface.BOLD),
                        0,
                        spannableString.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append(spannableString);
                spannableString = new SpannableString(" 第" + nowExam.getWeek() + "周 ");
                spannableString.setSpan(new ForegroundColorSpan(context.getColor(R.color.color_day)),
                        0,
                        spannableString.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new StyleSpan(Typeface.BOLD),
                        0,
                        spannableString.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append(spannableString);
                spannableString = new SpannableString(TimeUtil.whichDay(nowExam.getDay()));
                spannableString.setSpan(new ForegroundColorSpan(context.getColor(R.color.color_time)),
                        0,
                        spannableString.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new StyleSpan(Typeface.BOLD),
                        0,
                        spannableString.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append(spannableString);
                if (nowExam.getComm().isEmpty()) {
                    holder.textView6.setVisibility(View.GONE);
                    holder.textView7.setText(builder);
                } else {
                    holder.textView6.setVisibility(View.VISIBLE);
                    holder.textView6.setText(builder);
                    spannableString = new SpannableString("备注信息：\n" + nowExam.getComm());
                    spannableString.setSpan(new ForegroundColorSpan(context.getColor(R.color.color_remark)), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    holder.textView7.setText(spannableString);
                }
            } else { //理论课和课内实验
                CourseBean courseBean = new CourseBean();
                courseBean.setFromSchedule(schedule);
                if (schedule.getStart() == starting && position <= todayList.size()) {
                    SpannableString spannableString = new SpannableString(courseBean.getName() + " 上课中");
                    spannableString.setSpan(new ForegroundColorSpan(context.getColor(R.color.app_green)), spannableString.length() - 3, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    holder.textView1.setText(spannableString);
                } else {
                    holder.textView1.setText(courseBean.getName());
                }
                if (courseBean.getTeacher().isEmpty()) {
                    holder.textView3.setVisibility(View.GONE);
                } else {
                    holder.textView3.setText("教师：" + courseBean.getTeacher());
                }
                if (courseBean.getRoom().isEmpty()) {
                    holder.textView4.setVisibility(View.GONE);
                } else {
                    SpannableString spannableString = new SpannableString("教室：" + courseBean.getRoom());
                    spannableString.setSpan(new ForegroundColorSpan(context.getColor(R.color.color_room)), 3, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new StyleSpan(Typeface.BOLD), 3, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    holder.textView4.setText(spannableString);
                }
                StringBuilder section = new StringBuilder();
                if (courseBean.courseRangeVersion == 1) {
                    int n = courseBean.getTime();
                    if (n == 7) {
                        n = 0;
                    }
                    section.append(n + ", ");
                } else {
                    int start = courseBean.start;
                    int end = courseBean.end;
                    for (int i = start; i <= end; i++) {
                        if (i == 5) {
                            section.append("中午, ");
                        }
                        if (i % 2 == 0) {
                            section.append((i + (i < 5 ? 1 : 0)) / 2 + ", ");
                        }
                    }
                }
                SpannableStringBuilder builder = new SpannableStringBuilder();
                builder.append("时间：");
                SpannableString spannableString = new SpannableString(TimeUtil.whichDay(courseBean.getDay()));
                spannableString.setSpan(new ForegroundColorSpan(context.getColor(R.color.color_day)),
                        0,spannableString.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new StyleSpan(Typeface.BOLD),
                        0,spannableString.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append(spannableString);
                spannableString = new SpannableString(" 第" + section.toString().substring(0, max(0, section.length() - 2)) + "大节 ");
                spannableString.setSpan(new ForegroundColorSpan(context.getColor(R.color.color_time)),
                        0,spannableString.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new StyleSpan(Typeface.BOLD),
                        0,spannableString.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append(spannableString);
                if (courseBean.getCourseTime() != null) {
                    spannableString = new SpannableString("(" + courseBean.getCourseTime() + ")");
                    spannableString.setSpan(new ForegroundColorSpan(context.getColor(R.color.color_clock)),
                            0,spannableString.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new StyleSpan(Typeface.BOLD),
                            0,spannableString.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.append(spannableString);
                }
                holder.textView5.setText(builder);

                if (courseBean.getRemarks().isEmpty()) {
                    holder.textView6.setVisibility(View.GONE);
                    holder.textView7.setText("周次：" + courseBean.getWeekStart() + "-" + courseBean.getWeekEnd() + "周");
                } else {
                    holder.textView6.setVisibility(View.VISIBLE);
                    holder.textView6.setText("周次：" + courseBean.getWeekStart() + "-" + courseBean.getWeekEnd() + "周");
                    spannableString = new SpannableString("备注信息：\n" + courseBean.getRemarks());
                    spannableString.setSpan(new ForegroundColorSpan(context.getColor(R.color.color_remark)), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    holder.textView7.setText(spannableString);
                }

                if (courseBean.isLab()) { //课内实验
                    holder.textView2.setText("名称：" + courseBean.getLabName());
                    holder.lablessonCode.setVisibility(View.VISIBLE);
                    holder.lablessonCode.setText("课号：" + courseBean.getNumber());
                } else { //理论课
                    if (courseBean.getNumber().isEmpty()) {
                        holder.textView2.setVisibility(View.GONE);
                    } else {
                        holder.textView2.setVisibility(View.VISIBLE);
                        holder.textView2.setText("课号：" + courseBean.getNumber());
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return todayList.size() + tomorrowList.size() + 2;
    }

    static class ClassDetailViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2, lablessonCode, textView3, textView4, textView5, textView6, textView7;

        public ClassDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.detail_text_1);
            textView2 = itemView.findViewById(R.id.detail_text_2);
            lablessonCode = itemView.findViewById(R.id.detail_text_3);
            lablessonCode.setVisibility(View.GONE);
            textView3 = itemView.findViewById(R.id.detail_text_4);
            textView4 = itemView.findViewById(R.id.detail_text_5);
            textView5 = itemView.findViewById(R.id.detail_text_6);
            textView6 = itemView.findViewById(R.id.detail_text_7);
            textView7 = itemView.findViewById(R.id.detail_text_8);
        }
    }
}
