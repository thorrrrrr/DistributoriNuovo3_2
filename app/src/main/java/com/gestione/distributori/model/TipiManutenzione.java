package com.gestione.distributori.model;

public class TipiManutenzione {
    String IdTipoMacchina;
    int IdTipoManutenzione;
    String NBattute;
    String NGgPreavviso;
    String NGgScadenza;
    String TipoManutenzioneDescrizione;

    public void setIdTipoManutenzione(int IdTipoManutenzione2) {
        this.IdTipoManutenzione = IdTipoManutenzione2;
    }

    public void setTipoManutenzioneDescrizione(String TipoManutenzioneDescrizione2) {
        this.TipoManutenzioneDescrizione = TipoManutenzioneDescrizione2;
    }

    public void setNBattute(String NBattute2) {
        this.NBattute = NBattute2;
    }

    public void setNGgScadenza(String NGgScadenza2) {
        this.NGgScadenza = NGgScadenza2;
    }

    public void setNGgPreavviso(String NGgPreavviso2) {
        this.NGgPreavviso = NGgPreavviso2;
    }

    public void setIdTipoMacchina(String IdTipoMacchina2) {
        this.IdTipoMacchina = IdTipoMacchina2;
    }

    public int getIdTipoManutenzione() {
        return this.IdTipoManutenzione;
    }

    public String getTipoManutenzioneDescrizione() {
        return this.TipoManutenzioneDescrizione;
    }

    public String getNBattute() {
        return this.NBattute;
    }

    public String getNGgScadenza() {
        return this.NGgScadenza;
    }

    public String getNGgPreavviso() {
        return this.NGgPreavviso;
    }

    public String getIdTipoMacchina() {
        return this.IdTipoMacchina;
    }
}
