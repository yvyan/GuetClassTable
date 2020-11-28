package top.yvyan.guettable.adapter;

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

public class ClassDetailAdapter extends RecyclerView.Adapter<ClassDetailAdapter.ClassDetailViewHolder> {
    List<Schedule> schedules;

    public ClassDetailAdapter(List<Schedule> schedules) {
        this.schedules = schedules;
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
        if ((int)schedules.get(position).getExtras().get(ExamBean.TYPE) == 2) { //考试安排
            ExamBean examBean = new ExamBean();
            examBean.setFromSchedule(schedules.get(position));
            holder.textView1.setText("(考试)" + examBean.getName());
            holder.textView2.setText("课号：" + examBean.getNumber());
            holder.textView3.setText("教师：" + examBean.getTeacher());
            holder.textView4.setText("教室：" + examBean.getRoom());
            holder.textView5.setText("时间：" + examBean.getTime());
            holder.textView6.setVisibility(View.GONE);
            holder.textView7.setText("日期：" + TimeUtil.timeFormat(examBean.getDate()) + "(第" + examBean.getWeek() + "周 " + TimeUtil.whichDay(examBean.getDay()) + ")");
        } else { //理论课和课内实验
            CourseBean courseBean = new CourseBean();
            courseBean.setFromSchedule(schedules.get(position));
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
        return schedules.size();
    }

    static class ClassDetailViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7;
        public ClassDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.card_1);
            textView2 = itemView.findViewById(R.id.card_2);
            textView3 = itemView.findViewById(R.id.card_3);
            textView4 = itemView.findViewById(R.id.card_4);
            textView5 = itemView.findViewById(R.id.card_5);
            textView6 = itemView.findViewById(R.id.card_6);
            textView7 = itemView.findViewById(R.id.card_7);
        }
    }
}
