package com.example.diaconescu_andrei_alexandru_1088;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<Locatie> {
    private Context context;
    private int resource;
    private LayoutInflater layoutInflater;
    private List<Locatie> locatieList;
    public CustomAdapter(@NonNull Context context, int resource, LayoutInflater layoutInflater, @NonNull List<Locatie> locatieList) {
        super(context, resource, locatieList);
        this.context = context;
        this.resource = resource;
        this.layoutInflater = layoutInflater;
        this.locatieList = locatieList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = layoutInflater.inflate(resource, parent, false);
        Locatie locatie = locatieList.get(position);
        if(locatie != null) {
            TextView textView = view.findViewById(android.R.id.text1);
            textView.setText("Locatie: " + Integer.toString(position) + " " + locatie.toString());
        }
        return view;
    }
}
