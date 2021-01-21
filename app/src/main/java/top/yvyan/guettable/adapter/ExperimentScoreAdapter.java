package top.yvyan.guettable.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import top.yvyan.guettable.R;
import top.yvyan.guettable.bean.ExperimentScoreBean;

public class ExperimentScoreAdapter extends RecyclerView.Adapter<ExperimentScoreAdapter.ExperimentScoreViewHolder> {
    private List<ExperimentScoreBean> experimentScoreBeans;

    public ExperimentScoreAdapter(List<ExperimentScoreBean> experimentScoreBeans) {
        this.experimentScoreBeans = experimentScoreBeans;
    }

    @NonNull
    @Override
    public ExperimentScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.score_cardview,parent,false);
        return new ExperimentScoreViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExperimentScoreAdapter.ExperimentScoreViewHolder holder, int position) {
        if (position == 0 || !experimentScoreBeans.get(position).getTerm().equals(experimentScoreBeans.get(position - 1).getTerm())) {
            holder.headerTerm.setText(experimentScoreBeans.get(position).getTerm());
            holder.headerTerm.setVisibility(View.VISIBLE);
        } else {
            holder.headerTerm.setVisibility(View.GONE);
        }
        holder.experimentScoreName.setText(experimentScoreBeans.get(position).getName());
        holder.experimentScoreFinal.setText(String.valueOf(experimentScoreBeans.get(position).getFinalScore()));
        holder.experimentScoreUsually.setText(String.valueOf(experimentScoreBeans.get(position).getUsuallyScore()));
        holder.courseNo.setText(experimentScoreBeans.get(position).getNumber());
        holder.experimentScoreCheck.setText(String.valueOf(experimentScoreBeans.get(position).getCheckScore()));
    }

    @Override
    public int getItemCount() {
        return experimentScoreBeans.size();
    }

    public class ExperimentScoreViewHolder extends RecyclerView.ViewHolder {
        TextView headerTerm, experimentScoreName, experimentScoreFinal, experimentScoreUsually, courseNo, experimentScoreCheck;
        public ExperimentScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            headerTerm = itemView.findViewById(R.id.header_term);
            experimentScoreName = itemView.findViewById(R.id.course_name);
            experimentScoreFinal = itemView.findViewById(R.id.ScoreFinal);
            experimentScoreUsually = itemView.findViewById(R.id.ScoreUsually);
            courseNo = itemView.findViewById(R.id.course_no);
            experimentScoreCheck = itemView.findViewById(R.id.ScoreCheck);
        }
    }
}
