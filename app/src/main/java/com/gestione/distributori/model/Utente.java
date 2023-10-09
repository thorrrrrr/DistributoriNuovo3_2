package com.gestione.distributori.model;

public class Utente {
    String abilitato;
    int idUtente;
    String password;
    String tipoProfilo;
    String utenza;

    public void setIdUtente(int idUtente2) {
        this.idUtente = idUtente2;
    }

    public void setUtenza(String utenza2) {
        this.utenza = utenza2;
    }

    public void setPassword(String password2) {
        this.password = password2;
    }

    public void setTipoProfilo(String tipoProfilo2) {
        this.tipoProfilo = tipoProfilo2;
    }

    public void setAbilitato(String abilitato2) {
        this.abilitato = abilitato2;
    }

    public int getIdUtente() {
        return this.idUtente;
    }

    public String getUtenza() {
        return this.utenza;
    }

    public String getPassword() {
        return this.password;
    }

    public String getTipoProfilo() {
        return this.tipoProfilo;
    }

    public String getAbilitato() {
        return this.abilitato;
    }
}
