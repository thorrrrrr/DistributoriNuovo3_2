package com.gestione.distributori.model;

public class RicavoMacchina {
    int battuteCaldo;
    int battuteFreddo;
    String consolidato;
    String dataScaricoSoldi;
    int idMacchina;
    int idRigaRicavo;
    float soldiScaricati;
    String trasmesso;

    public void setIdRigaRicavo(int idRigaRicavo2) {
        this.idRigaRicavo = idRigaRicavo2;
    }

    public void setIdMacchina(int idMacchina2) {
        this.idMacchina = idMacchina2;
    }

    public void setDataScaricoSoldi(String dataScaricoSoldi2) {
        this.dataScaricoSoldi = dataScaricoSoldi2;
    }

    public void setSoldiScaricati(float soldiScaricati2) {
        this.soldiScaricati = soldiScaricati2;
    }

    public void setConsolidato(String consolidato2) {
        this.consolidato = consolidato2;
    }

    public void setTrasmesso(String trasmesso2) {
        this.trasmesso = trasmesso2;
    }

    public void setBattuteFreddo(int battuteFreddo2) {
        this.battuteFreddo = battuteFreddo2;
    }

    public void setBattuteCaldo(int battuteCaldo2) {
        this.battuteCaldo = battuteCaldo2;
    }

    public int getIdRigaRicavo() {
        return this.idRigaRicavo;
    }

    public int getIdMacchina() {
        return this.idMacchina;
    }

    public String getDataScaricoSoldi() {
        return this.dataScaricoSoldi;
    }

    public float getSoldiScaricati() {
        return this.soldiScaricati;
    }

    public String getCondolidato() {
        return this.consolidato;
    }

    public String getTrasmesso() {
        return this.trasmesso;
    }

    public int getBattuteFreddo() {
        return this.battuteFreddo;
    }

    public int getBattuteCaldo() {
        return this.battuteCaldo;
    }
}
