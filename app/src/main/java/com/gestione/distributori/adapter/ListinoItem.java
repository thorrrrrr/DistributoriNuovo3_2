package com.gestione.distributori.adapter;

public class ListinoItem {
    private String descrizioneArt;
    private String idArticolo;
    private String prezzoVenditaArt;

    public ListinoItem(String descrizioneArt2, String prezzoVenditaArt2, String idArticolo2) {
        this.descrizioneArt = descrizioneArt2;
        this.prezzoVenditaArt = prezzoVenditaArt2;
        this.idArticolo = idArticolo2;
    }

    public void setDescrizioneArt(String descrizioneArt2) {
        this.descrizioneArt = descrizioneArt2;
    }

    public void setPrezzoVenditaArt(String prezzoVenditaArt2) {
        this.prezzoVenditaArt = prezzoVenditaArt2;
    }

    public void setIdArticolo(String idArticolo2) {
        this.idArticolo = idArticolo2;
    }

    public String getDescrizioneArt() {
        return this.descrizioneArt;
    }

    public String getPrezzoVenditaArt() {
        return this.prezzoVenditaArt;
    }

    public String getIdArticolo() {
        return this.idArticolo;
    }
}
