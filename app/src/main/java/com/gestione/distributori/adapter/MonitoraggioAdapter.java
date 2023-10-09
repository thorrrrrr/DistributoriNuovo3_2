package com.gestione.distributori.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gestione.distributori.R;
import com.gestione.distributori.InsManutenzioneActivity;

import java.util.ArrayList;

public class MonitoraggioAdapter extends ArrayAdapter<MonitoraggioItem> {
    private final Bundle bundle;
    private final Context context;
    private final ArrayList<MonitoraggioItem> itemsArrayList;

    public MonitoraggioAdapter(Context context2, ArrayList<MonitoraggioItem> itemsArrayList2, Bundle bundle2) {
        super(context2, R.layout.monitoraggio_row, itemsArrayList2);
        this.context = context2;
        this.itemsArrayList = itemsArrayList2;
        this.bundle = bundle2;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = ((LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.monitoraggio_row, parent, false);
        TextView cliente = (TextView) rowView.findViewById(R.id.cliente);
        TextView modello = (TextView) rowView.findViewById(R.id.modello);
        TextView tipomacchina = (TextView) rowView.findViewById(R.id.tipomacchina);
        cliente.setText(this.itemsArrayList.get(position).getcliente());
        ((TextView) rowView.findViewById(R.id.idmacchina)).setText(this.itemsArrayList.get(position).getidmacchina());
        modello.setText(this.itemsArrayList.get(position).getmodello());
        ((TextView) rowView.findViewById(R.id.idtipomacchina)).setText(this.itemsArrayList.get(position).getidtipomacchina());
        tipomacchina.setText(this.itemsArrayList.get(position).gettipomacchina());
        ((TextView) rowView.findViewById(R.id.descrizione)).setText(this.itemsArrayList.get(position).getdescrizione());
        ((TextView) rowView.findViewById(R.id.idtipomanutenzione)).setText(this.itemsArrayList.get(position).getidtipomanutenzione());
        ((TextView) rowView.findViewById(R.id.battuteattuali)).setText(this.itemsArrayList.get(position).getbattuteattuali());
        ((TextView) rowView.findViewById(R.id.battutemanutenzione)).setText(this.itemsArrayList.get(position).getbattutemacchina());
        ((TextView) rowView.findViewById(R.id.datascadenza)).setText(this.itemsArrayList.get(position).getdatascadenzamanutenzione());
        cliente.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean unused = MonitoraggioAdapter.this.ClickEvent(v);
            }
        });
        modello.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean unused = MonitoraggioAdapter.this.ClickEvent(v);
            }
        });
        tipomacchina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean unused = MonitoraggioAdapter.this.ClickEvent(v);
            }
        });
        rowView.setClickable(true);
        rowView.setFocusable(true);
        return rowView;
    }

    public boolean isEnabled(int position) {
        return true;
    }

    /* access modifiers changed from: private */
    public boolean ClickEvent(View v) {
        this.bundle.putString("midmacchina", String.valueOf(((TextView) ((View) v.getParent().getParent()).findViewById(R.id.idmacchina)).getText()));
        this.bundle.putString("mcliente", String.valueOf(((TextView) ((View) v.getParent().getParent()).findViewById(R.id.cliente)).getText()));
        this.bundle.putString("mmodello", String.valueOf(((TextView) ((View) v.getParent().getParent()).findViewById(R.id.modello)).getText()));
        this.bundle.putString("midtipomacchina", String.valueOf(((TextView) ((View) v.getParent().getParent()).findViewById(R.id.idtipomacchina)).getText()));
        this.bundle.putString("mtipomacchina", String.valueOf(((TextView) ((View) v.getParent().getParent()).findViewById(R.id.tipomacchina)).getText()));
        this.bundle.putString("midtipomanutenzione", String.valueOf(((TextView) ((View) v.getParent().getParent()).findViewById(R.id.idtipomanutenzione)).getText()));
        Intent intent = new Intent(v.getContext(), InsManutenzioneActivity.class);
        intent.putExtras(this.bundle);
        v.getContext().startActivity(intent);
        return true;
    }
}
