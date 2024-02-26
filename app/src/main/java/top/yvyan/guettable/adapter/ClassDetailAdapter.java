package top.yvyan.guettable.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhuangfei.timetable.model.Schedule;

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
            ExamBean examBean = new ExamBean();
            examBean.setFromSchedule(schedules.get(position));
            holder.textView1.setText("(考试)" + examBean.getName());
            holder.textView2.setText("课号：" + examBean.getNumber());
            holder.textView3.setText("教师：" + examBean.getTeacher());
            holder.textView4.setText("教室：" + examBean.getRoom());
            holder.textView5.setText("时间：" + examBean.getTime());
            String date = "日期：" + (examBean.getDate() == null ? examBean.getDateString() : TimeUtil.timeFormat(examBean.getDate())) + "(第" + examBean.getWeek() + "周 " + TimeUtil.whichDay(examBean.getDay()) + ")";
            if (examBean.getComm().isEmpty()) {
                holder.textView6.setVisibility(View.GONE);
                holder.textView7.setText(date);
            } else {
                holder.textView6.setVisibility(View.VISIBLE);
                holder.textView6.setText(date);
                holder.textView7.setText(examBean.getComm());
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
                holder.textView4.setText("教室：" + courseBean.getRoom());
            }
            StringBuilder section=new StringBuilder();
            if (courseBean.courseRangeVersion == 1) {
                int n = courseBean.getTime();
                if (n == 7) {
                    n = 0;
                }
                section.append(n+", ");
            } else {
                int start = courseBean.start;
                int end=courseBean.end;
                for (int i=start;i<=end;i++) {
                    if (i==5) {
                        section.append("中午, ");
                    }
                    if (i%2==0) {
                        section.append((i+(i<5 ? 1 : 0))/2+", ");
                    }
                }
            }
            holder.textView5.setText("时间：" + TimeUtil.whichDay(courseBean.getDay()) + " 第" + section.toString().substring(0,section.length()-2) + "大节");

            if (courseBean.getRemarks().isEmpty()) {
                holder.textView6.setVisibility(View.GONE);
                holder.textView7.setText("周次：" + courseBean.getWeekStart() + "-" + courseBean.getWeekEnd() + "周");
            } else {
                holder.textView6.setVisibility(View.VISIBLE);
                holder.textView6.setText("周次：" + courseBean.getWeekStart() + "-" + courseBean.getWeekEnd() + "周");
                holder.textView7.setText(courseBean.getRemarks());
            }

            if (courseBean.isLab()) { //课内实验
                holder.textView2.setText("名称：" + courseBean.getLabName());
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
        TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7;
        ImageView imageView;

        public ClassDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.detail_card);
            textView1 = itemView.findViewById(R.id.detail_text_1);
            textView2 = itemView.findViewById(R.id.detail_text_2);
            textView3 = itemView.findViewById(R.id.detail_text_3);
            textView4 = itemView.findViewById(R.id.detail_text_4);
            textView5 = itemView.findViewById(R.id.detail_text_5);
            textView6 = itemView.findViewById(R.id.detail_text_6);
            textView7 = itemView.findViewById(R.id.detail_text_7);
            imageView = itemView.findViewById(R.id.detail_imageView);
        }
    }
}
