package com.SB.SBtugar.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.SB.SBtugar.CartData.CartItem;
import com.SB.SBtugar.R;
import com.SB.SBtugar.listener.ICartListListener;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Project ${PROJECT}
 * Created by asamy on 5/5/2018.
 */

public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.CartItemView> {

    List<CartItem> items;
    private RequestManager mGlideManager;
    private ICartListListener listListener;


    public void setGlideManager(RequestManager glideManager) {
        this.mGlideManager = glideManager;
    }

    public void setListListener(ICartListListener listListener){
        this.listListener = listListener;
    }

    public CartRecyclerViewAdapter(){
        items = new ArrayList<>();
    }

    public void setCartItems(List<CartItem> items){
        this.items = items;
        notifyDataSetChanged();
    }
    String replaceOfArabic (String str){
        return str.replace("٠", "0").replace(",", ".")
                .replace("١", "1").replace("٢", "2").replace("٣", "3")
                .replace("٤", "4").replace("٥", "5").replace("٦", "6")
                .replace("٧", "7").replace("٨", "8").replace("٩", "9");
    }


    @Override
    public CartItemView onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item,parent,false);
        return new CartItemView(itemView,listListener);
    }

    @Override
    public void onBindViewHolder(final CartItemView holder, int position) {
        final CartItem item = items.get(position);
        holder.item = item;
        mGlideManager.load(item.Image).into(holder.productImage);
        holder.productName.setText(item.name);
        if (item.name.contains("-")) {
            if (Locale.getDefault().getLanguage().equals("ar"))
                holder.productName.setText(item.name.split("-")[1]);
            else
                holder.productName.setText(item.name.split("-")[1]);
        }
        else {
            holder.productName.setText(item.name);
        }

        if (!item.Variation_txt.equals("")) {
            holder.productName.setText(holder.productName.getText().toString() + "(" + item.Variation_txt + ")");
        }

        float pprice = Float.parseFloat(item.price) * item.quantity;
        String price = holder.productPrice.getContext().getString(R.string.price,
                replaceOfArabic(String.format ("%.2f", pprice)));
        holder.productPrice.setText(price);
        holder.quantity.setText(item.quantity+"");



        holder.Minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = item.quantity - 1 ;
                if (value > 0) {

                    item.quantity = value;
                    float pprice = Float.parseFloat(item.price) * item.quantity;
                    String price = holder.productPrice.getContext().getString(R.string.price,
                            pprice+"");
                    holder.productPrice.setText(price);
                    holder.quantity.setText(item.quantity+"");
                    listListener.onQuantityChange(item,value,false);
                }
            }
        });

        holder.Plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = item.quantity + 1 ;
                if (value < 20) {
                    item.quantity = value;
                    float pprice = Float.parseFloat(item.price) * item.quantity;
                    String price = holder.productPrice.getContext().getString(R.string.price,
                            pprice+"");
                    holder.productPrice.setText(price);
                    holder.quantity.setText(item.quantity+"");
                    listListener.onQuantityChange(item,value,true);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class CartItemView extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView productImage;
        ImageView Minus;
        ImageView Plus;
        TextView productName;
        TextView productPrice;
        TextView quantity;
        TextView removeItem;
        CartItem item;
        ICartListListener listListener;

        public CartItemView(View itemView, ICartListListener listener) {
            super(itemView);
            quantity = itemView.findViewById(R.id.np_cart_product_quantity);
            Minus = itemView.findViewById(R.id.minus);
            Plus = itemView.findViewById(R.id.plus);

            productImage = itemView.findViewById(R.id.iv_cart_product_image);
            productName = itemView.findViewById(R.id.tv_cart_product_name);
            productPrice = itemView.findViewById(R.id.tv_cart_product_price);
            removeItem = itemView.findViewById(R.id.btn_cart_remove_item);
            removeItem.setOnClickListener(this);

            this.listListener = listener;
        }

        @Override
        public void onClick(View view) {
            listListener.onRemoveItem(item);
        }
    }
}
