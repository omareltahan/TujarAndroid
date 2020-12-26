package com.SB.SBtugar.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.SB.SBtugar.AllModels.Orders.OrderDetailsModel;
import com.SB.SBtugar.R;
import com.bumptech.glide.Glide;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ProductView> {
    List<OrderDetailsModel> productList;
    Context context;
    public OrderDetailsAdapter(Context context , List<OrderDetailsModel> productList ){
        this.productList = productList;
        this.context = context;
    }


    @Override
    public ProductView onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_details_item,parent,false);
        return new OrderDetailsAdapter.ProductView(itemView);
    }

    @Override
    public void onBindViewHolder(ProductView holder, int position) {
        OrderDetailsModel product = productList.get(position);
        Glide.with(context).load(product.getProduct_id()).into(holder.image_item);
        holder.productName.setText(product.getName());
        float value_of_total = Float.parseFloat(product.getPrice()) * product.getQuantity();
        String price = holder.productPrice.getContext().getString(R.string.price, String.valueOf(value_of_total));

        holder.productPrice.setText(price);
        holder.count.setText(product.getQuantity()+ " وحدة x "+ String.format ("%.2f", Float.parseFloat(product.getPrice()))+" جنيه");
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductView extends RecyclerView.ViewHolder{
        ImageView image_item;
        private TextView productName;
        private TextView productPrice;
        private TextView count;

        public ProductView(View itemView) {

            super(itemView);
            image_item = itemView.findViewById(R.id.iv_cart_product_image);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            count = itemView.findViewById(R.id.count);
        }
    }
}
