package com.gestione.distributori.model;

import java.sql.Date;

public class ListiniClienti {
    float KG;
    float costoArticolo;
    Date dataRicarica;
    int idArticolo;
    int idCategoria;
    int idCliente;
    int idMacchina;
    String nomeArticolo;
    float prezzoVendita;
    String tipologia;

    public void setIdCliente(int idCliente2) {
        this.idCliente = idCliente2;
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

    public void setCostoArticolo(float costoArticolo2) {
        this.costoArticolo = costoArticolo2;
    }

    public void setPrezzoVendita(float prezzoVendita2) {
        this.prezzoVendita = prezzoVendita2;
    }

    public void setTipologia(String tipologia2) {
        this.tipologia = tipologia2;
    }

    public void setIdCategoria(int idcategoria) {
        this.idCategoria = idcategoria;
    }

    public int getIdCliente() {
        return this.idCliente;
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

    public float getCostoArticolo() {
        return this.costoArticolo;
    }

    public float getPrezzoVendita() {
        return this.prezzoVendita;
    }

    public String getTipologia() {
        return this.tipologia;
    }

    public int getidCategoria() {
        return this.idCategoria;
    }
}
