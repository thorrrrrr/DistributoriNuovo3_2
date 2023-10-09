package com.gestione.distributori.model;

public class Settings {
    String descrizione;
    int idSettings;
    String valore;

    public void setIdSettings(int idSettings2) {
        this.idSettings = idSettings2;
    }

    public void setDescrizione(String descrizione2) {
        this.descrizione = descrizione2;
    }

    public void setValore(String valore2) {
        this.valore = valore2;
    }

    public int getIdSettings() {
        return this.idSettings;
    }

    public String getDescrizione() {
        return this.descrizione;
    }

    public String getValore() {
        return this.valore;
    }
}
