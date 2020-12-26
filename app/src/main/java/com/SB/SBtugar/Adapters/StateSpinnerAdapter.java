package com.SB.SBtugar.Adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Project ${PROJECT}
 * Created by asamy on 4/23/2018.
 */

public class StateSpinnerAdapter extends ArrayAdapter<String> {

    private List<String> states;

    @Override
    public int getCount() {
        return states.size();
    }

    public StateSpinnerAdapter(@NonNull Context context, int resource, List<String> stateList) {
        super(context, resource);
        this.states = stateList;
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return states.get(position);
    }

    public String getItemData(int position){
        return states.get(position);
    }
}
