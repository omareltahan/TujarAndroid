package com.SB.SBtugar.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.SB.SBtugar.R;
import androidx.recyclerview.widget.RecyclerView;

public class BottomImagesProductDetails extends RecyclerView.Adapter<BottomImagesProductDetails.ViewHolder> {
    Context context;
    int count,pos;
    public BottomImagesProductDetails(Context context,int count,int pos) {
        this.context = context;
        this.count = count;
        this.pos = pos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.images_item_dot, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (pos==position)
            holder.text.setImageResource(R.drawable.bottom_blue);
        else
            holder.text.setImageResource(R.drawable.bottom_gray);
    }

    @Override
    public int getItemCount() {
        if (count>1)
            return count;
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView text;
        public ViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.image);
        }
    }
}