package top.yvyan.guettable.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import top.yvyan.guettable.R;
import top.yvyan.guettable.bean.PlannedCourseBean;

public class PlannedCourseAdapter extends RecyclerView.Adapter<PlannedCourseAdapter.PlannedCourseViewHolder> {

    List<PlannedCourseBean> plannedCourseBeans;

    public PlannedCourseAdapter(List<PlannedCourseBean> plannedCourseBeans) {
        this.plannedCourseBeans = plannedCourseBeans;
    }

    @NonNull
    @Override
    public PlannedCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.planned_course_cardview,parent,false);
        return new PlannedCourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlannedCourseViewHolder holder, int position) {
        if (position == 0 || !plannedCourseBeans.get(position).getType().equals(plannedCourseBeans.get(position - 1).getType())) {
            holder.headerName.setText(plannedCourseBeans.get(position).getTypeName());
            holder.headerName.setVisibility(View.VISIBLE);
        } else {
            holder.headerName.setVisibility(View.GONE);
        }
        holder.courseName.setText(plannedCourseBeans.get(position).getName());
        holder.credits.setText(plannedCourseBeans.get(position).getCredits());
        holder.degree.setText(plannedCourseBeans.get(position).getDegree());
    }

    @Override
    public int getItemCount() {
        return plannedCourseBeans.size();
    }

    public static class PlannedCourseViewHolder extends RecyclerView.ViewHolder {
        TextView headerName, courseName, credits, degree;

        public PlannedCourseViewHolder(@NonNull View itemView) {
            super(itemView);
            headerName = itemView.findViewById(R.id.planned_course_header_name);
            courseName = itemView.findViewById(R.id.planned_course_name);
            credits = itemView.findViewById(R.id.planned_course_credits);
            degree = itemView.findViewById(R.id.planned_course_degree);
        }
    }
}
