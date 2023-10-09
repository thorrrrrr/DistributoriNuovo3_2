package com.gestione.distributori.model;

public class RicaricaMacchina {
    float KG;
    String consolidato;
    float costoAcquisto;
    String dataRicarica;
    int idArticolo;
    int idMacchina;
    int idRigaRicarica;
    String nomeArticolo;
    float prezzoVendita;
    int qtaConfezione;
    String trasmesso;

    public void setIdRigaRicarica(int idRigaRicarica2) {
        this.idRigaRicarica = idRigaRicarica2;
    }

    public void setIdMacchina(int idMacchina2) {
        this.idMacchina = idMacchina2;
    }

    public void setIdArticolo(int idArticolo2) {
        this.idArticolo = idArticolo2;
    }

    public void setNomeArticolo(String nomeArticolo2) {
        this.nomeArticolo = nomeArticolo2;
    }

    public void setQtaConfezione(int qtaConfezione2) {
        this.qtaConfezione = qtaConfezione2;
    }

    public void setKG(float KG2) {
        this.KG = KG2;
    }

    public void setDataRicarica(String dataRicarica2) {
        this.dataRicarica = dataRicarica2;
    }

    public void setCostoAcquisto(float costoAcquisto2) {
        this.costoAcquisto = costoAcquisto2;
    }

    public void setPrezzoVendita(float prezzoVendita2) {
        this.prezzoVendita = prezzoVendita2;
    }

    public void setConsolidato(String consolidato2) {
        this.consolidato = consolidato2;
    }

    public void setTrasmesso(String trasmesso2) {
        this.trasmesso = trasmesso2;
    }

    public int getIdRigaRicarica() {
        return this.idRigaRicarica;
    }

    public int getIdMacchina() {
        return this.idMacchina;
    }

    public int getIdArticolo() {
        return this.idArticolo;
    }

    public String getNomeArticolo() {
        return this.nomeArticolo;
    }

    public int getQtaConfezione() {
        return this.qtaConfezione;
    }

    public float getKG() {
        return this.KG;
    }

    public String getDataRicarica() {
        return this.dataRicarica;
    }

    public float getCostoAcquisto() {
        return this.costoAcquisto;
    }

    public float getPrezzoVendita() {
        return this.prezzoVendita;
    }

    public String getCondolidato() {
        return this.consolidato;
    }

    public String getTrasmesso() {
        return this.trasmesso;
    }
}
