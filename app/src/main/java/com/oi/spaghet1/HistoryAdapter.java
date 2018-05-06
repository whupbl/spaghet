package com.oi.spaghet1;

import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<History> posts;

    public HistoryAdapter(List<History> posts) {
        this.posts = posts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        History post = posts.get(position);

        String text = "Дата: " + post.getTimeStart() + "\n" +
                "Блюдо: " + post.getDishName() + "\n" + post.getDescription() +
                "\nПовар: " + post.getCookName() + "\nТел.: " + post.getPhoneNumber();
        if (!post.getClientToCook().equals(0))
            text = text + "\nОценка: " + post.getClientToCook();
        holder.post.setText(text);

        holder.site.setText("Оценить");
    }

    @Override
    public int getItemCount() {
        if (posts == null)
            return 0;
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView post;
        Button site;

        public ViewHolder(View itemView) {
            super(itemView);
            post = (TextView) itemView.findViewById(R.id.historyitem_dish);
            site = (Button) itemView.findViewById(R.id.button_assess);
        }
    }
}

