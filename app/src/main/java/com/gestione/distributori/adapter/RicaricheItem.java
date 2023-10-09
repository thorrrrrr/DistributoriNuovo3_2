package com.gestione.distributori.adapter;

public class RicaricheItem {
    private String dataRicaricaArt;
    private String descrizioneQuantArt;
    private String idRigaRicarica;
    boolean selected = false;

    public RicaricheItem(String descrizioneQuantArt2, String dataRicaricaArt2, String idRigaRicarica2, boolean selected2) {
        this.descrizioneQuantArt = descrizioneQuantArt2;
        this.dataRicaricaArt = dataRicaricaArt2;
        this.idRigaRicarica = idRigaRicarica2;
        this.selected = selected2;
    }

    public void setDescrizioneQuantArt(String descrizioneQuantArt2) {
        this.descrizioneQuantArt = descrizioneQuantArt2;
    }

    public void setDataRicaricaArt(String dataRicaricaArt2) {
        this.dataRicaricaArt = dataRicaricaArt2;
    }

    public void setIdRigaRicarica(String idRigaRicarica2) {
        this.idRigaRicarica = idRigaRicarica2;
    }

    public void SetSelected(boolean selected2) {
        this.selected = selected2;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public String getDescrizioneQuantArt() {
        return this.descrizioneQuantArt;
    }

    public String getDataRicaricaArt() {
        return this.dataRicaricaArt;
    }

    public String getIdRigaRicarica() {
        return this.idRigaRicarica;
    }

    public boolean getSelected() {
        return this.selected;
    }
}
