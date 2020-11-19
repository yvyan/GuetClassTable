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

public class ClassDetailAdapter extends RecyclerView.Adapter<ClassDetailAdapter.ClassDetailViewHolder> {
    List<CourseBean> CourseInfo = new ArrayList<>();

    public void setCourseInfo(List<CourseBean> CourseInfo) {
        this.CourseInfo = CourseInfo;
    }
    @NonNull
    @Override
    public ClassDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.day_class_detail_cardview,parent,false);
        return new ClassDetailViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassDetailViewHolder holder, int position) {

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
