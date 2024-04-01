package top.yvyan.guettable.adapter;

import static java.lang.Math.max;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhuangfei.timetable.model.Schedule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import top.yvyan.guettable.R;
import top.yvyan.guettable.bean.CourseBean;
import top.yvyan.guettable.util.TimeUtil;

public class LibAdapter extends RecyclerView.Adapter<LibAdapter.LibViewHolder> {
    private final List<Schedule> schedules;
    private final int week;

    private Context context;

    public LibAdapter(List<Schedule> schedules, int week) {
        this.schedules = schedules;
        this.week = week;
    }

    @NonNull
    @Override
    public LibViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.detail_cardview, parent, false);
        return new LibViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LibViewHolder holder, int position) {
        CourseBean courseBean = new CourseBean();
        courseBean.setFromSchedule(schedules.get(position));
        // if (position == 0 || !schedules.get(position - 1).getName().equals(schedules.get(position).getName())) {
        holder.textView1.setVisibility(View.VISIBLE);
        holder.textView1.setText(courseBean.getName());
        holder.lessonCode.setText("课号：" + courseBean.getNumber());
        holder.lessonCode.setVisibility(View.VISIBLE);
        if (courseBean.getTeacher().isEmpty()) {
            holder.textView3.setVisibility(View.GONE);
        } else {
            holder.textView3.setVisibility(View.VISIBLE);
            holder.textView3.setText("教师：" + courseBean.getTeacher());
        }
        if (courseBean.getRoom().isEmpty()) {
            holder.textView4.setVisibility(View.GONE);
        } else {
            holder.textView4.setVisibility(View.VISIBLE);
            SpannableString spannableString = new SpannableString("教室：" + courseBean.getRoom());
            spannableString.setSpan(new ForegroundColorSpan(context.getColor(R.color.color_room)), 3, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), 3, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.textView4.setText(spannableString);
        }
        /*} else {
            holder.textView2.setTextColor(0xff000000);
            holder.lessonCode.setVisibility(View.GONE);
            holder.textView1.setVisibility(View.GONE);
            holder.textView3.setVisibility(View.GONE);
            holder.textView4.setVisibility(View.GONE);
        }*/
        StringBuilder section = new StringBuilder();
        if (courseBean.courseRangeVersion == 1) {
            int n = courseBean.getTime();
            if (n == 7) {
                n = 0;
            }
            section.append(n + ", ");
        } else {
            int start = courseBean.start;
            int end = courseBean.end;
            for (int i = start; i <= end; i++) {
                if (i == 5) {
                    section.append("中午, ");
                }
                if (i % 2 == 0) {
                    section.append((i + (i < 5 ? 1 : 0)) / 2 + ", ");
                }
            }
        }
        List<String> FragmentstartText = new ArrayList<>(Arrays.asList("时间：", TimeUtil.whichDay(courseBean.getDay()), " 第" + section.toString().substring(0, max(0, section.length() - 2)) + "大节 "));
        if (courseBean.getCourseTime() != null) {
            FragmentstartText.add("(" + courseBean.getCourseTime() + ")");
        }
        SpannableString spannableString = new SpannableString(String.join("", FragmentstartText));
        spannableString.setSpan(new ForegroundColorSpan(context.getColor(R.color.color_day)),
                FragmentstartText.get(0).length(),
                FragmentstartText.get(0).length() +
                        FragmentstartText.get(1).length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD),
                FragmentstartText.get(0).length(),
                FragmentstartText.get(0).length() +
                        FragmentstartText.get(1).length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(context.getColor(R.color.color_time)),
                FragmentstartText.get(0).length() +
                        FragmentstartText.get(1).length(),
                FragmentstartText.get(0).length() +
                        FragmentstartText.get(1).length() +
                        FragmentstartText.get(2).length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD),
                FragmentstartText.get(0).length() +
                        FragmentstartText.get(1).length(),
                FragmentstartText.get(0).length() +
                        FragmentstartText.get(1).length() +
                        FragmentstartText.get(2).length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (courseBean.getCourseTime() != null) {
            spannableString.setSpan(new ForegroundColorSpan(context.getColor(R.color.color_clock)),
                    FragmentstartText.get(0).length() +
                            FragmentstartText.get(1).length() +
                            FragmentstartText.get(2).length(),
                    FragmentstartText.get(0).length() +
                            FragmentstartText.get(1).length() +
                            FragmentstartText.get(2).length() +
                            FragmentstartText.get(3).length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new StyleSpan(Typeface.BOLD),
                    FragmentstartText.get(0).length() +
                            FragmentstartText.get(1).length() +
                            FragmentstartText.get(2).length(),
                    FragmentstartText.get(0).length() +
                            FragmentstartText.get(1).length() +
                            FragmentstartText.get(2).length() +
                            FragmentstartText.get(3).length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        holder.textView5.setText(spannableString);
        holder.textView2.setText("名称：" + courseBean.getLabName());
        if (courseBean.getRemarks() != null && !courseBean.getRemarks().isEmpty()) {
            holder.textView6.setVisibility(View.VISIBLE);
            holder.textView6.setText("周次：" + courseBean.getWeekStart() + "-" + courseBean.getWeekEnd() + "周");
            spannableString = new SpannableString("备注信息：\n" + courseBean.getRemarks());
            spannableString.setSpan(new ForegroundColorSpan(context.getColor(R.color.color_day)), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.textView7.setText(spannableString);

        } else {
            holder.textView6.setVisibility(View.GONE);
            holder.textView7.setText("周次：" + courseBean.getWeekStart() + "-" + courseBean.getWeekEnd() + "周");
        }

        if (schedules.get(position).getWeekList().contains(week)) {
            holder.card.setBackgroundColor(0xCF94D6F9);
        } else {
            holder.card.setBackgroundColor(0xFFFFFFFF);
        }
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }

    static class LibViewHolder extends RecyclerView.ViewHolder {
        View card;
        TextView textView1, lessonCode, textView2, textView3, textView4, textView5, textView6, textView7;

        public LibViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.detail_card);
            textView1 = itemView.findViewById(R.id.detail_text_1);
            textView2 = itemView.findViewById(R.id.detail_text_2);
            lessonCode = itemView.findViewById(R.id.detail_text_3);
            textView3 = itemView.findViewById(R.id.detail_text_4);
            textView4 = itemView.findViewById(R.id.detail_text_5);
            textView5 = itemView.findViewById(R.id.detail_text_6);
            textView6 = itemView.findViewById(R.id.detail_text_7);
            textView7 = itemView.findViewById(R.id.detail_text_8);
        }
    }
}
