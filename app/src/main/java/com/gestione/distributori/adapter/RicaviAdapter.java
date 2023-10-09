package com.gestione.distributori.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.gestione.distributori.R;

import java.util.ArrayList;

public class RicaviAdapter extends ArrayAdapter<RicaviItem> {
    private final Context context;
    private final ArrayList<RicaviItem> itemsArrayList;

    public RicaviAdapter(Context context2, ArrayList<RicaviItem> itemsArrayList2) {
        super(context2, R.layout.ricavi_row, itemsArrayList2);
        this.context = context2;
        this.itemsArrayList = itemsArrayList2;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = ((LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.ricavi_row, parent, false);
        ((TextView) rowView.findViewById(R.id.dataScaricoSoldi)).setText(this.itemsArrayList.get(position).getDataScaricoSoldi());
        ((TextView) rowView.findViewById(R.id.soldiScaricati)).setText(Float.toString(this.itemsArrayList.get(position).getSoldiScaricati()));
        ((TextView) rowView.findViewById(R.id.idRigaRicavo)).setText(this.itemsArrayList.get(position).getIdRigaRicavo());
        ((CheckBox) rowView.findViewById(R.id.chkSelConsRic)).setChecked(this.itemsArrayList.get(position).isSelected());
        return rowView;
    }
}
