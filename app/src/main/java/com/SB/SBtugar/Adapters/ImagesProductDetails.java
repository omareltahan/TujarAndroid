package com.SB.SBtugar.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.SB.SBtugar.Activities.FullScreenImages;
import com.SB.SBtugar.AllModels.Image;
import com.SB.SBtugar.AllModels.Product;
import com.SB.SBtugar.R;
import com.bumptech.glide.Glide;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class ImagesProductDetails extends RecyclerView.Adapter<ImagesProductDetails.ViewHolder> {
    Context context;
    List<Image> list;
    Product product;
    public ImagesProductDetails(Context context, List<Image> list, Product product) {
        this.context = context;
        this.list = list;
        this.product = product;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.images_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (list.size()>0)
            Glide.with(context).load(list.get(position).getSrc()).into(holder.text);
        else
            holder.text.setImageResource(R.drawable.tugar_logo);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.size()==0 || product == null)
                    return;
                Intent intent = new Intent(context, FullScreenImages.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data",product);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list.size()>0)
            return list.size();
        else
            return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView text;

        public ViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.image);
        }
    }
}