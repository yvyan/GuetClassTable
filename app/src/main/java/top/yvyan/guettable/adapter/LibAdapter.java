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
import top.yvyan.guettable.util.TimeUtil;

public class LibAdapter extends RecyclerView.Adapter<LibAdapter.LibViewHolder> {
    private List<Schedule> schedules;
    private int week;

    public LibAdapter(List<Schedule> schedules, int week) {
        this.schedules = schedules;
        this.week = week;
    }

    @NonNull
    @Override
    public LibViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.detail_cardview,parent,false);
        return new LibViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LibViewHolder holder, int position) {
        CourseBean courseBean = new CourseBean();
        courseBean.setFromSchedule(schedules.get(position));
        if (position == 0 || !schedules.get(position - 1).getName().equals(schedules.get(position).getName())) {
            holder.textView1.setVisibility(View.VISIBLE);
            holder.textView1.setText(courseBean.getName());
            if (courseBean.getTeacher() == null) {
                holder.textView3.setVisibility(View.GONE);
            } else {
                holder.textView3.setVisibility(View.VISIBLE);
                holder.textView3.setText("教师：" + courseBean.getTeacher());
            }
            if (courseBean.getRoom() == null) {
                holder.textView4.setVisibility(View.GONE);
            } else {
                holder.textView4.setVisibility(View.VISIBLE);
                holder.textView4.setText("教室：" + courseBean.getRoom());
            }
            holder.textView2.setTextColor(0x8a000000);
        } else {
            holder.textView2.setTextColor(0xff000000);
            holder.textView1.setVisibility(View.GONE);
            holder.textView3.setVisibility(View.GONE);
            holder.textView4.setVisibility(View.GONE);
        }
        int n = courseBean.getTime();
        if (n == 7) {
            n = 0;
        }
        holder.textView5.setText("时间：" + TimeUtil.whichDay(courseBean.getDay()) + " 第" + n + "大节");
        holder.textView2.setText("名称：" + courseBean.getLibName());
        if ("".equals(courseBean.getRemarks())) {
            holder.textView6.setVisibility(View.GONE);
            holder.textView7.setText("周次：" + courseBean.getWeekStart() + "-" + courseBean.getWeekEnd() + "周");
        } else {
            holder.textView6.setVisibility(View.VISIBLE);
            holder.textView6.setText("周次：" + courseBean.getWeekStart() + "-" + courseBean.getWeekEnd() + "周");
            holder.textView7.setText(courseBean.getRemarks());
        }

        if (schedules.get(position).getWeekList().contains(week)) {
            holder.card.setBackgroundColor(0xCF94D6F9);
        } else {
            holder.card.setBackgroundColor(0xFFFFFFFF);
        }
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }

    static class LibViewHolder extends RecyclerView.ViewHolder {
        View card;
        TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7;
        public LibViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.detail_card);
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
