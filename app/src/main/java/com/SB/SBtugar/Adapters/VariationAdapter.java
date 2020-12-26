package com.SB.SBtugar.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.SB.SBtugar.Activities.FullScreenImage;
import com.SB.SBtugar.Activities.FullScreenImages;
import com.SB.SBtugar.R;
import com.SB.SBtugar.listener.listen_to_variation;
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Locale;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class
VariationAdapter extends RecyclerView.Adapter<VariationAdapter.ViewHolder> {
    Context context;
    List<String> list;
    List<String> list_images;
    int selectedPos;
    int type;
    listen_to_variation listener;
    public VariationAdapter(Context context, List<String> list_images, List<String> list , int selectedPos, listen_to_variation listener, int type) {
        this.context = context;
        this.selectedPos = selectedPos;
        this.list = list;
        this.list_images = list_images;
        this.type = type;
        this.listener = listener;
    }

    @Override
    public VariationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_variation_item, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Glide.with(context).load(list_images.get(position)).into(holder.img);
        holder.itemView.setOnClickListener(view -> {
            listener.onVariationSelected(position,type,true);
        });
        holder.itemView.setOnLongClickListener(view -> {
            Intent intent = new Intent(context, FullScreenImage.class);
            intent.putExtra("URL",list_images.get(position));
            context.startActivity(intent);
            return true;
        });
        if (selectedPos != position)
            holder.img.setBackground(ContextCompat.getDrawable(context, R.drawable.border_primary));
        else
            holder.img.setBackground(ContextCompat.getDrawable(context, R.drawable.border_primary_selected));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
        }
    }
}