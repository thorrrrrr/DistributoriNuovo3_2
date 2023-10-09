package com.gestione.distributori.adapter;

import java.text.SimpleDateFormat;

public class MonitoraggioItem {
    private String battuteattuali;
    private String battutemacchina;
    private String cliente;
    private String datascadenzamanutenzione;
    private String descrizione;
    private String idmacchina;
    private String idtipomacchina;
    private String idtipomanutenzione;
    private String modello;
    private String tipomacchina;

    public MonitoraggioItem(String cliente2, String idmacchina2, String modello2, String idtipomacchina2, String tipomacchina2, String idtipomanutenzione2, String descrizione2, String battuteattuali2, String battutemacchina2, String datascadenzamanutenzione2) {
        this.cliente = cliente2;
        this.idmacchina = idmacchina2;
        this.modello = modello2;
        this.idtipomacchina = idtipomacchina2;
        this.tipomacchina = tipomacchina2;
        this.descrizione = descrizione2;
        this.battuteattuali = battuteattuali2;
        this.battutemacchina = battutemacchina2;
        try {
            this.datascadenzamanutenzione = new SimpleDateFormat("dd-MM-yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(datascadenzamanutenzione2)).toString();
        } catch (Exception e) {
        }
        this.idtipomanutenzione = idtipomanutenzione2;
    }

    public void setcliente(String cliente2) {
        this.cliente = cliente2;
    }

    public void setidmacchina(String idmacchina2) {
        this.idmacchina = idmacchina2;
    }

    public void setmodello(String modello2) {
        this.modello = modello2;
    }

    public void setidtipomacchina(String idtipomacchina2) {
        this.idtipomacchina = idtipomacchina2;
    }

    public void settipomacchina(String tipomacchina2) {
        this.tipomacchina = tipomacchina2;
    }

    public void setdescrizione(String descrizione2) {
        this.descrizione = descrizione2;
    }

    public void setidtipomanutenzione(String idtipomanutenzione2) {
        this.idtipomanutenzione = idtipomanutenzione2;
    }

    public void setbattuteattuali(String battuteattuali2) {
        this.battuteattuali = battuteattuali2;
    }

    public void setbattutemacchina(String battutemacchina2) {
        this.battutemacchina = battutemacchina2;
    }

    public void setdatascadenzamanutenzione(String datascadenzamanutenzione2) {
        this.datascadenzamanutenzione = datascadenzamanutenzione2;
    }

    public String getcliente() {
        return this.cliente;
    }

    public String getidmacchina() {
        return this.idmacchina;
    }

    public String getmodello() {
        return this.modello;
    }

    public String getidtipomacchina() {
        return this.idtipomacchina;
    }

    public String gettipomacchina() {
        return this.tipomacchina;
    }

    public String getdescrizione() {
        return this.descrizione;
    }

    public String getidtipomanutenzione() {
        return this.idtipomanutenzione;
    }

    public String getbattuteattuali() {
        return this.battuteattuali;
    }

    public String getbattutemacchina() {
        return this.battutemacchina;
    }

    public String getdatascadenzamanutenzione() {
        return this.datascadenzamanutenzione;
    }
}
