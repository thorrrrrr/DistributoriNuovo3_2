package com.gestione.distributori.model;

public class MonitoraggioDistributori {
    String BattuteAttuali;
    String BattuteManutenzione;
    String Descrizione;
    String IdTipoMacchina;
    String IdTipomanutenzione;
    String Idmacchina;
    String Modello;
    String RagioneSociale;
    String ScadenzaManutenzione;
    String TipoMacchina;

    public void setRagioneSociale(String RagioneSociale2) {
        this.RagioneSociale = RagioneSociale2;
    }

    public void setModello(String Modello2) {
        this.Modello = Modello2;
    }

    public void setIdTipoMacchina(String IdTipoMacchina2) {
        this.IdTipoMacchina = IdTipoMacchina2;
    }

    public void setTipoMacchina(String TipoMacchina2) {
        this.TipoMacchina = TipoMacchina2;
    }

    public void setDescrizione(String Descrizione2) {
        this.Descrizione = Descrizione2;
    }

    public void setIdTipomanutenzione(String IdTipomanutenzione2) {
        this.IdTipomanutenzione = IdTipomanutenzione2;
    }

    public void setBattuteAttuali(String BattuteAttuali2) {
        this.BattuteAttuali = BattuteAttuali2;
    }

    public void setBattuteManutenzione(String BattuteManutenzione2) {
        this.BattuteManutenzione = BattuteManutenzione2;
    }

    public void setScadenzaManutenzione(String ScadenzaManutenzione2) {
        this.ScadenzaManutenzione = ScadenzaManutenzione2;
    }

    public void setIdMacchina(String Idmacchina2) {
        this.Idmacchina = Idmacchina2;
    }

    public String getRagioneSociale() {
        return this.RagioneSociale;
    }

    public String getModello() {
        return this.Modello;
    }

    public String getTipoMacchina() {
        return this.TipoMacchina;
    }

    public String getIdTipoMacchina() {
        return this.IdTipoMacchina;
    }

    public String getDescrizione() {
        return this.Descrizione;
    }

    public String getIdTipomanutenzione() {
        return this.IdTipomanutenzione;
    }

    public String getBattuteAttuali() {
        return this.BattuteAttuali;
    }

    public String getBattuteManutenzione() {
        return this.BattuteManutenzione;
    }

    public String getScadenzaManutenzione() {
        return this.ScadenzaManutenzione;
    }

    public String getIdmacchina() {
        return this.Idmacchina;
    }
}
