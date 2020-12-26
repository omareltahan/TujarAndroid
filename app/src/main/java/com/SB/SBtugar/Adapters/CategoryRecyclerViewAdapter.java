package com.SB.SBtugar.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.SB.SBtugar.AllModels.Category;
import com.SB.SBtugar.AppController;
import com.SB.SBtugar.R;
import com.SB.SBtugar.listener.RecyclerViewItemClickListener;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Project ${PROJECT}
 * Created by asamy on 4/12/2018.
 */

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.CategoryView>{

    private List<Category> categories;
    private int position_selected;
    private RecyclerViewItemClickListener mClickListener;
    Context context;
    public CategoryRecyclerViewAdapter(Context context,List<Category> categories, int pos){
        this.context = context;
        this.categories = categories;
        position_selected = pos;
    }

    public void onItemClickListener(RecyclerViewItemClickListener listener){
        this.mClickListener = listener;
    }

    @Override
    public CategoryView onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cat_item,parent,false);
        return new CategoryView(itemView);
    }

    @Override
    public void onBindViewHolder(CategoryView holder, final int position) {
        Category category = categories.get(position);
        if (position_selected==position){
            holder.text.setBackgroundColor(Color.parseColor(AppController.selectedCategoyMain.getColor()));
            holder.text.setTextColor(context.getResources().getColor(R.color.white_color));
        }
        else {
            holder.text.setBackgroundResource(0);
            holder.text.setTextColor(context.getResources().getColor(R.color.black));
        }
        holder.text.setText(category.getCatName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.e("datasize","size"+categories.size());
        return categories.size();
    }

    public class CategoryView extends RecyclerView.ViewHolder{

        TextView text;

        public CategoryView(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
        }

    }

    public Category getItem(int position){
        return categories.get(position);
    }
}
