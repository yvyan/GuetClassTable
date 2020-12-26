package top.yvyan.guettable.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import top.yvyan.guettable.R;
import top.yvyan.guettable.bean.ResitBean;
import top.yvyan.guettable.util.TimeUtil;

public class ResitAdapter extends RecyclerView.Adapter<ResitAdapter.ResitViewHolder> {
    List<ResitBean> resitBeans;

    public ResitAdapter(List<ResitBean> resitBeans) {
        this.resitBeans = resitBeans;
    }

    @NonNull
    @Override
    public ResitAdapter.ResitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.detail_cardview,parent,false);
        return new ResitAdapter.ResitViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ResitAdapter.ResitViewHolder holder, int position) {
        holder.textView1.setText(resitBeans.get(position).getName());
        holder.textView2.setText("课号：" + resitBeans.get(position).getNumber());
        holder.textView3.setVisibility(View.GONE);
        holder.textView4.setText("教室：" + resitBeans.get(position).getRoom());
        holder.textView5.setText("时间：" + resitBeans.get(position).getTime());
        holder.textView6.setVisibility(View.GONE);
        holder.textView7.setText("日期：" + TimeUtil.timeFormat(resitBeans.get(position).getDate()));
    }

    @Override
    public int getItemCount() {
        return resitBeans.size();
    }

    public class ResitViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7;
        public ResitViewHolder(@NonNull View itemView) {
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
