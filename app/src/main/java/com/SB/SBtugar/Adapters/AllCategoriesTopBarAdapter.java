package com.SB.SBtugar.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.SB.SBtugar.AllModels.display_value_item;
import com.SB.SBtugar.R;
import com.SB.SBtugar.listener.listen_outside_category;
import com.bumptech.glide.Glide;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public class AllCategoriesTopBarAdapter extends RecyclerView.Adapter<AllCategoriesTopBarAdapter.ViewHolder> {
    Context context;
    List<display_value_item> list;
    listen_outside_category listener;
    int pos_selected;
    public AllCategoriesTopBarAdapter(Context context, List<display_value_item> list, listen_outside_category listener,int pos_selected) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.pos_selected = pos_selected;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_main_categories ,parent, false);
        return new ViewHolder(v);
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (pos_selected==position)
            holder.image.setColorFilter(Color.parseColor(list.get(position).getColor()));
        else
            holder.image.setColorFilter(context.getResources().getColor(R.color.gray));
        Glide.with(context).load(list.get(position).getIcon()).into(holder.image);
        holder.text.setText(list.get(position).getDisplay());
        holder.itemView.setOnClickListener(v -> listener.listen_outside_category(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView text;
        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            text = itemView.findViewById(R.id.text);
        }
    }
}