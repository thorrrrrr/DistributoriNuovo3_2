package com.gestione.distributori.model;

public class Categorie {
    String DescCategorie;
    String FlagVisibile;
    int IdCategorie;

    public void setIdCategorie(int idCategorie) {
        this.IdCategorie = idCategorie;
    }

    public void setDescCategorie(String DescCategorie2) {
        this.DescCategorie = DescCategorie2;
    }

    public void setFlagVisibile(String FlagVisibile2) {
        this.FlagVisibile = FlagVisibile2;
    }

    public int getIdCategorie() {
        return this.IdCategorie;
    }

    public String getDescCategorie() {
        return this.DescCategorie;
    }

    public String getFlagVisibile() {
        return this.FlagVisibile;
    }
}
