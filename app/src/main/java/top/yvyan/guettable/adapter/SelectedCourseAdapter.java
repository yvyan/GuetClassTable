package top.yvyan.guettable.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import top.yvyan.guettable.R;
import top.yvyan.guettable.bean.SelectedCourseBean;

public class SelectedCourseAdapter extends RecyclerView.Adapter<SelectedCourseAdapter.SelectedCourseViewHolder> {

    List<SelectedCourseBean> dataList;

    public SelectedCourseAdapter(List<SelectedCourseBean> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public SelectedCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.selected_course_cardview, parent, false);
        return new SelectedCourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedCourseViewHolder holder, int position) {
        holder.courseCredit.setText(String.valueOf(dataList.get(position).getCourseCredit()));
        holder.courseName.setText(dataList.get(position).getCourseName());
        holder.courseType.setText(dataList.get(position).getSelectType());
        holder.courseQuality.setText(dataList.get(position).getCourseQuality());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class SelectedCourseViewHolder extends RecyclerView.ViewHolder {

        private final TextView courseCredit;
        private final TextView courseName;
        private final TextView courseType;
        private final TextView courseQuality;

        public SelectedCourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseCredit = itemView.findViewById(R.id.selected_courseCredit);
            courseName = itemView.findViewById(R.id.selected_courseName);
            courseType = itemView.findViewById(R.id.selected_selectType);
            courseQuality = itemView.findViewById(R.id.selected_courseQuality);
        }
    }
}
