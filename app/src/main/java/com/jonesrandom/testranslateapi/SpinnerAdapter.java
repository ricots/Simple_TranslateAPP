package com.jonesrandom.testranslateapi;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter{

    List<Langs> data;
    int rowLayout;

    public SpinnerAdapter(Context context, int resource, List<Langs> objects) {
        super(context, resource, objects);

        rowLayout = resource;
        data = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View view, ViewGroup parent) {

        view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent ,false);

        TextView Bahasa = (TextView) view.findViewById(R.id.row_langs);
        TextView idBahasa = (TextView) view.findViewById(R.id.row_id);

        Bahasa.setText(data.get(position).getLangs());
        idBahasa.setText(data.get(position).getId());

        return view;
    }


}
