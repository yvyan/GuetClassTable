package top.yvyan.guettable.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import top.yvyan.guettable.Gson.ExamScoreNew;
import top.yvyan.guettable.R;
import top.yvyan.guettable.bean.SelectedCourseBean;

public class SelectedCourseAdapter extends RecyclerView.Adapter<SelectedCourseAdapter.SelectedCourseViewHolder> {

    List<SelectedCourseBean> dataList;

    private Context context;

    public SelectedCourseAdapter(List<SelectedCourseBean> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public SelectedCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.selected_course_cardview, parent, false);
        return new SelectedCourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedCourseViewHolder holder, int position) {
        SelectedCourseBean now = dataList.get(position);
        if (position == 0 || now.semesterId != dataList.get(position - 1).semesterId) {
            holder.headerTerm.setText(now.getSemester());
            holder.headerTerm.setVisibility(View.VISIBLE);
        } else {
            holder.headerTerm.setVisibility(View.GONE);
        }
        holder.courseCredit.setText(String.valueOf(now.getCourseCredit()));
        holder.courseName.setText(now.getCourseName());
        holder.courseType.setText(now.getSelectType());
        holder.courseQuality.setText(now.getCourseQuality());
        holder.courseCode.setText(now.courseCode);
        String lessonCode = (now.teacher != null && !now.teacher.isEmpty() ? now.teacher + "-" : "") + now.code;
        holder.courseTea.setText(lessonCode);
        if(now.remark!=null && !now.remark.isEmpty()) {
            holder.comment.setVisibility(View.VISIBLE);
            SpannableString spannableString = new SpannableString("备注信息：\n" + now.remark);
            spannableString.setSpan(new ForegroundColorSpan(context.getColor(R.color.color_day)), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0,6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.comment.setText(spannableString);
        } else {
            holder.comment.setVisibility(View.GONE);
        }
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
        private final TextView headerTerm;

        private final TextView courseCode;

        private final TextView courseTea;

        private final TextView comment;

        public SelectedCourseViewHolder(@NonNull View itemView) {
            super(itemView);
            headerTerm = itemView.findViewById(R.id.header_term);
            courseCredit = itemView.findViewById(R.id.selected_courseCredit);
            courseName = itemView.findViewById(R.id.course_name);
            courseCode = itemView.findViewById(R.id.course_no);
            courseTea = itemView.findViewById(R.id.course_tea);
            courseType = itemView.findViewById(R.id.selected_selectType);
            courseQuality = itemView.findViewById(R.id.selected_courseQuality);
            comment = itemView.findViewById(R.id.comment);
        }
    }
}
