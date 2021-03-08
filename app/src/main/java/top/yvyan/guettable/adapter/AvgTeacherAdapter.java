package top.yvyan.guettable.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import top.yvyan.guettable.R;
import top.yvyan.guettable.bean.AvgTeacherBean;

public class AvgTeacherAdapter extends RecyclerView.Adapter<AvgTeacherAdapter.AvgTeacherViewHolder> {
    private final List<AvgTeacherBean> avgTeacherBeans;

    public AvgTeacherAdapter(List<AvgTeacherBean> avgTeacherBeans) {
        this.avgTeacherBeans = avgTeacherBeans;
    }

    @NonNull
    @Override
    public AvgTeacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.teacher_cardview,parent,false);
        return new AvgTeacherViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AvgTeacherViewHolder holder, int position) {
        holder.courseName.setText(avgTeacherBeans.get(position).getCourseName());
        holder.teacherName.setText(avgTeacherBeans.get(position).getTeacherName());
        holder.teacherHint.setText(avgTeacherBeans.get(position).getHint());
    }

    @Override
    public int getItemCount() {
        return avgTeacherBeans.size();
    }

    public static class AvgTeacherViewHolder extends RecyclerView.ViewHolder {
        TextView courseName, teacherName, teacherHint;

        public AvgTeacherViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.course_Name);
            teacherName = itemView.findViewById(R.id.teacher_name);
            teacherHint = itemView.findViewById(R.id.teacher_hint);
        }
    }

}
