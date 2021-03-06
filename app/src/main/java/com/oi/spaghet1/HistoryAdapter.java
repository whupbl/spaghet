package com.oi.spaghet1;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import java.sql.ResultSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<History> posts;
    public SpaghetAPI spaghetAPI;
    public Retrofit retrofit;
    private Context context;

    public HistoryAdapter(List<History> posts, Context cont) {
        this.posts = posts;
        retrofit = new Retrofit.Builder()
                .baseUrl(SpaghetAPI.serverURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        spaghetAPI = retrofit.create(SpaghetAPI.class);
        context = cont;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new ViewHolder(v);
    }


    public void showRatingDialog() {

    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final History post = posts.get(position);

        holder.site.setText("Оценить");
        holder.dishName.setText(post.getDishName());
        holder.cook.setText(post.getCookName());
        holder.phoneNumber.setText(post.getPhoneNumber());
        holder.description.setText(post.getDescription() + " Очень очень вкусная еда с разными специями и добавками, Вам точно придется по вкусу");
        holder.date.setText(post.getTimeStart());

        if (!post.getClientToCook().equals(0)) {
            holder.estimate.setText(post.getClientToCook().toString());
            holder.site.setVisibility(View.INVISIBLE);
        }
        else {
            holder.estimate.setVisibility(View.INVISIBLE);
            holder.estimateLabel.setVisibility(View.INVISIBLE);
        }

        holder.site.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderAssessActivity.class);
                intent.putExtra("postID", String.valueOf(post.getID()));
                ((Activity)context).startActivityForResult(intent,1 );
            }
        });
    }

    @Override
    public int getItemCount() {
        if (posts == null)
            return 0;
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        Button site;
        TextView dishName;
        TextView description;
        TextView phoneNumber;
        TextView cook;
        TextView estimate;
        TextView date;
        TextView estimateLabel;

        public ViewHolder(View itemView) {
            super(itemView);
            site =(Button) itemView.findViewById(R.id.button_assess);

            dishName =(TextView) itemView.findViewById(R.id.historyitem_dish);

            estimate =(TextView) itemView.findViewById(R.id.historyItem_estimate);

            description =(TextView) itemView.findViewById(R.id.historyItem_description);

            phoneNumber =(TextView) itemView.findViewById(R.id.historyItem_phone);

            date =(TextView) itemView.findViewById(R.id.historyItem_date);

            cook =(TextView) itemView.findViewById(R.id.historyItem_cook);

            estimateLabel = (TextView) itemView.findViewById(R.id.historyItem_estimateLabel);

        }
    }
}

