package com.SB.SBtugar.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.SB.SBtugar.AllModels.ModelController;
import com.SB.SBtugar.AllModels.SuperMarket.SuperMarket;
import com.SB.SBtugar.AllModels.UserData;
import com.SB.SBtugar.AllModels.UserDataResponse;
import com.SB.SBtugar.R;
import com.SB.SBtugar.SeverData.Constant;
import com.SB.SBtugar.SeverData.ServiceGenerator;
import com.SB.SBtugar.SeverData.StoreAPIInterface;
import com.SB.SBtugar.listener.IMarketListListener;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarketsRecycleViewAdapter extends RecyclerView.Adapter<MarketsRecycleViewAdapter.MarketView> {


    private List<SuperMarket> marketList;
    private RequestManager mGlideManager;
    private IMarketListListener listener;
    Context context;
    public MarketsRecycleViewAdapter(Context context){
        marketList = new ArrayList<>();
        this.context = context;
    }

    public void setGlideManager(RequestManager glideManager) {
        this.mGlideManager = glideManager;
    }

    public void setMarketList(List<SuperMarket> marketList){
        this.marketList.clear();
        if(marketList!=null){
            this.marketList.addAll(marketList);
        }
        super.notifyDataSetChanged();
    }

    public void setOnItemClickListener(IMarketListListener listener){
        this.listener = listener;
    }

    @Override
    public MarketView onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.market_item,parent,false);
        return new MarketView(itemView,listener);
    }

    @Override
    public void onBindViewHolder(final MarketView holder, int position) {
        final SuperMarket market = marketList.get(position);
        Log.e("timeisbe",market.getMarketDeliveryDuration());

        holder.marketName.setText(market.getMarketName());

        if(mGlideManager!=null){
            mGlideManager.load(market.getMarketLogo()).into(holder.marketLogo);
        }

        String price = holder.deliveryFee.getContext().getString(R.string.price2,
                market.getMarketDeliveryFees());
        holder.deliveryFee.setText(price);

        price = holder.minOrder.getContext().getString(R.string.price2, market.getMarketMinOrder());
        holder.minOrder.setText(price);

        String time = holder.deliveryTime.getContext().getString(R.string.time,
                market.getMarketDeliveryDuration());
        holder.deliveryTime.setText(time);

        String rating = market.getMarketRating();
        if(rating.equals("No Ratings found yet")){
            holder.rating.setRating(Float.parseFloat("0.0"));
        }else{
            holder.rating.setRating(Float.parseFloat( rating));
        }

        if (context.getSharedPreferences("superbekala", Context.MODE_PRIVATE).getBoolean(market.getId()+"",false)) {
            market.setFavourite(true);
            holder.Favourite.setImageResource(R.drawable.fav_icon);
        }
        else{
            holder.Favourite.setImageResource(R.drawable.heartis);
            market.setFavourite(false);
        }

        holder.Favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserData main_user = ModelController.getInstance().getModel().getUser();
                if (main_user == null)
                    Toast.makeText(context,"عليك تسجيل الدخول أولا", Toast.LENGTH_SHORT).show();
                else {
                    if (!market.isFavourite()) {
                        StoreAPIInterface storeServices = ServiceGenerator.createService(
                                StoreAPIInterface.class, Constant.BASE_URL_V2);
                        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), market.getId() + "");
                        UserData user = ModelController.getInstance().getModel().getUser();
                        storeServices.SetFavouriteToServer(user.getId() + "", body)
                                .enqueue(new Callback<UserDataResponse>() {
                                    @Override
                                    public void onResponse(@NonNull Call<UserDataResponse> call,
                                                           @NonNull Response<UserDataResponse> response) {
                                        market.setFavourite(!market.isFavourite());
                                        context.getSharedPreferences("superbekala", Context.MODE_PRIVATE).edit().putBoolean(market.getId() + "", market.isFavourite()).apply();
                                        holder.Favourite.setImageResource(R.drawable.fav_icon);
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<UserDataResponse> call, Throwable t) {
                                        market.setFavourite(!market.isFavourite());
                                        context.getSharedPreferences("superbekala", Context.MODE_PRIVATE).edit().putBoolean(market.getId() + "", market.isFavourite()).apply();
                                        holder.Favourite.setImageResource(R.drawable.fav_icon);
                                    }
                                });
                    } else {
                        StoreAPIInterface storeServices = ServiceGenerator.createService(
                                StoreAPIInterface.class, Constant.BASE_URL_V2);
                        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), market.getId() + "");
                        UserData user = ModelController.getInstance().getModel().getUser();
                        storeServices.RemoveFavouriteFromServer(user.getId() + "", body)
                                .enqueue(new Callback<UserDataResponse>() {
                                    @Override
                                    public void onResponse(@NonNull Call<UserDataResponse> call,
                                                           @NonNull Response<UserDataResponse> response) {
                                        market.setFavourite(!market.isFavourite());
                                        context.getSharedPreferences("superbekala", Context.MODE_PRIVATE).edit().putBoolean(market.getId() + "", market.isFavourite()).apply();
                                        holder.Favourite.setImageResource(R.drawable.heartis);
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<UserDataResponse> call, Throwable t) {
                                        market.setFavourite(!market.isFavourite());
                                        context.getSharedPreferences("superbekala", Context.MODE_PRIVATE).edit().putBoolean(market.getId() + "", market.isFavourite()).apply();
                                        holder.Favourite.setImageResource(R.drawable.heartis);
                                    }
                                });
                    }
                }
            }
        });

        holder.mItem = market;
        if(!market.isOpen()){
            holder.closeImage.setImageResource(R.drawable.close_image);
            holder.marketLogo.setBorderColor(context.getResources().getColor(R.color.red_image));
        }
        else {
            holder.closeImage.setImageResource(R.drawable.open_image);
            holder.marketLogo.setBorderColor(context.getResources().getColor(R.color.green_image));
        }
    }

    @Override
    public int getItemCount() {
        return marketList.size();
    }

    public class MarketView extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView closeImage;
        private TextView desc;
        private TextView marketName;
        private TextView deliveryTime;
        private TextView deliveryFee;
        private TextView minOrder;
        private RatingBar rating;
        private CircleImageView marketLogo;
        private SuperMarket mItem;
        private IMarketListListener listener;
        private ImageView Favourite;

        private MarketView(View itemView, IMarketListListener listListener) {
            super(itemView);
            closeImage = itemView.findViewById(R.id.closeImage);
            desc = itemView.findViewById(R.id.desc);
            Favourite = itemView.findViewById(R.id.favourite);
            marketName = itemView.findViewById(R.id.tv_market_name);
            marketLogo = itemView.findViewById(R.id.iv_market_logo);
            deliveryTime = itemView.findViewById(R.id.tv_market_delivery_time);
            minOrder = itemView.findViewById(R.id.tv_market_min_order);
            deliveryFee = itemView.findViewById(R.id.tv_market_delivery_fee);
            rating = itemView.findViewById(R.id.rb_market_rating);
            listener = listListener;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            listener.onItemClick(mItem);
        }
    }
}
