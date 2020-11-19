package top.yvyan.guettable.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import top.yvyan.guettable.R;
import top.yvyan.guettable.bean.CourseBean;
import top.yvyan.guettable.data.DayClassData;

public class ClassDetailAdapter extends RecyclerView.Adapter<ClassDetailAdapter.ClassDetailViewHolder> {
    DayClassData dayClassData = DayClassData.newInstance();

    @NonNull
    @Override
    public ClassDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.day_class_detail_cardview,parent,false);
        return new ClassDetailViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassDetailViewHolder holder, int position) {
        if(dayClassData.getCourseBeans().get(position).isLab()) {
            holder.tisLab.setText("实验");
            holder.tname.setText(dayClassData.getCourseBeans().get(position).getName());
            holder.tlibname.setText(dayClassData.getCourseBeans().get(position).getLibName());
            holder.troom.setText(dayClassData.getCourseBeans().get(position).getRoom());
            holder.tday.setText(String.valueOf(dayClassData.getCourseBeans().get(position).getDay()));
            holder.ttime.setText(String.valueOf(dayClassData.getCourseBeans().get(position).getTime()));
            holder.tweekStart.setText(String.valueOf(dayClassData.getCourseBeans().get(position).getWeekStart()));
            holder.tweekEnd.setText(String.valueOf(dayClassData.getCourseBeans().get(position).getWeekEnd()));
        }
        else {
            holder.tisLab.setText("理论");
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class ClassDetailViewHolder extends RecyclerView.ViewHolder {
        TextView tisLab, tname, tlibname, troom, tday, ttime, tweekStart, tweekEnd;
        public ClassDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            tisLab = itemView.findViewById(R.id.isLab);
            tname = itemView.findViewById(R.id.name);
            tlibname = itemView.findViewById(R.id.libname);
            troom = itemView.findViewById(R.id.room);
            tday = itemView.findViewById(R.id.day);
            ttime = itemView.findViewById(R.id.time);
            tweekStart = itemView.findViewById(R.id.weekStart);
            tweekEnd = itemView.findViewById(R.id.weekEnd);

        }
    }
}
