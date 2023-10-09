package com.gestione.distributori.model;

public class Manutenzione {
    String DataManutenzione;
    String DatascadenzaMan;
    String Descrizione;
    int IDTipoManutenzione;
    int IdMacchina;
    int NBattute;
    String Note;

    public void setIdMacchina(int IdMacchina2) {
        this.IdMacchina = IdMacchina2;
    }

    public void setDataManutenzione(String DataManutenzione2) {
        this.DataManutenzione = DataManutenzione2;
    }

    public void setDescrizione(String Descrizione2) {
        this.Descrizione = Descrizione2;
    }

    public void setDatascadenzaMan(String DatascadenzaMan2) {
        this.DatascadenzaMan = DatascadenzaMan2;
    }

    public void setNBattute(int NBattute2) {
        this.NBattute = NBattute2;
    }

    public void setNote(String Note2) {
        this.Note = Note2;
    }

    public void setIDTipoManutenzione(int IDTipoManutenzione2) {
        this.IDTipoManutenzione = IDTipoManutenzione2;
    }

    public int getIdMacchina() {
        return this.IdMacchina;
    }

    public String getDataManutenzione() {
        return this.DataManutenzione;
    }

    public String getDescrizione() {
        return this.Descrizione;
    }

    public String getDatascadenzaMan() {
        return this.DatascadenzaMan;
    }

    public int getNBattute() {
        return this.NBattute;
    }

    public String getNote() {
        return this.Note;
    }

    public int getIDTipoManutenzione() {
        return this.IDTipoManutenzione;
    }
}
