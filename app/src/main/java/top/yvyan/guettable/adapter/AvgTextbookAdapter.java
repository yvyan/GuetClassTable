package top.yvyan.guettable.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import top.yvyan.guettable.R;
import top.yvyan.guettable.bean.AvgTextbookBean;

public class AvgTextbookAdapter extends RecyclerView.Adapter<AvgTextbookAdapter.AvgTextbookViewHolder> {

    private List<AvgTextbookBean> avgTextbookBeans;

    public AvgTextbookAdapter(List<AvgTextbookBean> avgTextbookBeans) {
        this.avgTextbookBeans = avgTextbookBeans;
    }

    @NonNull
    @Override
    public AvgTextbookAdapter.AvgTextbookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.textbook_cardview, parent, false);
        return new AvgTextbookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AvgTextbookAdapter.AvgTextbookViewHolder holder, int position) {
        holder.courseName.setText(avgTextbookBeans.get(position).getCourseName());
        holder.textbookName.setText(avgTextbookBeans.get(position).getTextbookName());
        holder.textbookHint.setText(avgTextbookBeans.get(position).getHint());
    }

    @Override
    public int getItemCount() {
        return avgTextbookBeans.size();
    }

    static class AvgTextbookViewHolder extends RecyclerView.ViewHolder {
        TextView courseName, textbookName, textbookHint;

        public AvgTextbookViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.textbook_course_Name);
            textbookName = itemView.findViewById(R.id.textbook_name);
            textbookHint = itemView.findViewById(R.id.textbook_hint);
        }
    }
}
