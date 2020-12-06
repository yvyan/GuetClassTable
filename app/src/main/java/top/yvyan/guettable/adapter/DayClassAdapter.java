package top.yvyan.guettable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhuangfei.timetable.model.Schedule;

import java.util.List;

import top.yvyan.guettable.R;
import top.yvyan.guettable.bean.CourseBean;
import top.yvyan.guettable.bean.ExamBean;
import top.yvyan.guettable.util.TimeUtil;

public class DayClassAdapter extends RecyclerView.Adapter<DayClassAdapter.ClassDetailViewHolder> {
    private List<Schedule> todayList, tomorrowList;
    private Context context;

    public DayClassAdapter(Context context, List<Schedule> todayList, List<Schedule> tomorrowList) {
        this.context = context;
        this.todayList = todayList;
        this.tomorrowList = tomorrowList;
    }

    @NonNull
    @Override
    public ClassDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.detail_cardview,parent,false);
        return new ClassDetailViewHolder(itemView);
    }

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

            holder.textView1.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.textView1.setTextSize(23);
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
            return;
        }
        //课程显示
        Schedule schedule;
        if (position <= todayList.size()) {
            schedule = todayList.get(position - 1);
        } else {
            schedule = tomorrowList.get(position - todayList.size() - 2);
        }
        if ((int) schedule.getExtras().get(ExamBean.TYPE) == 2) { //考试安排
            ExamBean examBean = new ExamBean();
            examBean.setFromSchedule(schedule);
            holder.textView1.setText("(考试)" + examBean.getName());
            holder.textView2.setText("课号：" + examBean.getNumber());
            holder.textView3.setText("教师：" + examBean.getTeacher());
            holder.textView4.setText("教室：" + examBean.getRoom());
            holder.textView5.setText("时间：" + examBean.getTime());
            holder.textView6.setVisibility(View.GONE);
            holder.textView7.setText("日期：" + TimeUtil.timeFormat(examBean.getDate()) + "(第" + examBean.getWeek() + "周 " + TimeUtil.whichDay(examBean.getDay()) + ")");
        } else { //理论课和课内实验
            CourseBean courseBean = new CourseBean();
            courseBean.setFromSchedule(schedule);
            holder.textView1.setText(courseBean.getName());
            if (courseBean.getTeacher() == null) {
                holder.textView3.setVisibility(View.GONE);
            } else {
                holder.textView3.setText("教师：" + courseBean.getTeacher());
            }
            if (courseBean.getRoom() == null) {
                holder.textView4.setVisibility(View.GONE);
            } else {
                holder.textView4.setText("教室：" + courseBean.getRoom());
            }
            holder.textView5.setText("时间：" + TimeUtil.whichDay(courseBean.getDay()) + " 第" + courseBean.getTime() + "大节");

            if(courseBean.isLab()) { //课内实验
                holder.textView2.setText("名称：" + courseBean.getLibName());
                holder.textView6.setText("周次：" + courseBean.getWeekStart() + "-" + courseBean.getWeekEnd() + "周");
                holder.textView7.setText(courseBean.getRemarks());
            } else { //理论课
                holder.textView2.setText("课号：" + courseBean.getNumber());
                holder.textView6.setVisibility(View.GONE);
                holder.textView7.setText("周次：" + courseBean.getWeekStart() + "-" + courseBean.getWeekEnd() + "周");
            }
        }
    }

    @Override
    public int getItemCount() {
        return todayList.size() + tomorrowList.size() + 2;
    }

    static class ClassDetailViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7;
        public ClassDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.detail_text_1);
            textView2 = itemView.findViewById(R.id.detail_text_2);
            textView3 = itemView.findViewById(R.id.detail_text_3);
            textView4 = itemView.findViewById(R.id.detail_text_4);
            textView5 = itemView.findViewById(R.id.detail_text_5);
            textView6 = itemView.findViewById(R.id.detail_text_6);
            textView7 = itemView.findViewById(R.id.detail_text_7);
        }
    }
}
