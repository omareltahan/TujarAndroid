package com.SB.SBtugar.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.SB.SBtugar.AllModels.Rating_view;
import com.SB.SBtugar.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class RatingViewAdapter extends RecyclerView.Adapter<RatingViewAdapter.ViewHolder> {
    Context context;
    List<Rating_view> list;

    public RatingViewAdapter(Context context, List<Rating_view> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rate_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.textView.setText(list.get(position).getText());
        holder.name.setText(list.get(position).getName());
        holder.date.setText(list.get(position).getDate() + " " + list.get(position).getTime());
        holder.ratingBar.setRating(list.get(position).getRate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,textView,date;
        RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.username);
            ratingBar = itemView.findViewById(R.id.rate);
            date = itemView.findViewById(R.id.time);
            textView = itemView.findViewById(R.id.text);
        }
    }
}