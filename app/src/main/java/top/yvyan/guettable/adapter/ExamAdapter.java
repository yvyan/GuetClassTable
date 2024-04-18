package top.yvyan.guettable.adapter;

import static java.lang.Math.max;

import android.annotation.SuppressLint;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import top.yvyan.guettable.R;
import top.yvyan.guettable.bean.ExamBean;
import top.yvyan.guettable.util.TimeUtil;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ExamViewHolder> {

    List<ExamBean> examBeans;

    private Context context;
    public ExamAdapter(List<ExamBean> examBeans) {
        this.examBeans = examBeans;
    }

    @NonNull
    @Override
    public ExamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.detail_cardview,parent,false);
        return new ExamViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ExamViewHolder holder, int position) {
         ExamBean nowExam = examBeans.get(position);
        holder.textView1.setText(nowExam.getName());
        holder.textView2.setText("课号：" + nowExam.getNumber());
        holder.textView3.setText("教师：" + nowExam.getTeacher());
        SpannableString spannableString = new SpannableString("教室：" + (nowExam.getRoom().isEmpty() ? "未公布" : nowExam.getRoom()));
        spannableString.setSpan(new ForegroundColorSpan(context.getColor(R.color.color_room)), 3, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 3, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.textView4.setText(spannableString);
        spannableString = new SpannableString("时间：" + nowExam.getTime());
        spannableString.setSpan(new ForegroundColorSpan(context.getColor(R.color.color_clock)), 3, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 3, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.textView5.setText(spannableString);
        List<String> FragmentstartText = new ArrayList<>(Arrays.asList("日期：", (nowExam.getDate() == null ? nowExam.getDateString() : TimeUtil.timeFormat(nowExam.getDate())), " 第" + nowExam.getWeek() + "周 ",
                TimeUtil.whichDay(nowExam.getDay())));
        spannableString = new SpannableString(String.join("", FragmentstartText));
        spannableString.setSpan(new ForegroundColorSpan(context.getColor(R.color.color_date)),
                FragmentstartText.get(0).length(),
                FragmentstartText.get(0).length() +
                        FragmentstartText.get(1).length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD),
                FragmentstartText.get(0).length(),
                FragmentstartText.get(0).length() +
                        FragmentstartText.get(1).length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(context.getColor(R.color.color_day)),
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
            spannableString.setSpan(new ForegroundColorSpan(context.getColor(R.color.color_time)),
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
        if (nowExam.getComm().isEmpty()) {
            holder.textView6.setVisibility(View.GONE);
            holder.textView7.setText(spannableString);
        } else {
            holder.textView6.setVisibility(View.VISIBLE);
            holder.textView6.setText(spannableString);
            spannableString = new SpannableString("备注信息：\n" + nowExam.getComm());
            spannableString.setSpan(new ForegroundColorSpan(context.getColor(R.color.color_day)), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0,6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.textView7.setText(spannableString);
        }
    }

    @Override
    public int getItemCount() {
        return examBeans.size();
    }

    static class ExamViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7,textView8;
        public ExamViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.detail_text_1);
            textView2 = itemView.findViewById(R.id.detail_text_2);
            textView3 = itemView.findViewById(R.id.detail_text_3);
            textView4 = itemView.findViewById(R.id.detail_text_4);
            textView5 = itemView.findViewById(R.id.detail_text_5);
            textView6 = itemView.findViewById(R.id.detail_text_6);
            textView7 = itemView.findViewById(R.id.detail_text_7);
            textView8 = itemView.findViewById(R.id.detail_text_8);
            textView8.setVisibility(View.GONE);
        }
    }
}
