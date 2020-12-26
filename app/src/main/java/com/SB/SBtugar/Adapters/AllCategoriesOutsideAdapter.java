package com.SB.SBtugar.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.SB.SBtugar.AllModels.display_value_item;
import com.SB.SBtugar.AppController;
import com.SB.SBtugar.R;
import com.SB.SBtugar.listener.listen_outside_category;
import com.bumptech.glide.Glide;

import java.net.URLDecoder;
import java.util.List;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class AllCategoriesOutsideAdapter extends RecyclerView.Adapter<AllCategoriesOutsideAdapter.ViewHolder> {
    Context context;
    List<display_value_item> list;
    listen_outside_category listener;
    public AllCategoriesOutsideAdapter(Context context, List<display_value_item> list, listen_outside_category listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_areas_item ,parent, false);
        return new ViewHolder(v);
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.Title.setText(list.get(position).getDisplay());
        holder.CountOrders.setText(list.get(position).getNumber_of_recent_orders()+"");
        holder.CountMarkets.setText(list.get(position).getNumber_of_vendors()+"");
        holder.Desc.setText(list.get(position).getDesc());
        holder.container.setCardBackgroundColor(Color.parseColor(list.get(position).getColor()));
        Glide.with(context).load(list.get(position).getIcon()).into(holder.image);
        final int finalPosition = position;
        holder.itemView.setOnClickListener(v -> {
            AppController.selectedCategoyMain = list.get(position);
            listener.listen_outside_category(finalPosition);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView Title,Desc,CountOrders,CountMarkets;
        CardView container;
        public ViewHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            Title = itemView.findViewById(R.id.Title);
            Desc = itemView.findViewById(R.id.Desc);
            CountOrders = itemView.findViewById(R.id.CountOrders);
            CountMarkets = itemView.findViewById(R.id.CountMarkets);
            image = itemView.findViewById(R.id.image);
            ViewGroup.LayoutParams params=itemView.getLayoutParams();
        }
    }
}