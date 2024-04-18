package top.yvyan.guettable.adapter;

import static java.lang.Math.max;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhuangfei.timetable.model.Schedule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import top.yvyan.guettable.R;
import top.yvyan.guettable.activity.DetailActivity;
import top.yvyan.guettable.bean.CourseBean;
import top.yvyan.guettable.bean.ExamBean;
import top.yvyan.guettable.data.ScheduleData;
import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.TimeUtil;
import top.yvyan.guettable.widget.WidgetUtil;

public class ClassDetailAdapter extends RecyclerView.Adapter<ClassDetailAdapter.ClassDetailViewHolder> {
    private final List<Schedule> schedules;
    private final int week;
    private final Activity activity;
    private final Intent intent;

    public ClassDetailAdapter(Intent intent, Activity activity, List<Schedule> schedules, int week) {
        this.schedules = schedules;
        this.week = week;
        this.activity = activity;
        this.intent = intent;
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
        if ((int) schedules.get(position).getExtras().get(ExamBean.TYPE) == 2) { //考试安排
            ExamBean nowExam = new ExamBean();
            nowExam.setFromSchedule(schedules.get(position));
            holder.textView1.setText(nowExam.getName());
            holder.textView2.setText("课号：" + nowExam.getNumber());
            holder.textView3.setText("教师：" + nowExam.getTeacher());
            SpannableString spannableString = new SpannableString("教室：" + (nowExam.getRoom().isEmpty() ? "未公布" : nowExam.getRoom()));
            spannableString.setSpan(new ForegroundColorSpan(activity.getColor(R.color.color_room)), 3, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), 3, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.textView4.setText(spannableString);
            spannableString = new SpannableString("时间：" + nowExam.getTime());
            spannableString.setSpan(new ForegroundColorSpan(activity.getColor(R.color.color_clock)), 3, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), 3, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.textView5.setText(spannableString);
            List<String> FragmentstartText = new ArrayList<>(Arrays.asList("日期：", (nowExam.getDate() == null ? nowExam.getDateString() : TimeUtil.timeFormat(nowExam.getDate())), " 第" + nowExam.getWeek() + "周 ",
                    TimeUtil.whichDay(nowExam.getDay())));
            spannableString = new SpannableString(String.join("", FragmentstartText));
            spannableString.setSpan(new ForegroundColorSpan(activity.getColor(R.color.color_date)),
                    FragmentstartText.get(0).length(),
                    FragmentstartText.get(0).length() +
                            FragmentstartText.get(1).length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new StyleSpan(Typeface.BOLD),
                    FragmentstartText.get(0).length(),
                    FragmentstartText.get(0).length() +
                            FragmentstartText.get(1).length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(activity.getColor(R.color.color_day)),
                    FragmentstartText.get(0).length() +
                            FragmentstartText.get(1).length(),
                    FragmentstartText.get(0).length() +
                            FragmentstartText.get(1).length() +
                            FragmentstartText.get(2).length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new StyleSpan(Typeface.BOLD),
                    FragmentstartText.get(0).length() +
                            FragmentstartText.get(1).length(),
                    FragmentstartText.get(0).length() +
                            FragmentstartText.get(1).length() +
                            FragmentstartText.get(2).length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(activity.getColor(R.color.color_time)),
                    FragmentstartText.get(0).length() +
                            FragmentstartText.get(1).length() +
                            FragmentstartText.get(2).length(),
                    FragmentstartText.get(0).length() +
                            FragmentstartText.get(1).length() +
                            FragmentstartText.get(2).length() +
                            FragmentstartText.get(3).length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new StyleSpan(Typeface.BOLD),
                    FragmentstartText.get(0).length() +
                            FragmentstartText.get(1).length() +
                            FragmentstartText.get(2).length(),
                    FragmentstartText.get(0).length() +
                            FragmentstartText.get(1).length() +
                            FragmentstartText.get(2).length() +
                            FragmentstartText.get(3).length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (nowExam.getComm().isEmpty()) {
                holder.textView6.setVisibility(View.GONE);
                holder.textView7.setText(spannableString);
            } else {
                holder.textView6.setVisibility(View.VISIBLE);
                holder.textView6.setText(spannableString);
                spannableString = new SpannableString("备注信息：\n" + nowExam.getComm());
                spannableString.setSpan(new ForegroundColorSpan(activity.getColor(R.color.color_day)), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0,6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.textView7.setText(spannableString);
            }
        } else { //理论课和课内实验
            CourseBean courseBean = new CourseBean();
            courseBean.setFromSchedule(schedules.get(position));
            holder.textView1.setText(courseBean.getName());
            if (courseBean.getTeacher().isEmpty()) {
                holder.textView3.setVisibility(View.GONE);
            } else {
                holder.textView3.setText("教师：" + courseBean.getTeacher());
            }
            if (courseBean.getRoom().isEmpty()) {
                holder.textView4.setVisibility(View.GONE);
            } else {
                SpannableString spannableString = new SpannableString("教室：" + courseBean.getRoom());
                spannableString.setSpan(new ForegroundColorSpan(activity.getColor(R.color.color_room)), 3, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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

            List<String> FragmentstartText = new ArrayList<>(Arrays.asList("时间：", TimeUtil.whichDay(courseBean.getDay()), " 第" + section.toString().substring(0, max(0, section.length() - 2)) + "大节 "));
            if (courseBean.getCourseTime() != null) {
                FragmentstartText.add("(" + courseBean.getCourseTime() + ")");
            }
            SpannableString spannableString = new SpannableString(String.join("", FragmentstartText));
            spannableString.setSpan(new ForegroundColorSpan(activity.getColor(R.color.color_day)),
                    FragmentstartText.get(0).length(),
                    FragmentstartText.get(0).length() +
                            FragmentstartText.get(1).length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new StyleSpan(Typeface.BOLD),
                    FragmentstartText.get(0).length(),
                    FragmentstartText.get(0).length() +
                            FragmentstartText.get(1).length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(activity.getColor(R.color.color_time)),
                    FragmentstartText.get(0).length() +
                            FragmentstartText.get(1).length(),
                    FragmentstartText.get(0).length() +
                            FragmentstartText.get(1).length() +
                            FragmentstartText.get(2).length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new StyleSpan(Typeface.BOLD),
                    FragmentstartText.get(0).length() +
                            FragmentstartText.get(1).length(),
                    FragmentstartText.get(0).length() +
                            FragmentstartText.get(1).length() +
                            FragmentstartText.get(2).length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (courseBean.getCourseTime() != null) {
                spannableString.setSpan(new ForegroundColorSpan(activity.getColor(R.color.color_clock)),
                        FragmentstartText.get(0).length() +
                                FragmentstartText.get(1).length() +
                                FragmentstartText.get(2).length(),
                        FragmentstartText.get(0).length() +
                                FragmentstartText.get(1).length() +
                                FragmentstartText.get(2).length() +
                                FragmentstartText.get(3).length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new StyleSpan(Typeface.BOLD),
                        FragmentstartText.get(0).length() +
                                FragmentstartText.get(1).length() +
                                FragmentstartText.get(2).length(),
                        FragmentstartText.get(0).length() +
                                FragmentstartText.get(1).length() +
                                FragmentstartText.get(2).length() +
                                FragmentstartText.get(3).length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            holder.textView5.setText(spannableString);

            if (courseBean.getRemarks().isEmpty()) {
                holder.textView6.setVisibility(View.GONE);
                holder.textView7.setText("周次：" + courseBean.getWeekStart() + "-" + courseBean.getWeekEnd() + "周");
            } else {
                holder.textView6.setVisibility(View.VISIBLE);
                holder.textView6.setText("周次：" + courseBean.getWeekStart() + "-" + courseBean.getWeekEnd() + "周");
                spannableString = new SpannableString("备注信息：\n" + courseBean.getRemarks());
                spannableString.setSpan(new ForegroundColorSpan(activity.getColor(R.color.color_day)), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0,6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.textView7.setText(spannableString);
            }

            if (courseBean.isLab()) { //课内实验
                holder.textView2.setText("名称：" + courseBean.getLabName());
                holder.labLessonCode.setVisibility(View.VISIBLE);
                holder.labLessonCode.setText("课号：" + courseBean.getNumber());
            } else { //理论课
                if (courseBean.getNumber().isEmpty()) {
                    holder.textView2.setVisibility(View.GONE);
                } else {
                    holder.textView2.setVisibility(View.VISIBLE);
                    holder.textView2.setText("课号：" + courseBean.getNumber());
                }
            }

            if (courseBean.getId() != 0) {
                holder.imageView.setVisibility(View.VISIBLE);
                holder.imageView.setOnClickListener(view -> {
                    ScheduleData.deleteUserCourse(courseBean.getId());
                    schedules.remove(position);
                    //删除动画
                    notifyItemRemoved(position);
                    notifyDataSetChanged();
                    activity.setResult(DetailActivity.ALTER, intent);
                    WidgetUtil.notifyWidgetUpdate(activity);
                    AppUtil.reportFunc(activity, activity.getString(R.string.course_delete));
                });
            } else {
                holder.imageView.setVisibility(View.GONE);
            }
        }
        if (schedules.get(position).getWeekList().contains(week)) {
            holder.card.setBackgroundColor(0xCF94D6F9);
        } else {
            holder.card.setBackgroundColor(0xC0ffffff);
        }
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }

    static class ClassDetailViewHolder extends RecyclerView.ViewHolder {
        View card;
        TextView textView1, textView2, labLessonCode, textView3, textView4, textView5, textView6, textView7;
        ImageView imageView;

        public ClassDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.detail_card);
            textView1 = itemView.findViewById(R.id.detail_text_1);
            textView2 = itemView.findViewById(R.id.detail_text_2);
            labLessonCode = itemView.findViewById(R.id.detail_text_3);
            labLessonCode.setVisibility(View.GONE);
            textView3 = itemView.findViewById(R.id.detail_text_4);
            textView4 = itemView.findViewById(R.id.detail_text_5);
            textView5 = itemView.findViewById(R.id.detail_text_6);
            textView6 = itemView.findViewById(R.id.detail_text_7);
            textView7 = itemView.findViewById(R.id.detail_text_8);
            imageView = itemView.findViewById(R.id.detail_imageView);
        }
    }
}
