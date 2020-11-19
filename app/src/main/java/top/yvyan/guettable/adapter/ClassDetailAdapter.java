package top.yvyan.guettable.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import top.yvyan.guettable.R;
import top.yvyan.guettable.bean.CourseBean;

public class ClassDetailAdapter extends RecyclerView.Adapter<ClassDetailAdapter.ClassDetailViewHolder> {
    List<CourseBean> courseBeans;

    public ClassDetailAdapter(List<CourseBean> courseBeans) {
        this.courseBeans = courseBeans;
    }

    @NonNull
    @Override
    public ClassDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.day_class_detail_cardview,parent,false);
        return new ClassDetailViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassDetailViewHolder holder, int position) {
        if(courseBeans.get(position).isLab()) {
            holder.textView1.setText(courseBeans.get(position).getName());
            holder.textView2.setText("名称：" + courseBeans.get(position).getLibName());
            if (courseBeans.get(position).getTeacher() == null) {
                holder.textView3.setVisibility(View.GONE);
            } else {
                holder.textView3.setText("教师：" + courseBeans.get(position).getTeacher());
            }
            holder.textView4.setText("教室：" + courseBeans.get(position).getRoom());
            holder.textView5.setText("时间：" + whichDay(courseBeans.get(position).getDay()) + " 第" + courseBeans.get(position).getTime() + "大节");
            holder.textView6.setText("周次：" + courseBeans.get(position).getWeekStart() + "-" + courseBeans.get(position).getWeekEnd() + "周");
            holder.textView7.setText(courseBeans.get(position).getRemarks());
        } else {
        }
    }

    @Override
    public int getItemCount() {
        return courseBeans.size();
    }

    public String whichDay(int number){
        String s = new String();
        switch (number){
            case 1:
                s = "星期一";
                break;
            case 2:
                s = "星期二";
                break;
            case 3:
                s = "星期三";
                break;
            case 4:
                s = "星期四";
                break;
            case 5:
                s = "星期五";
                break;
            case 6:
                s = "星期六";
                break;
            case 7:
                s = "星期日";
                break;
        }
        return s;
    }

    static class ClassDetailViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7;
        public ClassDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.card_1);
            textView2 = itemView.findViewById(R.id.card_2);
            textView3 = itemView.findViewById(R.id.card_3);
            textView4 = itemView.findViewById(R.id.card_4);
            textView5 = itemView.findViewById(R.id.card_5);
            textView6 = itemView.findViewById(R.id.card_6);
            textView7 = itemView.findViewById(R.id.card_7);
        }
    }
}
