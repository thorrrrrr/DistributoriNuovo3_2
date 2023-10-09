package com.gestione.distributori.model;

public class Magazzino {
    int idarticolo;
    float kgresidua;
    int qtaconfezioniresidua;

    public void setKgResidua(float kgresidua2) {
        this.kgresidua = kgresidua2;
    }

    public void setIdArticolo(int idarticolo2) {
        this.idarticolo = idarticolo2;
    }

    public void setQtaConfezioniResidua(int qtaconfezioniresidua2) {
        this.qtaconfezioniresidua = qtaconfezioniresidua2;
    }

    public float getKgResidua() {
        return this.kgresidua;
    }

    public int getIdArticolo() {
        return this.idarticolo;
    }

    public int getQtaConfezioniResidua() {
        return this.qtaconfezioniresidua;
    }
}
