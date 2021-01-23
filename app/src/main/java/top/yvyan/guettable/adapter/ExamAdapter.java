package top.yvyan.guettable.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import top.yvyan.guettable.R;
import top.yvyan.guettable.bean.ExamBean;
import top.yvyan.guettable.util.TimeUtil;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ExamViewHolder> {

    List<ExamBean> examBeans;

    public ExamAdapter(List<ExamBean> examBeans) {
        this.examBeans = examBeans;
    }

    @NonNull
    @Override
    public ExamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.detail_cardview,parent,false);
        return new ExamViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamViewHolder holder, int position) {
        holder.textView1.setText(examBeans.get(position).getName());
        holder.textView2.setText("课号：" + examBeans.get(position).getNumber());
        holder.textView3.setText("教师：" + examBeans.get(position).getTeacher());
        holder.textView4.setText("教室：" + (examBeans.get(position).getRoom() == null? "未公布": examBeans.get(position).getRoom()));
        holder.textView5.setText("时间：" + examBeans.get(position).getTime());
        holder.textView6.setVisibility(View.GONE);
        holder.textView7.setText("日期：" + TimeUtil.timeFormat(examBeans.get(position).getDate()) + "(第" + examBeans.get(position).getWeek() + "周 " + TimeUtil.whichDay(examBeans.get(position).getDay()) + ")");
    }

    @Override
    public int getItemCount() {
        return examBeans.size();
    }

    static class ExamViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7;
        public ExamViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.detail_text_1);
            textView2 = itemView.findViewById(R.id.detail_text_2);
            textView3 = itemView.findViewById(R.id.detail_text_3);
            textView4 = itemView.findViewById(R.id.detail_text_4);
            textView5 = itemView.findViewById(R.id.detail_text_5);
            textView6 = itemView.findViewById(R.id.detail_text_6);
            textView7 = itemView.findViewById(R.id.detail_text_7);
        }
    }
}
