package top.yvyan.guettable.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import top.yvyan.guettable.R;
import top.yvyan.guettable.bean.CETBean;

public class CETAdapter extends RecyclerView.Adapter<CETAdapter.CETViewHolder> {

    List<CETBean> cetBeans;

    public CETAdapter(List<CETBean> cetBeans) {
        this.cetBeans = cetBeans;
    }

    @NonNull
    @Override
    public CETViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.detail_cardview,parent,false);
        return new CETViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CETViewHolder holder, int position) {
        holder.textView1.setText(cetBeans.get(position).getName());
        holder.textView2.setText("综合成绩：" + cetBeans.get(position).getStage());
        holder.textView3.setText("折算成绩：" + cetBeans.get(position).getScore());
        holder.textView4.setText("学期：" + cetBeans.get(position).getTerm());
        holder.textView5.setText("证书编号：" + cetBeans.get(position).getCard());
        holder.textView6.setVisibility(View.GONE);
        String postDate = cetBeans.get(position).getPostDate();
        if (postDate != null) {
            holder.textView7.setText("推送时间：" + postDate);
        } else {
            holder.textView7.setText("推送时间：");
        }
    }

    @Override
    public int getItemCount() {
        return cetBeans.size();
    }

    public class CETViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7;
        public CETViewHolder(@NonNull View itemView) {
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
