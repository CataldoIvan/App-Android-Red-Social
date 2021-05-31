package com.example.redsocial.entidades;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class HistoriaAdaptador extends ArrayAdapter<Historia> {
    List<Historia> listHistorias;
    Context context;
    public int layoutResource;


    public HistoriaAdaptador(@NonNull Context context, int resource, @NonNull List<Historia> objects) {
        super(context, resource, objects);
        this.context=context;
        this.layoutResource=resource;
        this.listHistorias=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);


    }
}
