package com.SB.SBtugar.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.SB.SBtugar.Activities.ProductDetailsActivity;
import com.SB.SBtugar.AllModels.AttributeModel;
import com.SB.SBtugar.AllModels.Image;
import com.SB.SBtugar.R;
import com.SB.SBtugar.listener.listen_to_variation;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class
Variation2Adapter extends RecyclerView.Adapter<Variation2Adapter.ViewHolder> {
    Context context;
    List<String> variationsStringList;
    List<AttributeModel> ArrListOptions;
    ArrayList<Integer> selectedIndex;
    listen_to_variation listener;
    public Variation2Adapter(Context context, List<String> variationsStringList , List<AttributeModel> ArrListOptions, ArrayList<Integer> selectedIndex, listen_to_variation listener) {
        this.context = context;
        this.variationsStringList = variationsStringList;
        this.selectedIndex = selectedIndex;
        this.ArrListOptions = ArrListOptions;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.variation_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (variationsStringList.get(position).contains("-")) {
            if (Locale.getDefault().getLanguage().equals("ar"))
                    holder.option.setText(variationsStringList.get(position).split("-")[1]+" :");
            else
                holder.option.setText(variationsStringList.get(position).split("-")[0]+" :");
        }
        else {
            holder.option.setText(variationsStringList.get(position)+" :");
        }

        if (variationsStringList.get(position).contains("model")){
//            Glide.with(context).load(ArrListOptions.get(position).getVariations_images().get(selectedIndex.get(position))).into(holder.img);
//            holder.txt.setText(ArrListOptions.get(position).getOptions().get(selectedIndex.get(position)));
//            holder.container.setOnClickListener(view -> {
//                listener.onImageSelected(position);
//            });
//            holder.container.setVisibility(View.VISIBLE);

            if (Locale.getDefault().getLanguage().equals("ar"))
                holder.option.setText("اختر موديل:");
            else
                holder.option.setText("Choose Model :");

            holder.spinner.setVisibility(View.GONE);

           holder.recyclerView.setNestedScrollingEnabled(true);
            holder.recyclerView.setHasFixedSize(true);
            GridLayoutManager layoutManager2 = new GridLayoutManager(context, 4);
            holder.recyclerView.setLayoutManager(layoutManager2);
            VariationAdapter adapter = new VariationAdapter(context, ArrListOptions.get(position).getVariations_images() , ArrListOptions.get(position).getOptions(), selectedIndex.get(position), listener, position);
            holder.recyclerView.setAdapter(adapter);
            holder.recyclerView.setVisibility(View.VISIBLE);

        }
        else {
            holder.container.setVisibility(View.GONE);
            holder.spinner.setVisibility(View.VISIBLE);
            StateSpinnerAdapter stateAdapter = new StateSpinnerAdapter(context,
                    R.layout.spinner_item_is, ArrListOptions.get(position).getOptions());
            stateAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            holder.spinner.setEnabled(true);
            holder.spinner.setClickable(true);
            holder.spinner.setAdapter(stateAdapter);

            holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    listener.onVariationSelected(i,position,false);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return variationsStringList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView option;
        ImageView img;
        TextView txt;
        LinearLayout container;
        Spinner spinner;
        RecyclerView recyclerView;
        public ViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recycler);
            option = itemView.findViewById(R.id.title);
            txt = itemView.findViewById(R.id.txt);
            img = itemView.findViewById(R.id.img);
            spinner = itemView.findViewById(R.id.spinner);
            container = itemView.findViewById(R.id.container);
        }
    }
}