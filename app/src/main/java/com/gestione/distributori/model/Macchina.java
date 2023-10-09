package com.gestione.distributori.model;

public class Macchina {
    String codice;
    int idMacchina;
    String marca;
    String modello;

    public void setIdMacchina(int idMacchina2) {
        this.idMacchina = idMacchina2;
    }

    public void setMarca(String marca2) {
        this.marca = marca2;
    }

    public void setModello(String modello2) {
        this.modello = modello2;
    }

    public void setCodice(String codice2) {
        this.codice = codice2;
    }

    public int getIdMacchina() {
        return this.idMacchina;
    }

    public String getMarca() {
        return this.marca;
    }

    public String getModello() {
        return this.modello;
    }

    public String getCodice() {
        return this.codice;
    }
}
