package com.gestione.distributori.adapter;

import android.widget.CheckBox;

public class RicaviItem {
    public CheckBox ckbx;
    private String dataScaricoSoldi;
    private String idRigaRicavo;
    boolean selected = false;
    private float soldiScaricati;

    public RicaviItem(String dataScaricoSoldi2, float soldiScaricati2, String idRigaRicavo2, boolean selected2) {
        this.dataScaricoSoldi = dataScaricoSoldi2;
        this.soldiScaricati = soldiScaricati2;
        this.idRigaRicavo = idRigaRicavo2;
        this.selected = selected2;
    }

    public void setDataScaricoSoldi(String dataScaricoSoldi2) {
        this.dataScaricoSoldi = dataScaricoSoldi2;
    }

    public void setSoldiScaricati(float soldiScaricati2) {
        this.soldiScaricati = soldiScaricati2;
    }

    public void setIdRigaRicavo(String idRigaRicavo2) {
        this.idRigaRicavo = idRigaRicavo2;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setCkbx(CheckBox ckbx2) {
        this.ckbx = ckbx2;
    }

    public String getDataScaricoSoldi() {
        return this.dataScaricoSoldi;
    }

    public float getSoldiScaricati() {
        return this.soldiScaricati;
    }

    public String getIdRigaRicavo() {
        return this.idRigaRicavo;
    }
}
