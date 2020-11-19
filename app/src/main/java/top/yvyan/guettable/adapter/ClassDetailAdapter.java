package top.yvyan.guettable.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import top.yvyan.guettable.R;
import top.yvyan.guettable.data.DayClassData;

public class ClassDetailAdapter extends RecyclerView.Adapter<ClassDetailAdapter.ClassDetailViewHolder> {
    DayClassData dayClassData = DayClassData.newInstance();

    @NonNull
    @Override
    public ClassDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.day_class_detail_cardview,parent,false);
        return new ClassDetailViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassDetailViewHolder holder, int position) {
        if(dayClassData.getCourseBeans().get(position).isLab()) {
            holder.tName.setText(dayClassData.getCourseBeans().get(position).getName());
            holder.tTeacherName.setText(dayClassData.getCourseBeans().get(position).getTeacher());
            holder.tLibname.setText(dayClassData.getCourseBeans().get(position).getLibName());
            holder.tRoom.setText(dayClassData.getCourseBeans().get(position).getRoom());
            holder.tDay.setText(whichDay(dayClassData.getCourseBeans().get(position).getDay()));
            holder.tTime.setText(String.valueOf(dayClassData.getCourseBeans().get(position).getTime()));
            holder.tWeekStart.setText(String.valueOf(dayClassData.getCourseBeans().get(position).getWeekStart()));
            holder.tWeekEnd.setText(String.valueOf(dayClassData.getCourseBeans().get(position).getWeekEnd()));
            holder.tRemarks.setText(dayClassData.getCourseBeans().get(position).getRemarks());
        }
        else {
        }
    }

    @Override
    public int getItemCount() {
        return dayClassData.getCourseBeans().size();
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
        TextView tName, tTeacherName, tLibname, tRoom, tDay, tTime, tWeekStart, tWeekEnd, tRemarks;
        public ClassDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            tName = itemView.findViewById(R.id.textViewName);
            tTeacherName = itemView.findViewById(R.id.textViewTeacherName);
            tLibname = itemView.findViewById(R.id.textViewLibname);
            tRoom = itemView.findViewById(R.id.textViewRoom);
            tDay = itemView.findViewById(R.id.textViewDay);
            tTime = itemView.findViewById(R.id.textViewTime);
            tWeekStart = itemView.findViewById(R.id.textViewWeekStart);
            tWeekEnd = itemView.findViewById(R.id.textViewWeekEnd);
            tRemarks = itemView.findViewById(R.id.textViewRemarks);
        }
    }
}
