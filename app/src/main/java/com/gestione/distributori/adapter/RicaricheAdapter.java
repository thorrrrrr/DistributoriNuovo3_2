package com.gestione.distributori.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.gestione.distributori.R;

import java.util.ArrayList;

public class RicaricheAdapter extends ArrayAdapter<RicaricheItem> {
    private final Context context;
    public final ArrayList<RicaricheItem> itemsArrayList;

    public RicaricheAdapter(Context context2, ArrayList<RicaricheItem> itemsArrayList2) {
        super(context2, R.layout.ricariche_row, itemsArrayList2);
        this.context = context2;
        this.itemsArrayList = itemsArrayList2;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = ((LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.ricariche_row, parent, false);
        ((TextView) rowView.findViewById(R.id.descrizioneQuantArt)).setText(this.itemsArrayList.get(position).getDescrizioneQuantArt());
        ((TextView) rowView.findViewById(R.id.dataRicaricaArt)).setText(this.itemsArrayList.get(position).getDataRicaricaArt());
        ((TextView) rowView.findViewById(R.id.idRigaRicarica)).setText(this.itemsArrayList.get(position).getIdRigaRicarica());
        final int IdPosition = position;
        CheckBox ckbx = (CheckBox) rowView.findViewById(R.id.chkSelCons);
        ckbx.setChecked(this.itemsArrayList.get(position).isSelected());
        ckbx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buttonView.setChecked(isChecked);
                RicaricheAdapter.this.itemsArrayList.get(IdPosition).SetSelected(isChecked);
            }
        });
        return rowView;
    }
}
