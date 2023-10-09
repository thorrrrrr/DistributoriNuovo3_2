package com.gestione.distributori.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gestione.distributori.R;

import java.util.ArrayList;

public class ListinoAdapter extends ArrayAdapter<ListinoItem> {
    private final Context context;
    private final ArrayList<ListinoItem> itemsArrayList;

    public ListinoAdapter(Context context2, ArrayList<ListinoItem> itemsArrayList2) {
        super(context2, R.layout.listino_row, itemsArrayList2);
        this.context = context2;
        this.itemsArrayList = itemsArrayList2;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = ((LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listino_row, parent, false);
        ((TextView) rowView.findViewById(R.id.descrizioneArt)).setText(this.itemsArrayList.get(position).getDescrizioneArt());
        ((TextView) rowView.findViewById(R.id.prezzoVendArt)).setText(this.itemsArrayList.get(position).getPrezzoVenditaArt());
        ((TextView) rowView.findViewById(R.id.idArticoloListino)).setText(this.itemsArrayList.get(position).getIdArticolo());
        return rowView;
    }
}
