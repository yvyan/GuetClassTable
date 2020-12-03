package top.yvyan.guettable.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import top.yvyan.guettable.R;
import top.yvyan.guettable.bean.ExamScoreBean;

public class ExamScoreAdapter extends RecyclerView.Adapter<ExamScoreAdapter.ExamScoreViewHolder> {

    List<ExamScoreBean> examScoreBeans;

    public ExamScoreAdapter(List<ExamScoreBean> examScoreBeans) {
        this.examScoreBeans = examScoreBeans;
    }

    @NonNull
    @Override
    public ExamScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.examscore_cardview,parent,false);
        return new ExamScoreViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamScoreAdapter.ExamScoreViewHolder holder, int position) {
        holder.examscore_name.setText(examScoreBeans.get(position).getName());
        holder.examscore_totalScore.setText(String.valueOf(examScoreBeans.get(position).getTotalScore()));
        holder.examscore_usuallyScore.setText(String.valueOf(examScoreBeans.get(position).getUsuallyScore()));
        holder.examscore_experimentScore.setText(String.valueOf(examScoreBeans.get(position).getExperimentScore()));
        holder.examscore_checkScore.setText(String.valueOf(examScoreBeans.get(position).getCheckScore()));
    }

    @Override
    public int getItemCount() {
        return examScoreBeans.size();
    }

    public class ExamScoreViewHolder extends RecyclerView.ViewHolder {
        TextView examscore_name, examscore_totalScore, examscore_usuallyScore, examscore_experimentScore, examscore_checkScore;
        public ExamScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            examscore_name = itemView.findViewById(R.id.examscore_name);
            examscore_totalScore = itemView.findViewById(R.id.examscore_totalScore);
            examscore_usuallyScore = itemView.findViewById(R.id.examscore_usuallyScore);
            examscore_experimentScore = itemView.findViewById(R.id.examscore_experimentScore);
            examscore_checkScore = itemView.findViewById(R.id.examscore_checkScore);
        }
    }
}
