package top.yvyan.guettable.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import top.yvyan.guettable.R;

public class MoreFunAdapter extends RecyclerView.Adapter<MoreFunAdapter.MoreFunViewHolder> {

    private List<Item> items;

    public MoreFunAdapter(List<Item> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public MoreFunViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.more_cardview,parent,false);
        return new MoreFunViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MoreFunViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return (items.size() + 1) / 2;
    }

    static class MoreFunViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public MoreFunViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.card_1);
        }
    }

    public class Item {
        int imageId;
        String title;
    }
}
