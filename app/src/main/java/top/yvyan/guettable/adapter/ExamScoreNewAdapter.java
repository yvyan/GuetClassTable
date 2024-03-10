package top.yvyan.guettable.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import top.yvyan.guettable.Gson.ExamScoreNew;
import top.yvyan.guettable.R;

public class ExamScoreNewAdapter extends RecyclerView.Adapter<ExamScoreNewAdapter.ExamScoreViewHolder> {

    private final List<ExamScoreNew.ScoreDetail> scoreData;

    public ExamScoreNewAdapter(ExamScoreNew examScoreNew) {
        List<ExamScoreNew.ScoreDetail> scoreData = new ArrayList<>();
        for (List<ExamScoreNew.ScoreDetail> score : examScoreNew.semesterId2studentGrades.values()) {
            scoreData.addAll(score);
        }
        Collections.sort(scoreData, (a, b) -> {
            if (a.semesterId == b.semesterId) {
                return a.id - b.id;
            } else {
                return b.semesterId - a.semesterId;
            }
        });
        this.scoreData = scoreData;
    }

    @NonNull
    @Override
    public ExamScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.score_cardview_new, parent, false);
        return new ExamScoreViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamScoreNewAdapter.ExamScoreViewHolder holder, int position) {
        ExamScoreNew.ScoreDetail nowScore = scoreData.get(position);
        if (position == 0 || nowScore.semesterId != scoreData.get(position - 1).semesterId) {
            holder.headerTerm.setText(nowScore.semesterName);
            holder.headerTerm.setVisibility(View.VISIBLE);
        } else {
            holder.headerTerm.setVisibility(View.GONE);
        }
        String score = nowScore.courseName + (nowScore.fillAGrace != null ? nowScore.fillAGrace : "");
        holder.examScoreName.setText(score);
        holder.examScoreScore.setText(nowScore.gaGrade);
        holder.courseNo.setText(nowScore.courseCode);
        String remark = nowScore.gradeDetail.replaceAll("<[^>]+>", "").replaceAll("\n", "");
        holder.examScoreRemark.setText(remark);
    }

    @Override
    public int getItemCount() {
        return scoreData.size();
    }

    public static class ExamScoreViewHolder extends RecyclerView.ViewHolder {
        TextView headerTerm, examScoreName, examScoreScore, examScoreRemark, courseNo;

        public ExamScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            headerTerm = itemView.findViewById(R.id.header_term);
            examScoreName = itemView.findViewById(R.id.course_name);
            courseNo = itemView.findViewById(R.id.course_no);
            examScoreScore = itemView.findViewById(R.id.ScoreFinal);
            examScoreRemark = itemView.findViewById(R.id.ScoreRemark);
        }
    }
}
