package com.SB.SBtugar.Adapters;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.SB.SBtugar.AllModels.Product;
import com.SB.SBtugar.AppController;
import com.SB.SBtugar.R;
import com.SB.SBtugar.listener.IProductListListener;
import com.bumptech.glide.RequestManager;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;

public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductRecyclerViewAdapter.ProductView> {

    List<Product> productList;
    private RequestManager mGlideManager;
    private IProductListListener listener;
    private int layoutId;

    public ProductRecyclerViewAdapter(int layoutId){
        productList = new ArrayList<>();
        this.layoutId = layoutId;
    }

    public void setProductList(List<Product> products){
        this.productList.clear();
        if(products!=null){
            Log.e("resHere","hh"+products.size()+"");
            this.productList.addAll(products);
        }
        super.notifyDataSetChanged();
    }

    public void setProductListListener(IProductListListener listener){
        this.listener = listener;
    }

    public void setGlideManager(RequestManager glideManager) {
        this.mGlideManager = glideManager;
    }

    @Override
    public ProductView onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(layoutId,parent,false);
        return new ProductRecyclerViewAdapter.ProductView(itemView,listener);
    }

    @Override
    public void onBindViewHolder(ProductView holder, int position) {
        Product product = productList.get(position);
        Log.e("requestorder",new Gson().toJson(product));

        holder.productOldPrice.setPaintFlags(holder.productOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        holder.product = product;
        if(!product.getOnSale()){
            holder.productOldPrice.setVisibility(View.GONE);
            holder.offer_container.setVisibility(View.GONE);
        }else{
            String oldPrice = holder.productOldPrice.getContext().getString(R.string.price,
                    product.getRegularPrice());
            holder.productOldPrice.setText(oldPrice);
            holder.productOldPrice.setVisibility(View.VISIBLE);
            holder.offer_container.setVisibility(View.VISIBLE);
            int valOf = 100 - (int)((Float.parseFloat(product.getPrice()) / Float.parseFloat(product.getRegularPrice()))  * 100);
            holder.percent.setText(valOf+"%");
        }

        if (product.getName().contains("-")) {
            if (Locale.getDefault().getLanguage().equals("ar"))
                holder.productName.setText(product.getName().split("-")[1]);
            else
                holder.productName.setText(product.getName().split("-")[1]);
        }
        else {
            holder.productName.setText(product.getName());
        }


        if(product.getType() != null){
            if (product.getType().equals("variable")) {
                holder.productPrice.setText("السعر حسب الطلب");
            }
            else {
                String price = holder.productPrice.getContext().getString(R.string.price,product.getPrice());
                holder.productPrice.setText(price);
            }
        }
        if(mGlideManager!=null){
            if(product.getMainImage() != null && !product.getMainImage().isEmpty()){
                Log.e("images",product.getMainImage());
                mGlideManager.load(product.getMainImage()).into(holder.productImage);
                mGlideManager.load(product.getMainImage()).into(holder.backgroundImg);
            }else if(product.getImages()!=null) {
                Log.e("images","second");
                if (product.getImages().size()==0) {
                    Log.e("images","third");
                    if (product.getStore().getId().equals(AppController.SelectedSuperMarket.getId())){
                        mGlideManager.load(AppController.SelectedSuperMarket.getMarketLogo()).into(holder.productImage);
                    mGlideManager.load(AppController.SelectedSuperMarket.getMarketLogo()).into(holder.backgroundImg);
                }
                    else{
                        Log.e("images","fourth");
                        holder.productImage.setImageResource(R.drawable.tugar_logo);
                        holder.backgroundImg.setImageResource(R.drawable.tugar_logo);
                    }
                }
                else {
                    Log.e("images","fifth");
                    mGlideManager.load(product.getImages().get(0).getSrc()).into(holder.productImage);
                    mGlideManager.load(product.getImages().get(0).getSrc()).into(holder.backgroundImg);
                }
            }
            else {
                Log.e("images","sixth");
                holder.productImage.setImageResource(R.drawable.tugar_logo);
                holder.backgroundImg.setImageResource(R.drawable.tugar_logo);

            }
        }
        if (product.getStockStatus().equals("outofstock"))
            holder.itemView.setAlpha(0.5f);
        else
            holder.itemView.setAlpha(1f);
        holder.borderview.setBorderColor(Color.parseColor(AppController.selectedCategoyMain.getColor()));
        if (productList.get(position).isIsnew())
            holder.isnew.setVisibility(View.VISIBLE);
        else
            holder.isnew.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductView extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView productName;
        private TextView productPrice;
        private TextView productOldPrice;
        private TextView percent;
        private RelativeLayout offer_container;
        private ImageView productImage;
        private ImageView backgroundImg;
        private Product product;
        private IProductListListener listener;
        RoundedImageView borderview;
        RelativeLayout isnew;

        public ProductView(View itemView, IProductListListener listListener) {
            super(itemView);
            offer_container = itemView.findViewById(R.id.offer_container);
            productOldPrice = itemView.findViewById(R.id.tv_product_price_old);
            percent = itemView.findViewById(R.id.percent);
            productName = itemView.findViewById(R.id.tv_product_name);
            productPrice = itemView.findViewById(R.id.tv_product_price);
            productImage = itemView.findViewById(R.id.iv_product_logo);
            backgroundImg = itemView.findViewById(R.id.backgroundImg);
            borderview = itemView.findViewById(R.id.borderview);
            isnew = itemView.findViewById(R.id.isnew);

            ImageView addToCart = itemView.findViewById(R.id.iv_add_to_cart);
            addToCart.setOnClickListener(this);
            this.listener = listListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (product.getStockStatus().equals("instock")) {
                if (view.getId() == R.id.iv_add_to_cart && product.getType() != null && !product.getType().equals("variable")) {
                    listener.addToCart(product);
                } else {
                    listener.showProductDetails(product);
                }
            }
        }
    }
}
