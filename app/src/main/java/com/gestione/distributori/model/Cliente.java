package com.gestione.distributori.model;

public class Cliente {
    int idCliente;
    String ragioneSociale;

    public void setIdCliente(int idCliente2) {
        this.idCliente = idCliente2;
    }

    public void setRagioneSociale(String ragioneSociale2) {
        this.ragioneSociale = ragioneSociale2;
    }

    public int getIdCliente() {
        return this.idCliente;
    }

    public String getRagioneSociale() {
        return this.ragioneSociale;
    }
}
