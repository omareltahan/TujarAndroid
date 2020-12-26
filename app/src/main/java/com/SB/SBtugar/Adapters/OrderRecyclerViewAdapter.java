package com.SB.SBtugar.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.SB.SBtugar.Activities.OrderDetailsView;
import com.SB.SBtugar.AllModels.Orders.OrderResponse;
import com.SB.SBtugar.AppController;
import com.SB.SBtugar.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Project ${PROJECT}
 * Created by asamy on 5/9/2018.
 */

public class OrderRecyclerViewAdapter extends RecyclerView.Adapter<OrderRecyclerViewAdapter.OrderView> {

    List<OrderResponse> orderList;
    Context context;
    public OrderRecyclerViewAdapter(Context context){
        orderList =new ArrayList<>();
        this.context=context;
    }

    public void setOrderList(List<OrderResponse> orders){
        orderList = orders;
        notifyDataSetChanged();
    }


    @Override
    public OrderView onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item,parent,false);
        return new OrderRecyclerViewAdapter.OrderView(itemView);
    }

    @Override
    public void onBindViewHolder(OrderView holder, int position) {
        final OrderResponse order = orderList.get(position);

        holder.orderTotal.setText(order.getTotal());
        holder.orderDate.setText(order.getDateCreated());
        if(order.getStatus().equals(OrderResponse.ORDER_COMPLETED)){
            holder.orderStatus.setBackground(ContextCompat.getDrawable(holder.orderStatus.getContext(),
                    R.drawable.status_success));
            holder.orderStatus.setTextColor(Color.WHITE);
        }else if(order.getStatus().equals(OrderResponse.ORDER_REJECTED)
                || order.getStatus().equals(OrderResponse.ORDER_FAILED) ||
                order.getStatus().equals(OrderResponse.ORDER_CANCELLED)){
            holder.orderStatus.setBackground(ContextCompat.getDrawable(holder.orderStatus.getContext(),
                    R.drawable.status_rejected));
            holder.orderStatus.setTextColor(Color.WHITE);
        }else {
            holder.orderStatus.setTextColor(Color.WHITE);
            holder.orderStatus.setBackground(ContextCompat.getDrawable(holder.orderStatus.getContext(),
                    R.drawable.status_pending));
        }
        if (order.getStatus().equals(OrderResponse.ORDER_FAILED))
            holder.orderStatus.setText(OrderResponse.ORDER_REJECTED);
        else
            holder.orderStatus.setText(order.getStatus());

        holder.MarketName.setText(order.getStore().getShop_name());
        Glide.with(context).load(order.getStore().getUrl()).into(holder.marketLogo);
//        holder.marketLogo.setBorderColor(Color.parseColor(orderList.get(position).getColor()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderDetailsView.class);
                AppController.order_global = order;
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class OrderView extends RecyclerView.ViewHolder{
        CircleImageView marketLogo;
        TextView orderStatus;
        TextView orderDate;
        TextView orderTotal;
        TextView MarketName;

        public OrderView(View itemView) {
            super(itemView);
            MarketName = itemView.findViewById(R.id.MarketName);
            orderTotal = itemView.findViewById(R.id.orderTotal);
            orderStatus = itemView.findViewById(R.id.tv_order_status);
            orderDate = itemView.findViewById(R.id.tv_order_date);
            marketLogo = itemView.findViewById(R.id.marketLogo);
        }
    }
}
