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

    private List<ExamScoreBean> examScoreBeans;

    public ExamScoreAdapter(List<ExamScoreBean> examScoreBeans) {
        this.examScoreBeans = examScoreBeans;
    }

    @NonNull
    @Override
    public ExamScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.score_cardview,parent,false);
        return new ExamScoreViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamScoreAdapter.ExamScoreViewHolder holder, int position) {
        if (position == 0 || !examScoreBeans.get(position).getTerm().equals(examScoreBeans.get(position - 1).getTerm())) {
            holder.headerTerm.setText(examScoreBeans.get(position).getTerm());
            holder.headerTerm.setVisibility(View.VISIBLE);
        } else {
            holder.headerTerm.setVisibility(View.GONE);
        }
        holder.examScoreName.setText(examScoreBeans.get(position).getName());
        holder.examScoreScore.setText(String.valueOf(examScoreBeans.get(position).getScore()));
        holder.examScoreUsually.setText(String.valueOf(examScoreBeans.get(position).getUsuallyScore()));
        holder.examScoreExperiment.setText(String.valueOf(examScoreBeans.get(position).getExperimentScore()));
        holder.examScoreCheck.setText(String.valueOf(examScoreBeans.get(position).getCheckScore()));
    }

    @Override
    public int getItemCount() {
        return examScoreBeans.size();
    }

    public class ExamScoreViewHolder extends RecyclerView.ViewHolder {
        TextView headerTerm, examScoreName, examScoreScore, examScoreUsually, examScoreExperiment, examScoreCheck;
        public ExamScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            headerTerm = itemView.findViewById(R.id.header_term);
            examScoreName = itemView.findViewById(R.id.ScoreName);
            examScoreScore = itemView.findViewById(R.id.ScoreFinal);
            examScoreUsually = itemView.findViewById(R.id.ScoreUsually);
            examScoreExperiment = itemView.findViewById(R.id.ScoreExperiment);
            examScoreCheck = itemView.findViewById(R.id.ScoreCheck);
        }
    }
}
